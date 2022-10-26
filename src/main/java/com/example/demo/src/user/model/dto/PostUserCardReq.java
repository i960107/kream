package com.example.demo.src.user.model.dto;

import com.example.demo.utils.ValidationRegex;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class PostUserCardReq {

    @NotBlank
    private String company;
    @NotBlank
    @Pattern(regexp = ValidationRegex.regexBin)
    private String bin;
    @NotBlank
    @Pattern(regexp = ValidationRegex.regexBoolean)
    private String defaultCard;

    public PostUserCardReq(String company, String bin, String defaultCard) {
        this.company = company;
        this.bin = bin;
        this.defaultCard = defaultCard;
    }
}
