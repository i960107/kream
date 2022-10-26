package com.example.demo.src.user.model.dto;

import com.example.demo.config.Status;
import com.example.demo.utils.ValidationRegex;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class PostUserReq {
    @NotNull
    @Email
    private String email;

    private String  profileImage;

    @NotNull
    @Pattern(regexp = ValidationRegex.regexPassword,message="숫자, 영문자 소문자, 특수문자 8~12자리")
    private String password;

    @NotNull
    @Pattern(regexp = ValidationRegex.regexPhone,message = "-없이 10,11자리")
    private String phone;

    @NotNull
    @Length(min = 1, max = 45)
    private String name;

    private Integer sneakersSize;

    private String introduction;

    private String grade = "일반 회원";
    private Long point = 0L;

    private Status status= Status.ACTIVATED;
}
