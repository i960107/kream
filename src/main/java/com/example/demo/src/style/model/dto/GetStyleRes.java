package com.example.demo.src.style.model.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GetStyleRes {
    private Long idx;
    private String userProfileImage;
    private String userNickName;
    private List<GetStyleImageRes> images;
    private String content;
    private List<GetStyleProductRes> products;
    private Long liked;
    private Long commented;
    private LocalDateTime createdAt;

    public GetStyleRes(
            Long idx,
            String userProfileImage,
            String userNickName,
            List<GetStyleImageRes> images,
            String content,
            List<GetStyleProductRes> products,
            Long liked,
            Long commented,
            LocalDateTime createdAt
    ) {
        this.idx = idx;
        this.products = products;
        this.userProfileImage = userProfileImage;
        this.userNickName = userNickName;
        this.images = images;
        this.content = content;
        this.liked = liked;
        this.commented = commented;
        this.createdAt = createdAt;
    }
}
