package com.example.demo.src.product.model.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PostProductSaleRes {
    private Long bidSaleIdx;
    private String productThumbnail;
    private String productName;
    private String productSize;
    private Long bidPrice;
    private Long inspectionFee;
    private Long shippingFee;
    private Long commission;
    private Long settlementAmount;
    private LocalDate createdAt;
    private LocalDate deadline;
    private String bank;
    private String account;
    private String accountHolder;


    public PostProductSaleRes(
            Long bidSaleIdx,
            String productThumbnail,
            String productName,
            String productSize,
            Long bidPrice,
            Long inspectionFee,
            Long shippingFee,
            Long commission,
            Long settlementAmount,
            LocalDate createdAt,
            LocalDate deadline,
            String bank,
            String account,
            String accountHolder) {
        this.bidSaleIdx = bidSaleIdx;
        this.productThumbnail = productThumbnail;
        this.productName = productName;
        this.productSize = productSize;
        this.bidPrice = bidPrice;
        this.inspectionFee = inspectionFee;
        this.settlementAmount = settlementAmount;
        this.shippingFee = shippingFee;
        this.commission = commission;
        this.createdAt = createdAt;
        this.deadline = deadline;
        this.bank = bank;
        this.account = account;
        this.accountHolder = accountHolder;
    }
}
