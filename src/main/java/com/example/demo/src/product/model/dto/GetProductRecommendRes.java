package com.example.demo.src.product.model.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class GetProductRecommendRes {
    private String categoryName;
    private String description;
    private byte position;
    private List<GetProductRecommendResProduct> productList;

    public GetProductRecommendRes(String categoryName, String description, byte position, List<GetProductRecommendResProduct> productList) {
        this.categoryName = categoryName;
        this.description = description;
        this.position = position;
        this.productList = productList;
    }
}
