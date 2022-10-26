package com.example.demo.src.product.model.dto;

import lombok.Getter;

@Getter
public class GetProductOthersRes {
    private Long idx;
    private String brandLogo;
    private String brandName;
    private String name;
    private String thumbnail;
    private Long buyPrice;

    public GetProductOthersRes(Long idx, String brandLogo,String brandName, String name, String thumbnail, Long buyPrice) {
        this.idx = idx;
        this.brandLogo = brandLogo;
        this.brandName = brandName;
        this.name = name;
        this.thumbnail = thumbnail;
        this.buyPrice = buyPrice;
    }
}
