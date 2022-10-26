package com.example.demo.src.user.model.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class PostUserAccountReq {
    @NotBlank
    private String bank;
    @NotBlank
    private String account;
    @NotBlank
    private String accountHolder;

    public PostUserAccountReq(String bank, String account, String accountHolder) {
        this.bank = bank;
        this.account = account;
        this.accountHolder = accountHolder;
    }
}
