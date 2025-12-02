package com.yachaerang.yachaerangbatch.controller;

import com.yachaerang.yachaerangbatch.scheduler.DailyPriceScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/batch")
public class BatchController {

    private final DailyPriceScheduler dailyPriceScheduler;

    /**
     * 수동으로 일별 가격 수집 Job 실행
     */
    @GetMapping("/daily")
    public ResponseEntity<Map<String, String>> runDailyJob(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate targetDate,
            @RequestParam(defaultValue = "200") String categoryCode) {

        dailyPriceScheduler.runManually(targetDate, categoryCode);

        return ResponseEntity.ok(Map.of(
                "status", "Job 실행 완료",
                "targetDate", targetDate.toString(),
                "categoryCode", categoryCode
        ));
    }
}
