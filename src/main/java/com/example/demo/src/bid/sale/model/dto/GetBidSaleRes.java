package com.example.demo.src.bid.sale.model.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class GetBidSaleRes {
    private String productSize;
    private Long bidPrice;
    private Long count;

    public GetBidSaleRes(String productSize, Long bidPrice, Long count) {
        this.productSize = productSize;
        this.bidPrice = bidPrice;
        this.count = count;
    }
}
