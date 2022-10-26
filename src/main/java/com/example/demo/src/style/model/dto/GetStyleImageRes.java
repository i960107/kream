package com.example.demo.src.style.model.dto;

import lombok.Getter;

@Getter
public class GetStyleImageRes {
    private String image;
    private Byte position;

    public GetStyleImageRes(String image, Byte position) {
        this.image = image;
        this.position = position;
    }
}
