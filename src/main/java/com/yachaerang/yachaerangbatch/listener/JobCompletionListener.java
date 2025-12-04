package com.yachaerang.yachaerangbatch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

/*
Job에 대한 Listener 구현
 */
@Slf4j
@Component
public class JobCompletionListener implements JobExecutionListener {

    /*
    시작 이전의 Listener
     */
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("========================================");
        log.info("Job 시작: {}", jobExecution.getJobInstance().getJobName());
        log.info("Job Parameters: {}", jobExecution.getJobParameters());
        log.info("시작 시간: {}", jobExecution.getStartTime());
        log.info("========================================");
    }

    /*
    종료 이후의 Listener
     */
    @Override
    public void afterJob(JobExecution jobExecution) {
        LocalDateTime startTime = jobExecution.getStartTime();
        LocalDateTime endTime = jobExecution.getEndTime();

        Duration duration = Duration.between(startTime, endTime);

        log.info("========================================");
        log.info("Job 완료: {}", jobExecution.getJobInstance().getJobName());
        log.info("상태: {}", jobExecution.getStatus());
        log.info("종료 시간: {}", endTime);
        log.info("소요 시간: {}초", duration.getSeconds());

        if (jobExecution.getStatus() == BatchStatus.FAILED) {
            log.error("Job 실패 - 예외 정보:");
            jobExecution.getAllFailureExceptions()
                    .forEach(e -> log.error("Exception: ", e));
        }
        log.info("========================================");
    }
}
