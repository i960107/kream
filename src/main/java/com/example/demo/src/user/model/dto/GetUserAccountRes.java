package com.example.demo.src.user.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class GetUserAccountRes {
    @NotNull
    private Long accountIdx;
    @NotBlank
    private String bank;
    @NotBlank
    private String account;
    @NotBlank
    private String accountHolder;

    public GetUserAccountRes(Long accountIdx, String bank, String account, String accountHolder) {
        this.accountIdx = accountIdx;
        this.bank = bank;
        this.account = account;
        this.accountHolder = accountHolder;
    }
}
