package com.yachaerang.yachaerangbatch.domain.entity;

import com.yachaerang.yachaerangbatch.domain.common.BaseEntity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DailyPrice extends BaseEntity {

    private long dailyPriceId;
    private String productCode;

    private LocalDate priceDate;
    private long price;

    private Long priceChange;
    private BigDecimal priceChangeRate;
}
