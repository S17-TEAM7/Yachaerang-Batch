package com.yachaerang.yachaerangbatch.domain.entity;

import com.yachaerang.yachaerangbatch.domain.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YearlyPrice extends BaseEntity {

    private Long yearlyPriceId;
    private String productCode;

    private Integer priceYear;

    private Double avgPrice;
    private Long minPrice;
    private Long maxPrice;
    private Long startPrice;
    private Long endPrice;

    private Integer priceCount;

    private Long priceChange;
    private BigDecimal priceChangeRate;
}
