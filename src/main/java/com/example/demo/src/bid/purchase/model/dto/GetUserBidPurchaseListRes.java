package com.example.demo.src.bid.purchase.model.dto;

import com.example.demo.src.bid.sale.model.dto.GetUserBidSaleListResBid;
import lombok.Getter;

import java.util.List;

@Getter
public class GetUserBidPurchaseListRes {
    String Status;
    long count;
    List<GetUserBidPurchaseListResBid> bidList;

    public GetUserBidPurchaseListRes(String status, long count, List<GetUserBidPurchaseListResBid> bidList) {
        Status = status;
        this.count = count;
        this.bidList = bidList;
    }
}
