package com.yachaerang.yachaerangbatch.repository;

import com.yachaerang.yachaerangbatch.domain.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductRepository {

    /*
    저장
     */
    int save(Product product);

    /*
    각각의 조건을 기반으로 조회
     */
    Product findByItemCodeAndKindCodeAndRank(
            @Param("itemCode") String itemCode,
            @Param("kindCode") String kindCode,
            @Param("rank") String rank
    );

    /*
    OPEN API로부터 얻어온 유효한 정보 전부 조회
     */
    List<Product> findAllActiveProducts();

    /*
    고유한 아이템 코드들 조회
     */
    List<String> findDistinctItemCodes();

    /*
    고유한 아이템 이름들만 조회
     */
    List<String> findDistinctItemNames();

    /*
    productCode를 기반으로 작업
     */
    Product findByProductCode(@Param("productCode") String productCode);
}
