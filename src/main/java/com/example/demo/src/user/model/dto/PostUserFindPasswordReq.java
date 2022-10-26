package com.example.demo.src.user.model.dto;

import com.example.demo.utils.ValidationRegex;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class PostUserFindPasswordReq {
    @NotBlank
    @Pattern(regexp = ValidationRegex.regexPhone)
    private String phone;
    @NotBlank
    private String email;

    public PostUserFindPasswordReq(String phone, String email) {
        this.phone = phone;
        this.email = email;
    }
}
