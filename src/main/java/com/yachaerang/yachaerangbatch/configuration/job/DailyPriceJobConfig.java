package com.yachaerang.yachaerangbatch.configuration.job;

import com.yachaerang.yachaerangbatch.configuration.parameter.DailyJobParameter;
import com.yachaerang.yachaerangbatch.domain.dailyPrice.processor.DailyPriceProcessor;
import com.yachaerang.yachaerangbatch.domain.dailyPrice.reader.DailyPriceReader;
import com.yachaerang.yachaerangbatch.domain.dailyPrice.writer.DailyPriceWriter;
import com.yachaerang.yachaerangbatch.domain.dto.KamisPriceItem;
import com.yachaerang.yachaerangbatch.domain.entity.DailyPrice;
import com.yachaerang.yachaerangbatch.listener.JobCompletionListener;
import com.yachaerang.yachaerangbatch.listener.StepExecutionListener;
import com.yachaerang.yachaerangbatch.repository.DailyPriceRepository;
import com.yachaerang.yachaerangbatch.repository.ProductRepository;
import com.yachaerang.yachaerangbatch.service.KamisApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/*
Daily Price Job에 대한 설정
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class DailyPriceJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final JobCompletionListener jobCompletionListener;
    private final StepExecutionListener stepExecutionListener;

    private final KamisApiService kamisApiService;
    private final ProductRepository productRepository;
    private final DailyPriceRepository dailyPriceRepository;

    private static final int CHUNK_SIZE = 100;
    private static final List<String> CATEGORY_CODES = List.of("100", "200", "300", "400", "500", "600");

    /**
     * Job: 카테고리 100, 200, 300, 400, 500, 600 순차 실행(categoryCode)
     */
    @Bean
    public Job dailyPriceJob() {
        return new JobBuilder("dailyPriceJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobCompletionListener)
                .start(dailyPriceStep())
                .build();
    }

    /**
     * 카테고리별 Step 생성(Job의 Step)
     */
    private Step dailyPriceStep() {
        return new StepBuilder("dailyPriceStep", jobRepository)
                .listener(stepExecutionListener)
                .<KamisPriceItem, DailyPrice>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(dailyPriceReader(null))
                .processor(dailyPriceProcessor(null))
                .writer(dailyPriceWriter())
                .faultTolerant()
                .skipLimit(10)
                .skip(Exception.class)
                .build();
    }

    /**
     * Reader 스텝
     * @param dailyJobParameter : jobParameters에서 얻어오기
     * @return
     */
    @Bean
    @StepScope
    public DailyPriceReader dailyPriceReader(DailyJobParameter dailyJobParameter) {

        LocalDate targetDate = dailyJobParameter.getTargetDate();

        log.info("Reader 생성: targetDate={}", targetDate);

        return new DailyPriceReader(kamisApiService, targetDate, CATEGORY_CODES);
    }

    /**
     * Processor Step
     * @param dailyJobParameter : jobParameter로부터 얻음 (category는 reader에서 필터링)
     * @return
     */
    @Bean
    @StepScope
    public DailyPriceProcessor dailyPriceProcessor(DailyJobParameter dailyJobParameter) {

        LocalDate targetDate = dailyJobParameter.getTargetDate();

        return new DailyPriceProcessor(
                productRepository,
                dailyPriceRepository,
                targetDate
        );
    }

    /*
    Writer Step 등록
     */
    @Bean
    public DailyPriceWriter dailyPriceWriter() {
        return new DailyPriceWriter(dailyPriceRepository);
    }

    /*
    형식 맞춰서 찾기
     */
    private LocalDate parseTargetDate(String targetDateStr) {
        if (targetDateStr == null || targetDateStr.isBlank()) {
            return LocalDate.now().minusDays(1);
        }
        return LocalDate.parse(targetDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
