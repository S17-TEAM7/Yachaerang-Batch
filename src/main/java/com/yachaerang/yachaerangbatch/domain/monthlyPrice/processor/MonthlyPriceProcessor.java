package com.yachaerang.yachaerangbatch.domain.monthlyPrice.processor;

import com.yachaerang.yachaerangbatch.domain.entity.MonthlyPrice;
import com.yachaerang.yachaerangbatch.repository.DailyPriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Slf4j
@RequiredArgsConstructor
public class MonthlyPriceProcessor implements ItemProcessor<MonthlyPrice, MonthlyPrice> {

    private final DailyPriceRepository dailyPriceRepository;

    @Override
    public MonthlyPrice process(MonthlyPrice item) throws Exception {

        // null 일 경우 조회
        if (item.getPriceCount() == 0) {
            log.debug("priceCount가 0이므로 스킵: {}", item.getProductCode());
            return null;
        }

        String productCode = item.getProductCode();
        Integer priceYear = item.getPriceYear();
        Integer priceMonth = item.getPriceMonth();

        LocalDate monthStartDate = LocalDate.of(priceYear, priceMonth, 1);
        LocalDate monthEndDate = LocalDate.of(priceYear, priceMonth, 1)
                .with(TemporalAdjusters.lastDayOfMonth());

        Long startPrice = dailyPriceRepository.findEarliestPriceInMonth(productCode, monthStartDate, monthEndDate);
        Long endPrice = dailyPriceRepository.findLatestPriceInMonth(productCode, monthStartDate, monthEndDate);

        // 계산
        Long priceChange = 0L;
        BigDecimal priceChangeRate = new BigDecimal(0.00);

        if (startPrice != null && endPrice != null && startPrice > 0) {
            priceChange = endPrice - startPrice;
            priceChangeRate = BigDecimal.valueOf(priceChange)
                    .divide(BigDecimal.valueOf(startPrice), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        } else {
            log.debug("월간 변화 계산 불가 (데이터 부족): productCode={}, startPrice={}, endPrice={}",
                    productCode, startPrice, endPrice);
        }
        item.setPriceChange(priceChange);
        item.setPriceChangeRate(priceChangeRate);
        return MonthlyPrice.builder()
                .productCode(productCode)
                .priceYear(priceYear)
                .priceMonth(priceMonth)
                .avgPrice(item.getAvgPrice())
                .minPrice(item.getMinPrice())
                .maxPrice(item.getMaxPrice())
                .priceCount(item.getPriceCount())
                .priceChange(priceChange)
                .priceChangeRate(priceChangeRate)
                .build();
    }
}
