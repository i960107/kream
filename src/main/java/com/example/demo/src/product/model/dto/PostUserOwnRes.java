package com.example.demo.src.product.model.dto;

import lombok.Getter;

@Getter
public class PostUserOwnRes {
    Long userOwnIdx;
    Long productIdx;
    Long productSizeIdx;

    public PostUserOwnRes(Long userOwnIdx, Long productIdx, Long productSizeIdx) {
        this.userOwnIdx = userOwnIdx;
        this.productIdx = productIdx;
        this.productSizeIdx = productSizeIdx;
    }
}
