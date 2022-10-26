package com.example.demo.src.style.model.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class PostStyleCommentReq {
    private Long parentIdx;
    @NotNull
    private String content;

    public PostStyleCommentReq(Long parentIdx, String content) {
        this.parentIdx = parentIdx;
        this.content = content;
    }
}
