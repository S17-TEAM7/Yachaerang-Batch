package com.yachaerang.yachaerangbatch.domain.dailyPrice.reader;

import com.yachaerang.yachaerangbatch.domain.dto.KamisPriceItem;
import com.yachaerang.yachaerangbatch.service.KamisApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

/*
Daily Price Job의 Reader
 */
@Slf4j
public class DailyPriceReader implements ItemReader<KamisPriceItem> {

    private final KamisApiService kamisApiService;
    private final LocalDate targetDate;
    private final String categoryCode;

    public DailyPriceReader(KamisApiService kamisApiService, LocalDate targetDate, String categoryCode) {
        this.kamisApiService = kamisApiService;
        this.targetDate = targetDate;
        this.categoryCode = categoryCode;
    }

    private Iterator<KamisPriceItem> itemIterator;
    private boolean initialized = false;

    /*
    API 요청에 대하여 읽기 -> DTO 반환
     */
    @Override
    public KamisPriceItem read() {
        if (!initialized) {
            initialize();
        }

        if (itemIterator != null && itemIterator.hasNext()) {
            return itemIterator.next();
        }
        // 모든 Iterator 완료 -> null
        return null;
    }

    /*
    Initialize
     */
    private void initialize() {
        log.info("API 데이터 로딩 시작: date={}, category={}", targetDate, categoryCode);

        List<KamisPriceItem> items = kamisApiService.getDailyPrices(targetDate, categoryCode);
        this.itemIterator = items.iterator();
        this.initialized = true;

        log.info("API 데이터 로딩 완료: {} 건", items.size());
    }
}
