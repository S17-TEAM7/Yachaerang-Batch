package com.yachaerang.yachaerangbatch.configuration.job;

import com.yachaerang.yachaerangbatch.domain.entity.WeeklyPrice;
import com.yachaerang.yachaerangbatch.listener.JobCompletionListener;
import com.yachaerang.yachaerangbatch.listener.StepExecutionListener;
import com.yachaerang.yachaerangbatch.service.WeeklyPriceAggregationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WeeklyPriceJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final JobCompletionListener jobCompletionListener;
    private final StepExecutionListener stepExecutionListener;

    private final WeeklyPriceAggregationService weeklyPriceAggregationService;

    private static final int CHUNK_SIZE= 100;

    @Bean
    public Job weeklyPriceJob() {
        return new JobBuilder("weeklyPriceJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobCompletionListener)
                .start(weeklyPriceStep())
                .build();
    }

    /*
    WeeklyPrice Step
     */
    @Bean
    public Step weeklyPriceStep() {
        return new StepBuilder("weeklyPriceStep", jobRepository)
                .<WeeklyPrice, WeeklyPrice>chunk(CHUNK_SIZE, platformTransactionManager)
                .listener(stepExecutionListener)
                .reader(weeklyPriceReader(null, null))
                .processor(weeklyPriceProcessor())
                .writer(weeklyPriceWriter())
                .faultTolerant()
                .retryLimit(3)
                .retry(Exception.class)
                .build();
    }

    /**
     * Reader 스텝
     * @param startDate : 시작 날짜
     * @param endDate : 종료 날짜
     * @return
     */
    @Bean
    @StepScope
    public ListItemReader<WeeklyPrice> weeklyPriceReader(
            @Value("#{jobParameters['startDate']}") String startDate,
            @Value("#{jobParameters['endDate']}") String endDate) {

        List<WeeklyPrice> weeklyPriceList =
                weeklyPriceAggregationService.getWeeklyAggregatedPrices(
                        LocalDate.parse(startDate), LocalDate.parse(endDate)
                );

        log.info("ListItemReader 초기화 - 최종 리스트 크기: {}", weeklyPriceList.size());

        return new ListItemReader<>(weeklyPriceList);
    }

    /**
     * Processor 스텝
     * 데이터 검증 및 가공
     * @return
     */
    @Bean
    public ItemProcessor<WeeklyPrice, WeeklyPrice> weeklyPriceProcessor() {
        return item -> {
            if (item.getPriceCount() == 0) {
                log.debug("priceCount가 0이므로 스킵: {}", item.getProductCode());
                return null;
            }
            return item;
        };
    }

    /**
     * Writer 스텝
     * 집계 서비스를 통해 대신 MyBatis 실행
     * @return
     */
    @Bean
    public ItemWriter<WeeklyPrice> weeklyPriceWriter() {
        return chunk -> {
            List<WeeklyPrice> items = new ArrayList<>(chunk.getItems());
            log.info("저장할 데이터 : {} 건", items.size());
            weeklyPriceAggregationService.saveWeeklyPrices(items);
        };
    }
}
