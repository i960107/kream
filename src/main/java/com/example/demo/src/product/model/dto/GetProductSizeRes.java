package com.example.demo.src.product.model.dto;

import lombok.Getter;

@Getter
public class GetProductSizeRes {
    private Long productSizeIdx;
    private String size;

    public GetProductSizeRes(Long productSizeIdx, String size) {
        this.productSizeIdx = productSizeIdx;
        this.size = size;
    }
}
