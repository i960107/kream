package com.example.demo.src.user.model.dto;

import lombok.Getter;

@Getter
public class GetUserLoginRes {
    private Long userIdx;
    private String accessToken;

    public GetUserLoginRes(Long userIdx, String accessToken) {
        this.userIdx = userIdx;
        this.accessToken = accessToken;
    }
}
