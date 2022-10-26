package com.example.demo.src.style.model.dto;

import com.example.demo.config.Status;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostStyleLikeRes {
    private Long styleLikeIdx;
    private Long userIdx;
    private Long styleIdx;
    private Status status;

    public PostStyleLikeRes(Long styleLikeIdx, Long userIdx, Long styleIdx, Status status) {
        this.styleLikeIdx = styleLikeIdx;
        this.userIdx = userIdx;
        this.styleIdx = styleIdx;
        this.status = status;
    }
}
