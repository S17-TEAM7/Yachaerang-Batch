package com.yachaerang.yachaerangbatch.configuration.job;

import com.yachaerang.yachaerangbatch.configuration.parameter.MonthlyJobParameter;
import com.yachaerang.yachaerangbatch.domain.entity.MonthlyPrice;
import com.yachaerang.yachaerangbatch.listener.JobCompletionListener;
import com.yachaerang.yachaerangbatch.listener.StepExecutionListener;
import com.yachaerang.yachaerangbatch.service.MonthlyPriceAggregationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MonthlyPriceJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final JobCompletionListener jobCompletionListener;
    private final StepExecutionListener stepExecutionListener;

    private final MonthlyPriceAggregationService monthlyPriceAggregationService;


    private static final int CHUNK_SIZE= 100;

    @Bean
    public Job monthlyPriceJob(Step monthlyPriceStep) {
        return new JobBuilder("monthlyPriceJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobCompletionListener)
                .start(monthlyPriceStep)
                .build();
    }

    /*
    MonthlyPrice Step
     */
    @Bean
    public Step monthlyPriceStep(
            ListItemReader<MonthlyPrice> monthlyPriceReader,
            ItemProcessor<MonthlyPrice, MonthlyPrice> monthlyPriceProcessor,
            ItemWriter<MonthlyPrice> monthlyPriceWriter
    ) {
        return new StepBuilder("monthlyPriceStep", jobRepository)
                .<MonthlyPrice, MonthlyPrice>chunk(CHUNK_SIZE, platformTransactionManager)
                .listener(stepExecutionListener)
                .reader(monthlyPriceReader)
                .processor(monthlyPriceProcessor)
                .writer(monthlyPriceWriter)
                .faultTolerant()
                .retryLimit(3)
                .retry(Exception.class)
                .build();
    }

    /**
     * Reader 스텝
     * @param monthlyJobParameter: 연도와 월
     * @return
     */
    @Bean
    @StepScope
    public ListItemReader<MonthlyPrice> monthlyPriceReader(MonthlyJobParameter monthlyJobParameter) {
        int year = monthlyJobParameter.getYear();
        int month = monthlyJobParameter.getMonth();

        List<MonthlyPrice> monthlyPriceList =
                monthlyPriceAggregationService.getMonthlyAggregatedPrices(year, month);

        log.info("ListItemReader 초기화 - 최종 리스트 크기: {}", monthlyPriceList.size());
        return new ListItemReader<>(monthlyPriceList);
    }

    /**
     * Processor Step
     * @return
     */
    @Bean
    public ItemProcessor<MonthlyPrice, MonthlyPrice> monthlyPriceProcessor() {
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
    public ItemWriter<MonthlyPrice> monthlyPriceWriter() {
        return chunk -> {
            List<MonthlyPrice> items = new ArrayList<>(chunk.getItems());
            log.info("저장할 데이터 : {} 건", items.size());
            monthlyPriceAggregationService.saveMonthlyPrices(items);
        };
    }
}
