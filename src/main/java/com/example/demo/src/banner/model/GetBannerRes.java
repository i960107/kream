package com.example.demo.src.banner.model;

import lombok.Getter;

@Getter
public class GetBannerRes {
    private String image;
    private byte position;
    private Long productIdx;

    public GetBannerRes(
            String image,
            byte position,
            Long productIdx) {
        this.image = image;
        this.position = position;
        this.productIdx = productIdx;
    }
}
