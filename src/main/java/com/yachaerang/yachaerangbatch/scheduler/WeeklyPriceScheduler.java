package com.yachaerang.yachaerangbatch.scheduler;

import com.yachaerang.yachaerangbatch.service.BatchJobService;
import com.yachaerang.yachaerangbatch.util.WeekUtils;
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
        // 오늘이 어떤 년도의 몇주차인지 조회
        int[] todayYearWeek = WeekUtils.getYearAndWeek(LocalDate.now());
        int targetYear;
        int targetWeek;
        // 저번주 조회
        if (todayYearWeek[1] == 1) {
            // 첫 해의 시작일 경우에는 저번주가 년도가 다름
            targetYear = todayYearWeek[0] -1;
            targetWeek = WeekUtils.getLastIsoWeekOfYear(targetYear);
        } else {
            // 아니라면 상관 없음
            targetYear = todayYearWeek[0];
            // 저번주
            targetWeek = todayYearWeek[1] - 1;
        }

        // 저번주에 대하여 조회 시작
        log.info("지난 주({}년도 {}주차) 가격 집계 시작", targetYear, targetWeek);
        batchJobService.runWeeklyAggregation(targetYear, targetWeek);
    }
}
