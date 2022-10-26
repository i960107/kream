package com.example.demo.src.bid.sale.model.dto;

import com.example.demo.src.bid.purchase.model.dto.GetUserBidPurchaseListResBid;
import lombok.Getter;

import java.util.List;

@Getter
public class GetUserBidSaleListRes {
    String Status;
    long count;
    List<GetUserBidSaleListResBid> bidList;

    public GetUserBidSaleListRes(String status, long count, List<GetUserBidSaleListResBid> bidList) {
        Status = status;
        this.count = count;
        this.bidList = bidList;
    }
}
