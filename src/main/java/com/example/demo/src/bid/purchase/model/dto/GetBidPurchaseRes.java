package com.example.demo.src.bid.purchase.model.dto;

import lombok.Getter;

@Getter
public class GetBidPurchaseRes {
    private String productSize;
    private Long bidPrice;
    private Long count;

    public GetBidPurchaseRes(String productSize, Long bidPrice, Long count) {
        this.productSize = productSize;
        this.bidPrice = bidPrice;
        this.count = count;
    }
}
