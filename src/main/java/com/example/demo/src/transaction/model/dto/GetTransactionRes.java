package com.example.demo.src.transaction.model.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class GetTransactionRes {
    private String productSize;
    private Long transactedPrice;
    private LocalDate transactedAt;

    public GetTransactionRes(String productSize,
                             Long transactedPrice,
                             LocalDate transactedAt) {
        this.productSize = productSize;
        this.transactedPrice = transactedPrice;
        this.transactedAt = transactedAt;
    }
}

