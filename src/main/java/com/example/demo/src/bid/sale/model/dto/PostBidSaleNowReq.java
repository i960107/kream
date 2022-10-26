package com.example.demo.src.bid.sale.model.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
public class PostBidSaleNowReq {

    @NotNull
    @PositiveOrZero
    private Long targetBidPurchaseIdx;

    @NotNull
    @PositiveOrZero
    private Long cardIdx;
}
