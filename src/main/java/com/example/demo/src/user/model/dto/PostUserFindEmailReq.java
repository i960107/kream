package com.example.demo.src.user.model.dto;

import com.example.demo.utils.ValidationRegex;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class PostUserFindEmailReq {
    @NotBlank
    @Pattern(regexp = ValidationRegex.regexPhone,message = "3-3(4)-4")
    private String phone;
}
