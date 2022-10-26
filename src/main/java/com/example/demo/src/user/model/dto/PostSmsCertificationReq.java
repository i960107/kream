package com.example.demo.src.user.model.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class PostSmsCertificationReq {
    @NotBlank
    private String phone;
    @NotBlank
    private String certificationNum;

    public PostSmsCertificationReq(String phone, String certificationNum) {
        this.phone = phone;
        this.certificationNum = certificationNum;
    }
}
