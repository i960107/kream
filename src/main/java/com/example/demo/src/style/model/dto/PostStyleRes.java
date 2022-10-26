package com.example.demo.src.style.model.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostStyleRes {
    private Long idx;
    private Long userIdx;
    private List<GetStyleImageRes> images;
    private String content;
    private List<Long> products;
    private LocalDateTime createdAt;

    public PostStyleRes(
            Long idx,
            Long userIdx,
            List<GetStyleImageRes> images,
            String content,
            List<Long> products,
            LocalDateTime createdAt) {
        this.idx = idx;
        this.userIdx = userIdx;
        this.images = images;
        this.content = content;
        this.products = products;
        this.createdAt = createdAt;
    }
}
