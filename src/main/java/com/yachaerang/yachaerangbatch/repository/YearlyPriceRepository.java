package com.yachaerang.yachaerangbatch.repository;

import com.yachaerang.yachaerangbatch.domain.entity.YearlyPrice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface YearlyPriceRepository {

    /*
    일별 데이터를 연간으로 집계하여 조회
     */
    List<YearlyPrice> selectYearlyAggregatedPrices(@Param("targetYear") Integer targetYear);

    /**
     * 연초 가격 조회
     */
    Long selectStartPrice(
            @Param("productCode") String productCode,
            @Param("targetYear") Integer targetYear
    );

    /**
     * 연말 가격 조회
     */
    Long selectEndPrice(
            @Param("productCode") String productCode,
            @Param("targetYear") Integer targetYear
    );

    /*
    연간 가격 데이터 저장
     */
    int upsertYearlyPrice(YearlyPrice yearlyPrice);
}
