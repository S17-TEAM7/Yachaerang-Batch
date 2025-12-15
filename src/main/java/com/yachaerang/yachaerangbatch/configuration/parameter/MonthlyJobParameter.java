package com.yachaerang.yachaerangbatch.configuration.parameter;

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
public class MonthlyJobParameter {

    private Integer year;
    private Integer month;

    /*
    year 설정
     */
    @Value("#{jobParameters['year']}")
    public void setYear(String year) {
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
