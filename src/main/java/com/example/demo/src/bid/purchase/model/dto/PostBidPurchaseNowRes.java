package com.example.demo.src.bid.purchase.model.dto;

import com.example.demo.config.Status;
import lombok.Getter;

@Getter
public class PostBidPurchaseNowRes {
    private Long bidPurchaseIdx;
    private String thumbnail;
    private Long paymentAmount;
    private  Long productPrice;
    private Long inspectionFee;
    private Long shippingFee;
    private Long pointUsed;
    private Status status;

    public PostBidPurchaseNowRes(Long bidPurchaseIdx,String thumbnail, Long paymentAmount, Long productPrice, Long inspectionFee, Long shippingFee, Long pointUsed,Status status) {
        this.bidPurchaseIdx = bidPurchaseIdx;
        this.thumbnail = thumbnail;
        this.paymentAmount = paymentAmount;
        this.productPrice = productPrice;
        this.inspectionFee = inspectionFee;
        this.shippingFee = shippingFee;
        this.pointUsed = pointUsed;
        this.status = status;
    }
}
