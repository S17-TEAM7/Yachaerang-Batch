package com.yachaerang.yachaerangbatch.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    private Long productId;

    private String productName;
    private String productCode;

    private String itemName;
    private String itemCode;

    private String kindName;
    private String kindCode;

    private String productRank;
    private String unit;
    private String origin;
    private String imageUrl;
}
