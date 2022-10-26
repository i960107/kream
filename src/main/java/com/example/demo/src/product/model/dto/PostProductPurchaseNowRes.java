package com.example.demo.src.product.model.dto;

import com.example.demo.config.Status;
import lombok.Getter;

@Getter
public class PostProductPurchaseNowRes {
    private Long bidPurchaseIdx;
    private Long targetBidSaleIdx;
    private String productThumbnail;
    private String productModelNo;
    private String productName;
    private String productSize;
    private String addressName;
    private String address;
    private String addressDetail;
    private String addressPhone;
    private String zipCode;
    private Long totalPrice;
    private Long buyPrice;
    private Long inspectionFee;
    private Long shippingFee;
    private Status status;

    public PostProductPurchaseNowRes(
            Long  bidPurchaseIdx,
            Long targetBidSaleIdx,
            String productThumbnail,
            String productModelNo,
            String productName,
            String productSize,
            String addressName,
            String addressPhone,
            String address,
            String addressDetail,
            String zipCode,
            Long totalPrice,
            Long productPrice,
            Long inspectionFee,
            Long shippingFee,
            Status status
    ) {
        this.bidPurchaseIdx = bidPurchaseIdx;
        this.targetBidSaleIdx = targetBidSaleIdx;
        this.productThumbnail = productThumbnail;
        this.productModelNo = productModelNo;
        this.productName = productName;
        this.productSize = productSize;
        this.addressName = addressName;
        this.addressPhone = addressPhone;
        this.address = address;
        this.zipCode = zipCode;
        this.addressDetail = addressDetail;
        this.totalPrice = totalPrice;
        this.buyPrice = productPrice;
        this.inspectionFee = inspectionFee;
        this.shippingFee = shippingFee;
        this.status = status;
    }
}
