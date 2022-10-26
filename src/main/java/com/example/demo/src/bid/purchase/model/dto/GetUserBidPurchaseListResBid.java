package com.example.demo.src.bid.purchase.model.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class GetUserBidPurchaseListResBid {
    Long productIdx;
    String productName;
    Long sizeIdx;
    String size;
    Long bidPrice;
    LocalDate deadline;
    String status;

    public GetUserBidPurchaseListResBid(Long productIdx, String productName, Long sizeIdx, String size, Long bidPrice, LocalDate deadline, String status) {
        this.productIdx = productIdx;
        this.productName = productName;
        this.sizeIdx = sizeIdx;
        this.size = size;
        this.bidPrice = bidPrice;
        this.deadline = deadline;
        this.status = status;
    }
}
