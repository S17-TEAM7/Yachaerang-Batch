package com.yachaerang.yachaerangbatch.controller;

import com.yachaerang.yachaerangbatch.scheduler.DailyPriceScheduler;
import com.yachaerang.yachaerangbatch.service.BatchJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/batch")
public class BatchJobController {

    private final BatchJobService batchJobService;
    private final DailyPriceScheduler dailyPriceScheduler;

    /**
     * 수동으로 일별 가격 수집 Job 실행
     */
    @GetMapping("/daily")
    public ResponseEntity<Map<String, String>> runDailyJob(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate targetDate) {

        batchJobService.runManually(targetDate);

        return ResponseEntity.ok(Map.of(
                "status", "Job 실행 완료",
                "targetDate", targetDate.toString()
        ));
    }

    /**
     * 기간 범위 데이터 수집
     */
    @PostMapping("/date-range")
    public ResponseEntity<Map<String, Object>> collectDateRange(
            @RequestParam(name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        Map<String, Object> response = new HashMap<>();
        try {
            JobExecution execution = batchJobService.collectDateRange(startDate, endDate);
            response.put("success", true);
            response.put("jobId", execution.getJobId());
            response.put("status", execution.getStatus().toString());
            response.put("startDate", startDate.toString());
            response.put("endDate", endDate.toString());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 특정 월 데이터 수집
     */
    @PostMapping("/month")
    public ResponseEntity<Map<String, Object>> collectMonth(
            @RequestParam(name = "year") int year, @RequestParam(name = "month") int month) {
        Map<String, Object> response = new HashMap<>();

        try {
            JobExecution execution = batchJobService.collectMonth(year, month);
            response.put("success", true);
            response.put("jobId", execution.getJobId());
            response.put("status", execution.getStatus().toString());
            response.put("year", year);
            response.put("month", month);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 이전 달의 데이터 수집
     */
    @PostMapping("/previous-month")
    public ResponseEntity<Map<String, Object>> collectPreviousMonth() {

        Map<String, Object> response = new HashMap<>();
        try {
            JobExecution execution = batchJobService.collectPreviousMonth();
            response.put("success", true);
            response.put("jobId", execution.getJobId());
            response.put("status", execution.getStatus().toString());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
