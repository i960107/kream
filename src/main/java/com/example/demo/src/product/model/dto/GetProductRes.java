package com.example.demo.src.product.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class GetProductRes {
    private String brandName;
    private String name;
    private List<GetImageRes> productImages;
    private String description;
    private String modelNo;
    private LocalDate releaseDate;
    private String color;
    private Long releasePrice;
    private Long liked;
    private Long latestTransactedPrice;
    private Float priceIncreaseRate;
    private Long priceIncreaseAmount;
    private Long buyPrice;
    private Long sellPrice;

    @Builder
    public GetProductRes(String brandName,
                         String name,
                         String description,
                         List<GetImageRes> productImages,
                         String modelNo,
                         LocalDate releaseDate,
                         String color,
                         Long releasePrice,
                         Long liked,
                         Long latestTransactedPrice,
                         Float priceIncreaseRate,
                         Long priceIncreaseAmount,
                         Long buyPrice,
                         Long sellPrice) {
        this.brandName = brandName;
        this.name = name;
        this.productImages =productImages;
        this.description = description;
        this.modelNo = modelNo;
        this.releaseDate = releaseDate;
        this.color = color;
        this.releasePrice = releasePrice;
        this.liked = liked;
        this.latestTransactedPrice = latestTransactedPrice;
        this.priceIncreaseRate = priceIncreaseRate;
        this.priceIncreaseAmount = priceIncreaseAmount;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }
}
