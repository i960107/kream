package com.example.demo.src.product.model;

import com.example.demo.config.Status;
import lombok.Getter;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@Getter
public class PostProductBuyRes {
    private Long bidPurchaseIdx;
    private String productThumbnail;
    private Long bidPurchasePrice;
    private Long inspectionFee;
    private Long shippingFee;
    private Long totalPrice;
    private LocalDate createdAt;
    private LocalDate deadline;
    private Status status;

    public PostProductBuyRes(
            Long bidPurchaseIdx,
            String productThumbnail,
            Long bidPurchasePrice,
            Long inspectionFee,
            Long shippingFee,
            Long totalPrice,
            LocalDate createdAt,
            LocalDate deadline,
            Status status
    ) {
        this.bidPurchaseIdx = bidPurchaseIdx;
        this.productThumbnail = productThumbnail;
        this.bidPurchasePrice = bidPurchasePrice;
        this.inspectionFee = inspectionFee;
        this.shippingFee = shippingFee;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.deadline = deadline;
        this.status = status;
    }
}
