package com.example.demo.src.bid.sale.model.dto;

import com.example.demo.config.Status;
import lombok.Getter;

@Getter
public class PostBidSaleNowRes {

    private Long bidSaleIdx;
    private String thumbnail;
    private Long salesSettlement;
    private Long productPrice;
    private Long inspectionFee;
    private Long shippingFee;
    private String bank;
    private String account;
    private String accountHolder;
    private Status status;

    public PostBidSaleNowRes(
            Long bidSaleIdx,
            String thumbnail,
            Long salesSettlement,
            Long productPrice,
            Long inspectionFee,
            Long shippingFee,
            String bank,
            String account,
            String accountHolder,
            Status status) {
        this.bidSaleIdx = bidSaleIdx;
        this.thumbnail = thumbnail;
        this.salesSettlement = salesSettlement;
        this.productPrice = productPrice;
        this.inspectionFee = inspectionFee;
        this.shippingFee = shippingFee;
        this.bank = bank;
        this.account = account;
        this.accountHolder = accountHolder;
        this.status = status;
    }
}
