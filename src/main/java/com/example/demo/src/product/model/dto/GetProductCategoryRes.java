package com.example.demo.src.product.model.dto;

import lombok.Getter;

@Getter
public class GetProductCategoryRes {
    private Long idx;
    private String name;
    private String image;
    private byte position;

    public GetProductCategoryRes(Long idx, String name, String image, byte position) {
        this.idx = idx;
        this.name = name;
        this.image = image;
        this.position = position;
    }
}
