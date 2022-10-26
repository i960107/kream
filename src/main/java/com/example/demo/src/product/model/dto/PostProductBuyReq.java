package com.example.demo.src.product.model.dto;

import com.example.demo.config.BidPrice;
import com.example.demo.utils.ValidationRegex;
import lombok.Getter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
public class PostProductBuyReq {
    @NotNull
    @PositiveOrZero
    private Long productIdx;
    @NotNull
    @PositiveOrZero
    private  Long productSizeIdx;
    @NotNull
    @Min(35000)
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

    public PostProductBuyReq(
            Long productIdx,
            Long productSizeIdx,
            Long purchasePrice,
            String point,
            Long inspectionFee,
            Long shippingFee,
            Long totalPrice,
            LocalDate deadline,
            Long addressIdx,
            Long cardIdx) {
        this.productIdx = productIdx;
        this.productSizeIdx = productSizeIdx;
        this.purchasePrice = purchasePrice;
        this.point = point;
        this.inspectionFee = inspectionFee;
        this.shippingFee = shippingFee;
        this.totalPrice = totalPrice;
        this.deadline = deadline;
        this.addressIdx = addressIdx;
        this.cardIdx = cardIdx;
    }
}
