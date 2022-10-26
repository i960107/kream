package com.example.demo.src.product.model.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class GetProductSizeListRes {
    private List<GetProductSizeRes> sizeList;

    public GetProductSizeListRes(List<GetProductSizeRes> sizeList) {
        this.sizeList = sizeList;
    }
}
