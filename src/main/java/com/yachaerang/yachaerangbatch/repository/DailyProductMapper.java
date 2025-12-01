package com.yachaerang.yachaerangbatch.repository;

import com.yachaerang.yachaerangbatch.domain.entity.DailyPrice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DailyProductMapper {

    /*
    DailyPrice 저장하기
     */
    int insertFromDailyProduct(
            @Param("productId") Long productId,
            @Param("priceDate")LocalDate priceDate,
            @Param("price") Long price
            );

    /*
    DailyPrice 배치 단위로 저장하기
     */
    void insertBatchFromDailyProduct(
            @Param("productId") Long productId,
            @Param("priceList")List<DailyPrice> priceList
            );

    /*
    해당 날짜에 해당 상품이 저장된 것이 있는지 확인
     */
    int countByProductIdAndPriceDate(
            @Param("productId") Long productId,
            @Param("priceDate") LocalDate priceDate
    );

    /*
    itemName과 kindName을 기반으로 해당 상품 조회
     */
    Long selectProductIdByItemNameAndKind(
            @Param("itemName") String itemName,
            @Param("kindName") String kindName
    );
}
