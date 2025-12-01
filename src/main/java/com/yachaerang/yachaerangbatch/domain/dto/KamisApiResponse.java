package com.yachaerang.yachaerangbatch.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KamisApiResponse {
    @JsonProperty("condition")
    private List<KamisCondition> condition;

    @JsonProperty("data")
    private KamisData data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KamisData {
        @JsonProperty("error_code")
        private String errorCode;

        @JsonProperty("item")
        private List<KamisPriceItem> items;
    }
}
