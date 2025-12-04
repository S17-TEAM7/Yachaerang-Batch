package com.yachaerang.yachaerangbatch.domain.entity;

import com.yachaerang.yachaerangbatch.domain.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeeklyPrice extends BaseEntity {

    private long weeklyPriceId;
    private String productCode;

    private Integer priceYear;
    private Integer weekNumber;
    private LocalDate startDate;
    private LocalDate endDate;

    private Long minPrice;
    private Long maxPrice;
    private Double avgPrice;

    private Integer priceCount;
}
