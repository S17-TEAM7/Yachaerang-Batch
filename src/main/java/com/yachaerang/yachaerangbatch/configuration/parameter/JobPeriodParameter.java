package com.yachaerang.yachaerangbatch.configuration.parameter;

import com.yachaerang.yachaerangbatch.util.WeekUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Getter
@Component
@JobScope
public class JobPeriodParameter {

    private Integer year;
    private Integer month;
    private Integer week;

    // 생성자 주입으로 Parameter 주입
    public JobPeriodParameter(
            @Value("#{jobParameters['year']}") String year
    ) {
        if (year == null || year.isBlank()) {
            int defaultYear = LocalDate.now().getYear();
            this.year = defaultYear;
            log.info("jobParameters['year'] 미입력 → 기본값 사용: {}", this.year);
            return;
        }

        int parsed = parseIntOrThrow("year", year);
        validateYear(parsed);
        this.year = parsed;

        log.info("Target Year 설정: {}", this.year);
    }

    /*
    month 설정하기
     */
    @Value("#{jobParameters['month']}")
    public void setMonth(String month) {
        if (month == null || month.isBlank()) {
            int defaultMonth = LocalDate.now().getMonthValue();
            this.month = defaultMonth;
            log.info("jobParameters['month'] 미입력 → 기본값 사용: {}", this.month);
            return;
        }

        int parsed = parseIntOrThrow("month", month);
        this.month = normalizeMonth(parsed);

        log.info("Target Month 설정: {}", this.month);
    }

    /*
    week 설정하기
     */
    @Value("#{jobParameters['week']}")
    public void setWeek(String week) {
        int targetWeek;

        // 기본값: 현재 날짜의 ISO 주차
        if (week == null || week.isBlank()) {
            // 오늘자의 ISO 기준 year와 weekNumber(defaultYear = 현재)
            int[] targetYearWeek = WeekUtils.getYearAndWeek(LocalDate.now());
            targetWeek = targetYearWeek[1] - 1; // 저번주
            this.week = targetWeek;
            log.info("jobParameters['week'] 미입력 → 지난주 사용: {}", this.week);
            return;
        }

        // 값이 채워져있을 때
        int parsed = parseIntOrThrow("week", week);
        if (parsed < 1 || parsed > 53) {
            throw new IllegalArgumentException(
                    "jobParameters['week']는 1~52 또는 53만 허용합니다. 입력값=" + parsed
            );
        }
        int maxWeek = WeekUtils.getLastIsoWeekOfYear(this.year);
        if (maxWeek < parsed) {
            throw new IllegalArgumentException(String.format("%d년도는 %d주차까지만 존재합니다.", year, maxWeek));
        }
        this.week = parsed;
        log.info("Target ISO Week 설정: {} (year={})", this.week, year);
    }


    private int parseIntOrThrow(String key, String raw) {
        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("jobParameters['" + key + "']는 숫자여야 합니다. 입력값=" + raw, e);
        }
    }

    /*
    Year Validation 확인
    */
    private void validateYear(int year) {
        if (year < 1900 || year > 3000) {
            throw new IllegalArgumentException("year 범위가 올바르지 않습니다: " + year);
        }
    }

    /*
    Month Validation 확인
     */
    private int normalizeMonth(int month) {
        int m = month % 12;
        if (m <= 0) m += 12;
        return m;
    }
}
