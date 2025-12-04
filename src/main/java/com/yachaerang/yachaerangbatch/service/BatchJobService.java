package com.yachaerang.yachaerangbatch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

@Service
@Slf4j
@RequiredArgsConstructor
public class BatchJobService {

    private final JobLauncher jobLauncher;
    private final Job dateRangePriceJob;
    private final Job dailyPriceJob;
    private final Job weeklyPriceJob;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 단일 날짜에 대한 조회
     */
    public void runManually(LocalDate targetDate) {
        try {
            log.info("Daily Price Job 수동 시작: date={}", targetDate);
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("targetDate", targetDate.format(FORMATTER))
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(dailyPriceJob, jobParameters);
            log.info("Daily Price Job 수동 실행 전체 완료: date={}", targetDate);
        } catch (Exception e) {
            log.error("수동 실행 실패", e);
            throw new RuntimeException("Job 실행 실패", e);
        }
    }

    /**
     * 특정 기간의 데이터 수집
     */
    public JobExecution collectDateRange(LocalDate startDate, LocalDate endDate)
            throws JobExecutionException {

        log.info("기간 범위 배치 실행: {} ~ {}", startDate, endDate);

        JobParameters params = new JobParametersBuilder()
                .addString("startDate", startDate.format(FORMATTER))
                .addString("endDate", endDate.format(FORMATTER))
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();

        return jobLauncher.run(dateRangePriceJob, params);
    }

    /**
     * 특정 월의 전체 데이터 수집
     */
    public JobExecution collectMonth(int year, int month) throws JobExecutionException {

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        log.info("월간 배치 실행: {}-{}", year, month);

        return collectDateRange(startDate, endDate);
    }

    /**
     * 전월 데이터 수집
     */
    public JobExecution collectPreviousMonth() throws JobExecutionException {
        YearMonth previousMonth = YearMonth.now().minusMonths(1);
        log.info("이전의 달 배치 실행: {}-{}", previousMonth.getYear(), previousMonth.getMonthValue());
        return collectMonth(previousMonth.getYear(), previousMonth.getMonthValue());
    }

    /**
     * 특정 기간의 주간 가격 집계 실행
     */
    public void runWeeklyAggregation(LocalDate targetDate) {
        LocalDate startDate = targetDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endDate = startDate.plusDays(6);
        // 날짜 검증
        LocalDate yesterday = LocalDate.now().minusDays(1);
        if (!endDate.isBefore(yesterday)) {
            throw new IllegalArgumentException(
                    String.format("아직 완료되지 않은 주입니다.")
            );
        }

        log.info("주간 집계 실행 - 입력: {}, 기간: {} ~ {}", targetDate, startDate, endDate);
        try {
            JobParameters params = new JobParametersBuilder()
                    .addString("startDate", startDate.toString())
                    .addString("endDate", endDate.toString())
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(weeklyPriceJob, params);
            log.info("Job 실행 결과: {}", execution.getStatus());

        } catch (Exception e) {
            log.error("주간 가격 집계 Job 실행 실패", e);
            throw new RuntimeException("Job 실행 실패", e);
        }
    }
}
