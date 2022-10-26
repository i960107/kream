package com.example.demo.src.bid.purchase.model.dto;

import com.example.demo.src.bid.sale.model.dto.GetBidSaleRes;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GetBidPurchaseListRes {
    private List bidPurchaseList;
    private int totalPages;
    private Long totalElements;
    private int pageNumber;
    private boolean first;
    private boolean last;

    public GetBidPurchaseListRes(Page pageBidPurchaseList){
        this.bidPurchaseList =pageBidPurchaseList.getContent();
        this.totalPages = pageBidPurchaseList.getTotalPages();
        this.totalElements = pageBidPurchaseList.getTotalElements();
        this.pageNumber = ((PageImpl) pageBidPurchaseList).getPageable().getPageNumber();
        this.first = pageBidPurchaseList.isFirst();
        this.last = pageBidPurchaseList.isLast();
    }
}
