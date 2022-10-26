package com.example.demo.src.product.model.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class GetProductCategoryDetailListRes {
    List<GetProductCategoryListRes> categorylist;

    public GetProductCategoryDetailListRes(List<GetProductCategoryListRes> categorylist) {
        this.categorylist = categorylist;
    }
}
