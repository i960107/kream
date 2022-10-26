package com.example.demo.src.product.model.dto;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class GetProductCategoryListRes {
    private Byte idx;
    private String category;
    private byte position;
    private List<GetProductCategoryRes> detailCategoryList;

    public GetProductCategoryListRes(Byte idx,String category, byte position, List<GetProductCategoryRes> categoryList) {
        this.idx = idx;
        this.category=category;
        this.position = position;
        this.detailCategoryList = categoryList;
    }
}
