package com.example.demo.src.style.model.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class GetStyleChildCommentRes {
    private Long idx;
    private Long userIdx;
    private String nickName;
    private String profileImage;
    private String commentedTo;
    private String content;
    private LocalDateTime createdAt;

    public GetStyleChildCommentRes(
            Long idx,
            Long userIdx,
            String nickName,
            String profileImage,
            String commentedTo,
            String content,
            LocalDateTime createdAt) {
        this.idx = idx;
        this.userIdx = userIdx;
        this.nickName = nickName;
        this.commentedTo = commentedTo;
        this.profileImage = profileImage;
        this.content = content;
        this.createdAt = createdAt;
    }
}
