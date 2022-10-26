package com.example.demo.src.product.model.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class PostUserLikeReq {
    @NotNull
    private Long productSizeIdx;
}
