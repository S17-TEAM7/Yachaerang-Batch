package com.yachaerang.yachaerangbatch.configuration.parameter;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Getter
public class DailyJobParameter {

    private LocalDate targetDate;
    private String categoryCode;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /*
    targetDate 설정하기
     */
    @Value("#{jobParameters['targetDate']}")
    public void setTargetDate(String targetDate) {
        if (targetDate != null && !targetDate.isBlank()) {
            this.targetDate = LocalDate.parse(targetDate, FORMATTER);
        } else {
            // 오늘 null -> 어제 찾기
            this.targetDate = LocalDate.now().minusDays(1);
        }
        log.info("Target Date 설정: {}", this.targetDate);
    }

    /*
    categoryCode 설정하기(기본은 채소류 200)
     */
    @Value("#{jobParameters['categoryCode']}")
    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode != null ? categoryCode : "200";
        log.info("Category Code 설정: {}", this.categoryCode);
    }
}
