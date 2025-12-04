package com.yachaerang.yachaerangbatch.repository;

import com.yachaerang.yachaerangbatch.domain.entity.WeeklyPrice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface WeeklyPriceRepository {

    /*
    특정 일자에 대한 가격을 주간 데이터로 집계하기
     */
    List<WeeklyPrice> selectWeeklyAggregatedPrices(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /*
    주간 가격 데이터 저장
     */
    void upsertWeeklyPrice(WeeklyPrice weeklyPrice);

    /*
    배치 저장
     */
    void batchUpsertWeeklyPrice(List<WeeklyPrice> weeklyPrices);
}
