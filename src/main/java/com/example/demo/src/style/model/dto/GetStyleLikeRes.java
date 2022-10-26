package com.example.demo.src.style.model.dto;

import lombok.Getter;

@Getter
public class GetStyleLikeRes {
    private Long styleLikeIdx;
    private Long userIdx;
    private String nickName;
    private String profileImage;
    private String name;

    public GetStyleLikeRes(Long styleLikeIdx, Long userIdx, String nickName, String profileImage, String name) {
        this.styleLikeIdx = styleLikeIdx;
        this.userIdx = userIdx;
        this.nickName = nickName;
        this.profileImage = profileImage;
        this.name = name;
    }
}
