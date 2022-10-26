package com.example.demo.src.product.model.dto;

import com.example.demo.config.BidPrice;
import com.example.demo.utils.ValidationRegex;
import lombok.Getter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
public class PostProductPurchaseReq {
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
    @Future
    private LocalDate deadline;
    @NotNull
    @PositiveOrZero
    private Long addressIdx;
    @NotNull
    @PositiveOrZero
    private Long cardIdx;
}
