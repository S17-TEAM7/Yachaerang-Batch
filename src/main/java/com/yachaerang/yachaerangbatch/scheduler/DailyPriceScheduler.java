package com.yachaerang.yachaerangbatch.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class DailyPriceScheduler {

    private final JobLauncher jobLauncher;
    private final Job dailyPriceJob;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 매일 12시 정각에 실행
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void runDailyPriceJob() {
        LocalDate targetDate = LocalDate.now().minusDays(1); // 어제 날짜

        try {
            log.info("Daily Price Job 시작: date={}", targetDate);
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("targetDate", targetDate.format(DATE_FORMATTER))
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(dailyPriceJob, jobParameters);
            log.info("Daily Price Job 전체 완료: date={}", targetDate);
        } catch (Exception e) {
            log.error("Daily Price Job 실패: date={}, error={}", targetDate, e.getMessage(), e);
        }
    }
}
