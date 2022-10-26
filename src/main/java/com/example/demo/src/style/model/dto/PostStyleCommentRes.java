package com.example.demo.src.style.model.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostStyleCommentRes {
    private Long styleIdx;
    private Long commentIdx;
    private Long userIdx;
    private String nickName;
    private String profileImage;
    private Long parentIdx;
    private String content;
    private LocalDateTime createdAt;

    public PostStyleCommentRes(
            Long styleIdx,
            Long commentIdx,
            Long userIdx,
            String nickName,
            String profileImage,
            Long parentIdx,
            String content,
            LocalDateTime createdAt) {
        this.styleIdx = styleIdx;
        this.commentIdx = commentIdx;
        this.userIdx = userIdx;
        this.nickName = nickName;
        this.profileImage = profileImage;
        this.parentIdx = parentIdx;
        this.content = content;
        this.createdAt = createdAt;
    }
}
