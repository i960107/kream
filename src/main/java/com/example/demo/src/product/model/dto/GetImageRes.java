package com.example.demo.src.product.model.dto;

import lombok.Getter;

@Getter
public class GetImageRes {
    private String image;
    private Byte position;

    public GetImageRes(String image, Byte position) {
        this.image = image;
        this.position = position;
    }
}
