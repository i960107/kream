package com.example.demo.src.transaction.model.dto;

import com.example.demo.src.transaction.model.entity.Transaction;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GetTransactionListRes {
    private List transactionList;
    private int totalPages;
    private Long totalElements;
    private int pageNumber;
    private boolean first;
    private boolean last;


    public GetTransactionListRes(Page pageTransaction) {
        List<GetTransactionRes> getTransactionResList = new ArrayList<>();
        List<Transaction> list =pageTransaction.getContent();
        for (Transaction transaction : list) {
            getTransactionResList.add(
                    new GetTransactionRes(
                            transaction.getProductSize().getSize(),
                            transaction.getWinningBidPrice(),
                            transaction.getCreatedAt().toLocalDate()));
        }
        this.transactionList = getTransactionResList;
        this.totalPages = pageTransaction.getTotalPages();
        this.totalElements = pageTransaction.getTotalElements();
        this.pageNumber = ((PageImpl) pageTransaction).getPageable().getPageNumber();
        this.first = pageTransaction.isFirst();
        this.last = pageTransaction.isLast();
    }
}
