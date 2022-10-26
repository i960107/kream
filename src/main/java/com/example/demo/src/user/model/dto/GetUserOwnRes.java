package com.example.demo.src.user.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GetUserOwnRes {
    private Long userIdx;
    private Long totalValue;
    private Integer count;
    private Long totalPurchasePrice;
    private Float totalValueIncreaseRate;
    private Long totalValueIncreaseAmount;
    private List<GetUserOwnProduct> productList;

    @Builder
    public GetUserOwnRes(Long userIdx,
                         Long totalValue,
                         Integer count,
                         Long totalPurchasePrice,
                         Float totalValueIncreaseRate,
                         Long totalValueIncreaseAmount,
                         List<GetUserOwnProduct> productList) {
        this.userIdx = userIdx;
        this.totalValue = totalValue;
        this.count = count;
        this.totalPurchasePrice = totalPurchasePrice;
        this.totalValueIncreaseRate = totalValueIncreaseRate;
        this.totalValueIncreaseAmount = totalValueIncreaseAmount;
        this.productList = productList;
    }

}

