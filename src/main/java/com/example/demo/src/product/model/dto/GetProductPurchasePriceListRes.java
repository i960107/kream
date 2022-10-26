package com.example.demo.src.product.model.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class GetProductPurchasePriceListRes {
    List<GetProductPurchasePriceRes> purchasePriceRes;

    public GetProductPurchasePriceListRes(List<GetProductPurchasePriceRes> purchasePriceRes) {
        this.purchasePriceRes = purchasePriceRes;
    }
}
