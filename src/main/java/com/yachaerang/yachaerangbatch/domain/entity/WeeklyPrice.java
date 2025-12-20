package com.yachaerang.yachaerangbatch.domain.entity;

import com.yachaerang.yachaerangbatch.domain.common.BaseEntity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
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

    private Long priceChange;
    private BigDecimal priceChangeRate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeeklyPrice that = (WeeklyPrice) o;
        return Objects.equals(productCode, that.productCode) &&
                Objects.equals(priceYear, that.priceYear) &&
                Objects.equals(weekNumber, that.weekNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCode, priceYear, weekNumber);
    }
}
