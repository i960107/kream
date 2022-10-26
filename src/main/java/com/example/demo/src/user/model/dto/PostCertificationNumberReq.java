package com.example.demo.src.user.model.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class PostCertificationNumberReq {
    @NotBlank
    private String phone;

}
