package com.example.demo.src.bid.purchase.model.dto;

import com.example.demo.config.BidPrice;
import com.example.demo.utils.ValidationRegex;
import lombok.Getter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@Getter
public class PostBidPurchaseNowReq {
    @NotNull
    @PositiveOrZero
    private Long targetBidSaleIdx;

    @NotNull
    @PositiveOrZero
    private Long cardIdx;

}
