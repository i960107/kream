package com.example.demo.src.product.model.dto;

import lombok.Getter;

import java.util.Map;

@Getter
public class GetBrandRes {
    Long idx;
    String name;

    public GetBrandRes(Long idx, String name) {
        this.idx = idx;
        this.name = name;
    }
}
