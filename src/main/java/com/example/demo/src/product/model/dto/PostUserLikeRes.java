package com.example.demo.src.product.model.dto;

import com.example.demo.src.product.model.entity.Product;
import com.example.demo.src.product.model.entity.ProductLike;
import com.example.demo.src.user.model.dto.GetUserRes;
import com.example.demo.src.user.model.entity.User;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class PostUserLikeRes {
    private Long productLikeIdx;
    private Long userIdx;
    private Long productSizeIdx;

    public PostUserLikeRes(Long productLikeIdx, Long userIdx, Long productSizeIdx) {
        this.productLikeIdx = productLikeIdx;
        this.userIdx = userIdx;
        this.productSizeIdx = productSizeIdx;
    }
}
