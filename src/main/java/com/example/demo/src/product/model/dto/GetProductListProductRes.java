package com.example.demo.src.product.model.dto;

import lombok.Getter;

@Getter
public class GetProductListProductRes {
    private Long idx;
    private String thumbnail;
    private String brandName;
    private String brandLogo;
    private String name;
    private String description;
    private Long buyPrice;
    private Long liked;
    private Long tagged;

    public GetProductListProductRes(Long idx, String thumbnail, String brandName, String brandLogo, String name, String description, Long buyPrice, Long liked, Long tagged) {
        this.idx = idx;
        this.thumbnail = thumbnail;
        this.brandName = brandName;
        this.brandLogo = brandLogo;
        this.name = name;
        this.description = description;
        this.buyPrice = buyPrice;
        this.liked = liked;
        this.tagged = tagged;
    }
}
