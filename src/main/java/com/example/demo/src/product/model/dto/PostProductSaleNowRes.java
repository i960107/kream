package com.example.demo.src.product.model.dto;

import com.example.demo.config.Status;
import lombok.Getter;

@Getter
public class PostProductSaleNowRes {
    private Long bidSaleIdx;
    private Long targetBidPurchaseIdx;
    private String productThumbnail;
    private String productModelNo;
    private String productName;
    private String productSize;
    private String bank;
    private String account;
    private String accountHolder;
    private String addressName;
    private String addressPhone;
    private String address;
    private String addressDetail;
    private String zipCode;
    private Long settlementAmount;
    private Long salePrice;
    private Long inspectionFee;
    private Long shippingFee;
    private Long commission;
    private Status status;

    public PostProductSaleNowRes(
            Long bidSaleIdx,
            Long targetBidPurchaseIdx,
            String productThumbnail,
            String productModelNo,
            String productName,
            String productSize,
            String bank,
            String account,
            String accountHolder,
            String addressName,
            String address,
            String addressDetail,
            String zipCode,
            String addressPhone,
            Long settlementAmount,
            Long salePrice,
            Long inspectionFee,
            Long shippingFee,
            Long commission,
            Status status) {
        this.bidSaleIdx = bidSaleIdx;
        this.targetBidPurchaseIdx = targetBidPurchaseIdx;
        this.productThumbnail = productThumbnail;
        this.productModelNo = productModelNo;
        this.productName = productName;
        this.productSize = productSize;
        this.bank = bank;
        this.account = account;
        this.accountHolder = accountHolder;
        this.addressName = addressName;
        this.address = address;
        this.addressDetail = addressDetail;
        this.zipCode = zipCode;
        this.addressPhone = addressPhone;
        this.settlementAmount = settlementAmount;
        this.salePrice = salePrice;
        this.inspectionFee = inspectionFee;
        this.shippingFee = shippingFee;
        this.commission = commission;
        this.status = status;
    }
}
