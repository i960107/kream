package com.example.demo.src.user.model.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetUserOwnProduct {
    private String productName;
    private String brandName;
    private String image;
    private String size;
    private Long purchasePrice;
    private Long latestTransactedPrice;
    private Float valueIncreaseRate;
    private Long valueIncreaseAmount;

    @Builder
    public GetUserOwnProduct(
            String productName,
            String brandName,
            String image,
            String size,
            Long purchasePrice,
            Long latestTransactedPrice,
            Float valueIncreaseRate,
            Long valueIncreaseAmount) {
        this.productName = productName;
        this.brandName = brandName;
        this.image = image;
        this.size = size;
        this.purchasePrice = purchasePrice;
        this.latestTransactedPrice = latestTransactedPrice;
        this.valueIncreaseRate = valueIncreaseRate;
        this.valueIncreaseAmount = valueIncreaseAmount;
    }
}
