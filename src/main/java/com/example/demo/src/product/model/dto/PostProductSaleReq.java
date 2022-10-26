package com.example.demo.src.product.model.dto;

import com.example.demo.config.BaseException;
import com.example.demo.config.BidPrice;
import lombok.Getter;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@Getter
public class PostProductSaleReq {
    @NotNull
    private Long productSizeIdx;

    @NotNull
    @BidPrice
    private Long bidPrice;

    @NotNull
    @PositiveOrZero
    private Long inspectionFee;

    @NotNull
    @PositiveOrZero
    private Long commission;

    @NotNull
    @PositiveOrZero
    private Long shippingFee;

    @NotNull
    @PositiveOrZero
    private Long settlementAmount;

    @NotNull
    @Future
    private LocalDate deadline;

    @NotNull
    private Long accountIdx;

    @NotNull
    private Long addressIdx;

    @NotNull
    private Long cardIdx;



}
