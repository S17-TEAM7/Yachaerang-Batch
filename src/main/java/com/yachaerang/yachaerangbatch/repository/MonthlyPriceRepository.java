package com.yachaerang.yachaerangbatch.repository;

import com.yachaerang.yachaerangbatch.domain.entity.MonthlyPrice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface MonthlyPriceRepository {

    /**
     * 시작일~종료일을 기준으로 한달 집계에 대하여 조회
     * @param startDate : 시작일(서비스 코드에서 한달의 시작으로 조정)
     * @param endDate : 종료일(서비스 코드에서 한달의 종료일로 조정)
     * @return
     */
    List<MonthlyPrice> selectMonthlyAggregatedPrices(
            @Param("startDate")LocalDate startDate,
            @Param("endDate")LocalDate endDate
            );

    /*
    한달에 대한 가격 저장
     */
    void upsertMonthlyPrice(MonthlyPrice monthlyPrice);

    /*
    배치 저장
     */
    void batchUpsertMonthlyPrice(List<MonthlyPrice> monthlyPrices);
}
