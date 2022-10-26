package com.example.demo.src.user.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserRes {
    private Long idx;
    private String email;
    private String profileImage;
    private String nickName;
    private String name;
    private String introduction;
    private String grade;
    private Long point;
    private long productLikeCount;
    private long saleBiddingCount;
    private long saleProceedingCount;
    private long saleCompletedCount;
    private long purchaseProceedingCount;
    private long purchaseBiddingCount;
    private long purchaseCompletedCount;
    private Long totalProductValue;
    private int productOwnCount;
    private Long totalProductPurchasePrice;
    private Float totalValueIncreaseRate;
    private Long totalValueIncreaseAmount;

    @Builder
    public GetUserRes(Long idx,
                      String email,
                      String profileImage,
                      String nickName,
                      String name,
                      String introduction,
                      String grade,
                      Long point,
                      long productLikeCount,
                      long saleBiddingCount,
                      long saleProceedingCount,
                      long saleCompletedCount,
                      long purchaseProceedingCount,
                      long purchaseBiddingCount,
                      long purchaseCompletedCount,
                      Long totalProductValue,
                      int ownProductCount,
                      Long totalProductPurchasePrice,
                      Float totalValueIncreaseRate,
                      Long totalValueIncreaseAmount) {
        this.idx = idx;
        this.email = email;
        this.profileImage = profileImage;
        this.nickName = nickName;
        this.name = name;
        this.introduction = introduction;
        this.grade = grade;
        this.point = point;
        this.productLikeCount = productLikeCount;
        this.saleProceedingCount = saleProceedingCount;
        this.saleBiddingCount = saleBiddingCount;
        this.saleCompletedCount = saleCompletedCount;
        this.purchaseProceedingCount = purchaseProceedingCount;
        this.purchaseBiddingCount = purchaseBiddingCount;
        this.purchaseCompletedCount = purchaseCompletedCount;
        this.totalProductValue = totalProductValue;
        this.productOwnCount = ownProductCount;
        this.totalProductPurchasePrice = totalProductPurchasePrice;
        this.totalValueIncreaseRate = totalValueIncreaseRate;
        this.totalValueIncreaseAmount = totalValueIncreaseAmount;
    }
}
