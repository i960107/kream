package com.example.demo.src.product.model.dto;


import lombok.Getter;

@Getter
public class GetProductPurchasePriceRes {
    private Long productSizeIdx;
    private String productSize;
    private Long buyPrice;
    private Long bidSaleIdx;

    public GetProductPurchasePriceRes(Long productSizeIdx, String productSize, Long buyPrice, Long bidSaleIdx) {
        this.productSizeIdx = productSizeIdx;
        this.productSize = productSize;
        this.buyPrice = buyPrice;
        this.bidSaleIdx = bidSaleIdx;
    }
}
