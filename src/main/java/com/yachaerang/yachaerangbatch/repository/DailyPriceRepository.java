package com.yachaerang.yachaerangbatch.repository;

import com.yachaerang.yachaerangbatch.domain.entity.DailyPrice;
import com.yachaerang.yachaerangbatch.domain.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DailyPriceRepository {

    /*
    저장하기
     */
    int save(DailyPrice dailyPrice);

    /*
    전부 저장하기
     */
    int saveAll(List<DailyPrice> dailyPrices);
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
            @Param("productCode") String productCode,
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
    String selectProductCodeByItemNameAndKind(
            @Param("itemName") String itemName,
            @Param("kindName") String kindName
    );

    /*
    시작과 종료일을 지정하여 존재하는 날짜를 확인
     */
    List<LocalDate> findExistingDatesByProductAndDateRange(
            @Param("productCode") String productCode,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /*
    해당 상품의
     */
    boolean existsByProductAndPriceDate(
            @Param("product")Product product,
            @Param("priceDate") LocalDate priceDate
            );
}
