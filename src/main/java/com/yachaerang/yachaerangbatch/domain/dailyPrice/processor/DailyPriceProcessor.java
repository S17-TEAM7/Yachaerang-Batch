package com.yachaerang.yachaerangbatch.domain.dailyPrice.processor;

import com.yachaerang.yachaerangbatch.domain.dto.KamisPriceItem;
import com.yachaerang.yachaerangbatch.domain.entity.DailyPrice;
import com.yachaerang.yachaerangbatch.domain.entity.Product;
import com.yachaerang.yachaerangbatch.exception.GeneralException;
import com.yachaerang.yachaerangbatch.repository.DailyPriceRepository;
import com.yachaerang.yachaerangbatch.repository.ProductRepository;
import com.yachaerang.yachaerangbatch.util.PriceParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/*
Daily Price Job의 Processor
 */
@Slf4j
@RequiredArgsConstructor
public class DailyPriceProcessor implements ItemProcessor<KamisPriceItem, DailyPrice> {

    private final ProductRepository productRepository;
    private final DailyPriceRepository dailyPriceRepository;

    private final LocalDate targetDate;

    private static final String PREFIX = "KM-";

    @Override
    public DailyPrice process(KamisPriceItem item) {
        log.debug("Processing: {} - {} - {}", item.getItemName(), item.getKindName(), item.getRankCode());

        // 가격 파싱
        Long todayPrice = PriceParser.parse(item.getDpr1());
        if (todayPrice == null) {
            log.debug("가격 정보 없음, 건너뜀: {}", item.getItemName());
            return null;
        }

        // Product 조회 또는 생성
        Product product = findOrCreateProduct(item);
        if (dailyPriceRepository.existsByProductAndPriceDate(product, targetDate)) {
            log.debug("이미 존재하는 데이터, 건너뜀: productCode={}, date={}",
                    product.getProductCode(), targetDate);
            return null;
        }

        // 가장 최근 가격 조회
        Long priceChange = 0L;
        BigDecimal priceChangeRate = new BigDecimal(0.00);

        Long latestPrice = dailyPriceRepository.findLatestPriceByProductCode(product.getProductCode(), targetDate);
        if (latestPrice != null && latestPrice > 0) {
            priceChange = todayPrice - latestPrice;
            priceChangeRate = BigDecimal.valueOf(priceChange)
                    .divide(BigDecimal.valueOf(latestPrice), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
        // DailyPrice 생성
        return DailyPrice.builder()
                .productCode(product.getProductCode())
                .priceDate(targetDate)
                .price(todayPrice)
                .priceChange(priceChange)
                .priceChangeRate(priceChangeRate)
                .build();
    }

    /*
    Product 반환
     */
    private Product findOrCreateProduct(KamisPriceItem item) {
        String productCode = PREFIX + item.getItemCode() + "-" + item.getKindCode() + "-" + item.getRankCode();

        Product existing = productRepository.findByProductCode(productCode);
        if (existing != null) {
            return existing;
        } else {
            // save to DB
            return createNewProduct(item);
        }
    }

    /*
    DB에 저장
     */
    private Product createNewProduct(KamisPriceItem item) {
        log.info("신규 상품 등록: {} - {} - {}",
                item.getItemName(), item.getKindName(), item.getRank());

        Product newProduct = Product.builder()
                .name(item.getKindName() + " - " + item.getRank())
                .productCode(PREFIX + item.getItemCode() + "-" + item.getKindCode() + "-" + item.getRankCode())
                .itemName(item.getItemName())
                .itemCode(item.getItemCode())
                .kindName(item.getKindName())
                .kindCode(item.getKindCode())
                .productRank(item.getRank())
                .rankCode(item.getRankCode())
                .unit(item.getUnit())
                .build();

        if (productRepository.save(newProduct) == 1) {
            return newProduct;
        } else {
            throw new GeneralException(newProduct.getProductCode() + " - 생성 실패");
        }
    }
}
