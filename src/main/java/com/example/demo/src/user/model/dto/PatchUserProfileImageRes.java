package com.example.demo.src.user.model.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class PatchUserProfileImageRes {
    @NotNull
    String profileImageUrl;

    public PatchUserProfileImageRes(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
