package com.example.demo.src.product.model.dto;

import com.example.demo.config.Status;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PostProductPurchaseRes {
    private Long bidPurchaseIdx;
    private String productThumbnail;
    private String productModelNo;
    private String productName;
    private String productSize;
    private String addressName;
    private String address;
    private String addressDetail;
    private String zipCode;
    private Long totalPrice;
    private Long bidPrice;
    private Long inspectionFee;
    private Long shippingFee;
    private String point;
    private LocalDate createdAt;
    private LocalDate deadline;
    private Status status;

    public PostProductPurchaseRes(
            Long bidPurchaseIdx,
            String productThumbnail,
            String productModelNo,
            String productName,
            String productSize,
            String addressName,
            String address,
            String addressDetail,
            String zipCode,
            Long totalPrice,
            Long bidPrice,
            Long inspectionFee,
            Long shippingFee,
            String point,
            LocalDate createdAt,
            LocalDate deadline,
            Status status) {
        this.bidPurchaseIdx = bidPurchaseIdx;
        this.productThumbnail = productThumbnail;
        this.productModelNo = productModelNo;
        this.productName = productName;
        this.productSize = productSize;
        this.addressName = addressName;
        this.address = address;
        this.addressDetail = addressDetail;
        this.zipCode = zipCode;
        this.totalPrice = totalPrice;
        this.bidPrice = bidPrice;
        this.point = point;
        this.inspectionFee = inspectionFee;
        this.shippingFee = shippingFee;
        this.createdAt = createdAt;
        this.deadline = deadline;
        this.status = status;
    }
}
