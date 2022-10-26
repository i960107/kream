package com.example.demo.src.product.model.dto;

import lombok.Getter;

import java.awt.*;
import java.util.List;

@Getter
public class GetProductListRes {
    List<GetProductListProductRes> productList;

    public GetProductListRes(List<GetProductListProductRes> productList) {
        this.productList = productList;
    }
}
