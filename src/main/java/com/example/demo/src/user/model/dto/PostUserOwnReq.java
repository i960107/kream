package com.example.demo.src.user.model.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class PostUserOwnReq {
    @NotNull
    private Long productIdx;
    @NotNull
    private Long productSizeIdx;
    @NotNull
    private Long buyPrice;
}
