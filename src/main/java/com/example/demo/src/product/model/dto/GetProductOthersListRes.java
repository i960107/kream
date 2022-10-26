package com.example.demo.src.product.model.dto;

import lombok.Getter;

import java.util.List;


@Getter
public class GetProductOthersListRes {
    private String brandName;
    private List<GetProductOthersRes> otherList;

    public GetProductOthersListRes(String brandName, List<GetProductOthersRes> otherList) {
        this.brandName = brandName;
        this.otherList = otherList;
    }
}
