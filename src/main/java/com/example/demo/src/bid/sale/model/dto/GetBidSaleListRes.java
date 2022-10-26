package com.example.demo.src.bid.sale.model.dto;

import com.example.demo.src.bid.sale.model.entity.BidSale;
import com.example.demo.src.transaction.model.dto.GetTransactionRes;
import com.example.demo.src.transaction.model.entity.Transaction;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GetBidSaleListRes {
    private List bidSaleList;
    private int totalPages;
    private Long totalElements;
    private int pageNumber;
    private boolean first;
    private boolean last;

    public GetBidSaleListRes(Page pageBidSaleList){
        this.bidSaleList = pageBidSaleList.getContent();
        this.totalPages = pageBidSaleList.getTotalPages();
        this.totalElements = pageBidSaleList.getTotalElements();
        this.pageNumber = ((PageImpl) pageBidSaleList).getPageable().getPageNumber();
        this.first = pageBidSaleList.isFirst();
        this.last = pageBidSaleList.isLast();
    }
}
