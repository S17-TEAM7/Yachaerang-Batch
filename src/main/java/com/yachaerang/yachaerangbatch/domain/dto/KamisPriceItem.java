package com.yachaerang.yachaerangbatch.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KamisPriceItem {

    @JsonProperty("item_name")
    private String itemName;

    @JsonProperty("item_code")
    private String itemCode;

    @JsonProperty("kind_name")
    private String kindName;

    @JsonProperty("kind_code")
    private String kindCode;

    @JsonProperty("rank")
    private String rank;

    @JsonProperty("rank_code")
    private String rankCode;

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("day1")
    private String day1;

    @JsonProperty("dpr1")
    private String dpr1;  // 당일 가격

    @JsonProperty("day2")
    private String day2;

    @JsonProperty("dpr2")
    private String dpr2;  // 1일전 가격

    @JsonProperty("day3")
    private String day3;

    @JsonProperty("dpr3")
    private String dpr3;  // 1주일전 가격

    @JsonProperty("day4")
    private String day4;

    @JsonProperty("dpr4")
    private String dpr4;  // 2주일전 가격

    @JsonProperty("day5")
    private String day5;

    @JsonProperty("dpr5")
    private String dpr5;  // 1개월전 가격

    @JsonProperty("day6")
    private String day6;

    @JsonProperty("dpr6")
    private String dpr6;  // 1년전 가격

    @JsonProperty("day7")
    private String day7;

    @JsonProperty("dpr7")
    private String dpr7;  // 일평년 가격
}
