package com.yachaerang.yachaerangbatch.listener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;

/*
Step Listener (Step마다 실행)
 */
@Slf4j
@Component
public class StepExecutionListener implements org.springframework.batch.core.StepExecutionListener {

    /*
    Step 시작 로그
     */
    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("Step 시작: {}", stepExecution.getStepName());
    }

    /*
    Step 종료 로그
     */
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("Step 완료: {}", stepExecution.getStepName());
        log.info(" - 읽기: {} 건", stepExecution.getReadCount());
        log.info(" - 처리: {} 건", stepExecution.getWriteCount());
        log.info(" - Skip: {} 건", stepExecution.getSkipCount());
        return stepExecution.getExitStatus();
    }
}
