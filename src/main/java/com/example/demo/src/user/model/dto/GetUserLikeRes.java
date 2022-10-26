package com.example.demo.src.user.model.dto;

import lombok.Getter;

@Getter
public class GetUserLikeRes {
    private String brandName;
    private String brandLogo;
    private Long productIdx;
    private String productThumbnail;
    private String productName;
    private Long productSizeIdx;
    private String productSize;
    private Long buyOutPrice;

    public GetUserLikeRes(
            String brandName,
            String brandLogo,
            Long productIdx,
            String productThumbnail,
            String productName,
            Long productSizeIdx,
            String productSize,
            Long buyOutPrice) {
        this.brandName = brandName;
        this.brandLogo = brandLogo;
        this.productSizeIdx = productSizeIdx;
        this.productIdx = productIdx;
        this.productThumbnail = productThumbnail;
        this.productName = productName;
        this.productSize = productSize;
        this.buyOutPrice = buyOutPrice;
    }
}
