package com.example.demo.src.user.model.dto;

import lombok.Getter;

@Getter
public class PatchUserRes {
    private Long idx;
    private String email;
    private String profileImage;
    private String nickName;
    private String name;
    private String introduction;
    private String grade;
    private Long point;

    public PatchUserRes(
            Long idx,
            String email,
            String profileImage,
            String nickName,
            String name,
            String introduction,
            String grade,
            Long point) {
        this.idx = idx;
        this.email = email;
        this.profileImage = profileImage;
        this.nickName = nickName;
        this.name = name;
        this.introduction = introduction;
        this.grade = grade;
        this.point = point;
    }
}
