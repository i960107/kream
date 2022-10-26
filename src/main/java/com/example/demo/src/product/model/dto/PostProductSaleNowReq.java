package com.example.demo.src.product.model.dto;

import com.example.demo.config.BidPrice;
import com.example.demo.utils.ValidationRegex;
import lombok.Getter;

import javax.validation.constraints.*;

@Getter
public class PostProductSaleNowReq {
    @NotNull
    @PositiveOrZero
    private Long targetBidPurchaseIdx;
    @NotNull
    @PositiveOrZero
    private Long productSizeIdx;
    @NotNull
    @BidPrice
    private Long salePrice;
    @NotNull
    @PositiveOrZero
    private Long inspectionFee;
    @NotNull
    @PositiveOrZero
    private Long shippingFee;
    @NotNull
    @PositiveOrZero
    private Long commission;
    @NotNull
    @PositiveOrZero
    private Long settlementAmount;
    @NotNull
    @PositiveOrZero
    private Long accountIdx;
    @NotNull
    @PositiveOrZero
    private Long addressIdx;

}
