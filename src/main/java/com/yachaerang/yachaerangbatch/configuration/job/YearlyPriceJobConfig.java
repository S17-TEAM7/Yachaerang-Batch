package com.yachaerang.yachaerangbatch.configuration.job;

import com.yachaerang.yachaerangbatch.configuration.parameter.MonthlyJobParameter;
import com.yachaerang.yachaerangbatch.domain.entity.YearlyPrice;
import com.yachaerang.yachaerangbatch.listener.JobCompletionListener;
import com.yachaerang.yachaerangbatch.listener.StepExecutionListener;
import com.yachaerang.yachaerangbatch.service.YearlyPriceAggregationService;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class YearlyPriceJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final JobCompletionListener jobCompletionListener;
    private final StepExecutionListener stepExecutionListener;

    private final YearlyPriceAggregationService yearlyPriceAggregationService;

    private static final int CHUNK_SIZE = 100;

    @Bean
    public Job yearlyPriceJob(Step yearlyPriceStep) {
        return new JobBuilder("yearlyPriceJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobCompletionListener)
                .start(yearlyPriceStep)
                .build();
    }

    /*
    YearlyPrice Step
     */
    @Bean
    public Step yearlyPriceStep(
            ListItemReader<YearlyPrice> yearlyPriceReader,
            ItemProcessor<YearlyPrice, YearlyPrice> yearlyPriceProcessor,
            ItemWriter<YearlyPrice> yearlyPriceWriter
    ) {
        return new StepBuilder("yearlyPriceStep", jobRepository)
                .<YearlyPrice, YearlyPrice>chunk(CHUNK_SIZE, platformTransactionManager)
                .listener(stepExecutionListener)
                .reader(yearlyPriceReader)
                .processor(yearlyPriceProcessor)
                .writer(yearlyPriceWriter)
                .faultTolerant()
                .retryLimit(3)
                .retry(Exception.class)
                .build();
    }

    /**
     * Reader 스텝
     * @param year : 대상 년도
     * @return
     */
    @Bean
    @StepScope
    public ListItemReader<YearlyPrice> yearlyPriceReader(
            @Value("#{jobParameters['year']}") String year
    ) {
        int y = Integer.parseInt(year);

        List<YearlyPrice> yearlyPriceList =
                yearlyPriceAggregationService.getYearlyAggregatedPrices(y);

        log.info("ListItemReader 초기화 - 최종 리스트 크기: {}", yearlyPriceList.size());
        return new ListItemReader<>(yearlyPriceList);
    }

    /**
     * Processor Step : start_price, end_price 설정해 저장
     * @return
     */
    @Bean
    @StepScope
    public ItemProcessor<YearlyPrice, YearlyPrice> yearlyPriceProcessor(MonthlyJobParameter monthlyJobParameter) {
        int year = monthlyJobParameter.getYear();

        return item -> {
            if (item.getPriceCount() == 0) {
                log.debug("priceCount가 0이므로 스킵: {}", item.getProductCode());
                return null;
            }

            // 연초와 연말 가격 설정
            item.setStartPrice(yearlyPriceAggregationService.getStartPrice(item.getProductCode(), year));
            item.setEndPrice(yearlyPriceAggregationService.getEndPrice(item.getProductCode(), year));
            return item;
        };
    }

    /**
     * Writer 스텝
     *
     * @return
     */
    @Bean
    public ItemWriter<YearlyPrice> yearlyPriceWriter() {
        return chunk -> {
            List<YearlyPrice> items = new ArrayList<>(chunk.getItems());
            log.info("저장할 데이터 : {} 건", items.size());
            yearlyPriceAggregationService.saveYearlyPrices(items);
        };
    }
}