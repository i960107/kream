package com.example.demo.src.product.model.dto;

import lombok.Getter;

@Getter
public class GetProductSalePriceRes {
    private Long productSizeIdx;
    private String productSize;
    private Long salePrice;
    private Long bidPurchaseIdx;

    public GetProductSalePriceRes(Long productSizeIdx, String productSize, Long salePrice, Long bidPurchaseIdx) {
        this.productSizeIdx = productSizeIdx;
        this.productSize = productSize;
        this.salePrice = salePrice;
        this.bidPurchaseIdx = bidPurchaseIdx;
    }
}
