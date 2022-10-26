package com.example.demo.src.product.model.dto;

import com.example.demo.config.BidPrice;
import com.example.demo.utils.ValidationRegex;
import lombok.Getter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
public class PostProductPurchaseNowReq {
    @NotNull
    @PositiveOrZero
    private Long targetBidSaleIdx;
    @NotNull
    @PositiveOrZero
    private  Long productSizeIdx;
    @NotNull
    @BidPrice
    private Long purchasePrice;
    @NotBlank
    @Pattern(regexp = ValidationRegex.regexBoolean)
    private String point;
    @NotNull
    @PositiveOrZero
    private Long inspectionFee;
    @NotNull
    @PositiveOrZero
    private Long shippingFee;
    @NotNull
    @PositiveOrZero
    private Long totalPrice;
    @NotNull
    @PositiveOrZero
    private Long addressIdx;

    public PostProductPurchaseNowReq(
            Long productSizeIdx,
            Long purchasePrice,
            String point,
            Long inspectionFee,
            Long shippingFee,
            Long totalPrice,
            Long addressIdx
            ){
        this.productSizeIdx = productSizeIdx;
        this.purchasePrice = purchasePrice;
        this.point = point;
        this.inspectionFee = inspectionFee;
        this.shippingFee = shippingFee;
        this.totalPrice = totalPrice;
        this.addressIdx = addressIdx;
    }

}
