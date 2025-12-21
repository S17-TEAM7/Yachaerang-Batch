package com.yachaerang.yachaerangbatch.domain.entity;

import com.yachaerang.yachaerangbatch.domain.common.BaseEntity;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthlyPrice extends BaseEntity {

    private long monthlyPriceId;
    private String productCode;

    private Integer priceYear;
    private Integer priceMonth;

    private Double avgPrice;
    private Long minPrice;
    private Long maxPrice;

    private Integer priceCount;

    private Long priceChange;
    private BigDecimal priceChangeRate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthlyPrice that = (MonthlyPrice) o;
        return Objects.equals(productCode, that.productCode) &&
                Objects.equals(priceYear, that.priceYear) &&
                Objects.equals(priceMonth, that.priceMonth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCode, priceYear, priceMonth);
    }
}
