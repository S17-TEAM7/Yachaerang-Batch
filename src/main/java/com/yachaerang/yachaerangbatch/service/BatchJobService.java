package com.yachaerang.yachaerangbatch.service;

import com.yachaerang.yachaerangbatch.exception.GeneralException;
import com.yachaerang.yachaerangbatch.util.WeekUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class BatchJobService {

    private final JobLauncher jobLauncher;
    private final Job dateRangePriceJob;
    private final Job dailyPriceJob;
    private final Job weeklyPriceJob;
    private final Job monthlyPriceJob;
    private final Job yearlyPriceJob;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 단일 날짜에 대한 조회
     */
    public JobExecution runManually(LocalDate targetDate) {
        try {
            log.info("Daily Price Job 수동 시작: date={}", targetDate);
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("targetDate", targetDate.format(FORMATTER))
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();
            return jobLauncher.run(dailyPriceJob, jobParameters);
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
     * 특정 월의 일자별 데이터 전체 수집
     */
    public JobExecution collectMonth(Integer year, Integer month) throws JobExecutionException {

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        log.info("일자 데이터 월 단위 배치 실행: {}-{}", year, month);

        return collectDateRange(startDate, endDate);
    }

    /**
     * 전월 일간 데이터 수집
     */
    public JobExecution collectPreviousMonth() throws JobExecutionException {
        YearMonth previousMonth = YearMonth.now().minusMonths(1);
        log.info("이전의 달 배치 실행: {}-{}", previousMonth.getYear(), previousMonth.getMonthValue());
        return collectMonth(previousMonth.getYear(), previousMonth.getMonthValue());
    }

    /**
     * 특정 기간의 주간 가격 집계 실행
     */
    public JobExecution runWeeklyAggregation(Integer year, Integer week) {

        // 만약 year와 week가 안맞으면 예외
        if (week > WeekUtils.getLastIsoWeekOfYear(year)) {
            throw new IllegalArgumentException("week가 해당 년도의 주차에 존재하지 않는 주차입니다.");
        }

        LocalDate startDate = WeekUtils.getWeekStartDate(year, week);
        LocalDate endDate = WeekUtils.getWeekEndDate(year, week);
        // 날짜 검증
        LocalDate yesterday = LocalDate.now().minusDays(1);
        if (!endDate.isBefore(yesterday)) {
            throw new IllegalArgumentException(
                    String.format("아직 완료되지 않은 주입니다.")
            );
        }

        log.info("주간 집계 실행 - 입력: {}년 {}주차, 기간: {} ~ {}", year, week, startDate, endDate);
        try {
            JobParameters params = new JobParametersBuilder()
                    .addString("year", year.toString())
                    .addString("week", week.toString())
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            return jobLauncher.run(weeklyPriceJob, params);
        } catch (Exception e) {
            log.error("주간 가격 집계 Job 실행 실패", e);
            throw new RuntimeException("Job 실행 실패", e);
        }
    }

    /**
     * 특정 기간의 월간 가격 집계 실행
     */
    public JobExecution runMonthlyAggregation(Integer year, Integer month) throws JobExecutionException {
        // 날짜 검증
        if (year == null || month == null) {
            throw new GeneralException("year와 month는 필수입니다.");
        }
        if (month < 1 || month >12) {
            throw new GeneralException("month는 1~12 사이여야합니다.");
        }
        YearMonth targetMonth = YearMonth.of(year, month);
        YearMonth currentMonth = YearMonth.now();
        if (!targetMonth.isBefore(currentMonth)) {
            throw new GeneralException(
                    String.format("아직 완료되지 않은 달입니다. 대상: %d년 %d월", year, month)
            );
        }

        log.info("월간 집계 실행 - 대상: {}년 {}월", year, month);

        try {
            JobParameters params = new JobParametersBuilder()
                    .addString("year", year.toString())
                    .addString("month", month.toString())
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            return jobLauncher.run(monthlyPriceJob, params);
        } catch (JobExecutionAlreadyRunningException e) {
            log.warn("이미 실행 중인 Job입니다.");
            throw new GeneralException("이미 실행 중인 Job입니다.", e);
        } catch (Exception e) {
            log.error("월간 가격 집계 Job 실행 실패", e);
            throw new GeneralException("Job 실행 실패: " + e.getMessage(), e);
        }
    }

    /**
     * 특정 연도의 연간 가격 집계 실행
     */
    public JobExecution runYearlyAggregation(Integer year) {
        // 기본 유효성 검증
        if (year == null) {
            throw new IllegalArgumentException("year는 필수입니다.");
        }

        // 미완료 연도 검증
        int currentYear = LocalDate.now().getYear();
        if (year >= currentYear) {
            throw new IllegalArgumentException(
                    String.format("아직 완료되지 않은 연도입니다. 대상: %d년", year)
            );
        }

        log.info("연간 집계 실행 - 대상: {}년", year);

        try {
            JobParameters params = new JobParametersBuilder()
                    .addString("year", year.toString())
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            return jobLauncher.run(yearlyPriceJob, params);
        } catch (JobExecutionAlreadyRunningException e) {
            log.warn("이미 실행 중인 Job입니다.");
            throw new RuntimeException("이미 실행 중인 Job입니다.", e);
        } catch (Exception e) {
            log.error("연간 가격 집계 Job 실행 실패", e);
            throw new RuntimeException("Job 실행 실패: " + e.getMessage(), e);
        }
    }
}
