package com.example.demo.src.product.model.dto;

import com.example.demo.config.Status;
import lombok.Getter;

@Getter
public class DeleteProductLikeRes {
    private Long productLikeIdx;
    private Long userIdx;
    private Long productSizeIdx;
    private Status status;

    public DeleteProductLikeRes(Long productLikeIdx, Long userIdx, Long productIdx, Status status) {
        this.productLikeIdx = productLikeIdx;
        this.userIdx = userIdx;
        this.productSizeIdx = productIdx;
        this.status = status;
    }
}
