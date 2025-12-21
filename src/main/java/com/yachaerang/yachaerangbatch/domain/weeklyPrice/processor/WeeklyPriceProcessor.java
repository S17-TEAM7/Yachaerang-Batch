package com.yachaerang.yachaerangbatch.domain.weeklyPrice.processor;

import com.yachaerang.yachaerangbatch.domain.entity.WeeklyPrice;
import com.yachaerang.yachaerangbatch.repository.DailyPriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
public class WeeklyPriceProcessor implements ItemProcessor<WeeklyPrice, WeeklyPrice> {

    private final DailyPriceRepository dailyPriceRepository;

    @Override
    public WeeklyPrice process(WeeklyPrice item) throws Exception {

        // null 확인
        if (item.getPriceCount() == 0) {
            log.debug("priceCount가 0이므로 스킵: {}", item.getProductCode());
            return null;
        }

        String productCode = item.getProductCode();
        LocalDate startDate = item.getStartDate();
        LocalDate endDate = item.getEndDate();

        // 해당 주차의 시작 가격 조회
        Long startPrice = dailyPriceRepository.findEarliestPriceInWeek(productCode, startDate, endDate);
        // 해당 주차의 종료 가격 조회
        Long endPrice = dailyPriceRepository.findLatestPriceInWeek(productCode, startDate, endDate);

        // 계산
        Long priceChange = 0L;
        BigDecimal priceChangeRate = new BigDecimal(0.00);

        if (startPrice != null && endPrice != null && startPrice > 0) {
            priceChange = endPrice - startPrice;
            priceChangeRate = BigDecimal.valueOf(priceChange)
                    .divide(BigDecimal.valueOf(startPrice), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            log.debug("주간 변화량: {} , 변화율: {}", priceChange, priceChangeRate);
        } else {
            log.debug("주간 변화 계산 불가 (데이터 부족): productCode={}, startPrice={}, endPrice={}",
                    productCode, startPrice, endPrice);
        }
        return WeeklyPrice.builder()
                .productCode(productCode)
                .priceYear(item.getPriceYear())
                .weekNumber(item.getWeekNumber())
                .startDate(startDate)
                .endDate(endDate)
                .avgPrice(item.getAvgPrice())
                .minPrice(item.getMinPrice())
                .maxPrice(item.getMaxPrice())
                .priceCount(item.getPriceCount())
                .priceChange(priceChange)
                .priceChangeRate(priceChangeRate)
                .build();
    }
}
