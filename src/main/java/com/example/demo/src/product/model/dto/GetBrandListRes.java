package com.example.demo.src.product.model.dto;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class GetBrandListRes {
    List<GetBrandRes> brandList;

    public GetBrandListRes(List<GetBrandRes> brandList) {
        this.brandList = brandList;
    }
}
