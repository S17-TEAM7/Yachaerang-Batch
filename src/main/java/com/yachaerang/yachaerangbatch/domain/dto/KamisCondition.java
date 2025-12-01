package com.yachaerang.yachaerangbatch.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
KAMIS Open API 응답의 Condition
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KamisCondition {

    @JsonProperty("p_product_cls_code")
    private String productClsCode;

    @JsonProperty("p_country_code")
    private String countryCode;

    @JsonProperty("p_regday")
    private String regday;

    @JsonProperty("p_convert_kg_yn")
    private String convertKgYn;

    @JsonProperty("p_category_code")
    private String categoryCode;
}
