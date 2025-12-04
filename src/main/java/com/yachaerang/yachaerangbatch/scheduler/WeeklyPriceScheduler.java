package com.yachaerang.yachaerangbatch.scheduler;

import com.yachaerang.yachaerangbatch.service.BatchJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Component
@Slf4j
@RequiredArgsConstructor
public class WeeklyPriceScheduler {

    private final BatchJobService batchJobService;

    /**
     * 지난 주 데이터 집계
     */
    @Scheduled(cron = "0 0 1 ? * MON")  // 매주 월요일 새벽 1시
    public void runLastWeekAggregation() {
        LocalDate today = LocalDate.now();
        LocalDate lastWeekStart = today.minusWeeks(1).with(DayOfWeek.MONDAY);
        LocalDate lastWeekEnd = lastWeekStart.plusDays(6);

        log.info("지난 주({} ~ {}) 가격 집계 시작", lastWeekStart, lastWeekEnd);
        batchJobService.runWeeklyAggregation(lastWeekStart);
    }
}
