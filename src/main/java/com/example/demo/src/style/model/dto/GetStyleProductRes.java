package com.example.demo.src.style.model.dto;

import lombok.Getter;

@Getter
public class GetStyleProductRes {
    private Long idx;
    private String thumbnail;
    private String name;
    private Long buyPrice;

    public GetStyleProductRes(Long idx, String thumbnail, String name, Long buyPrice) {
        this.idx = idx;
        this.thumbnail = thumbnail;
        this.name = name;
        this.buyPrice = buyPrice;
    }
}
