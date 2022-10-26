package com.example.demo.src.user.model.dto;

import com.example.demo.utils.ValidationRegex;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class GetUserCardRes {
    private Long idx;
    @NotBlank
    private String company;

    @NotBlank
    private String bin;

    @NotBlank
    @Pattern(regexp = ValidationRegex.regexBoolean)
    private String defaultCard;

    public GetUserCardRes(Long idx,String company, String bin, String defaultCard) {
        this.idx = idx;
        this.company = company;
        this.bin = bin;
        this.defaultCard = defaultCard;
    }
}
