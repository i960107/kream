package com.example.demo.src.product.model.dto;

import lombok.Getter;

@Getter
public class GetProductRecommendResProduct {
    private Long productIdx;
    private String productImage;
    private String brandName;
    private String brandLogo;
    private String productName;
    private Long buyPrice;

    public GetProductRecommendResProduct(Long productIdx, String productImage, String brandName, String brandLogo, String productName, Long buyPrice) {
        this.productIdx = productIdx;
        this.productImage = productImage;
        this.brandName = brandName;
        this.brandLogo = brandLogo;
        this.productName = productName;
        this.buyPrice = buyPrice;
    }
}
