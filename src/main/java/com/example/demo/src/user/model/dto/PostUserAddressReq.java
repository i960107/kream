package com.example.demo.src.user.model.dto;

import com.example.demo.utils.ValidationRegex;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class PostUserAddressReq {

    @NotBlank
    @Length(max = 45)
    private String name;

    @NotBlank
    @Pattern(regexp = ValidationRegex.regexPhone)
    private String phone;

    @NotBlank
    private String address;

    @NotBlank
    private String zipCode;

    @NotBlank
    private String addressDetail;

    @NotBlank
    @Pattern(regexp = ValidationRegex.regexBoolean)
    private String defaultAddress;

    public PostUserAddressReq(
            String name,
            String phone,
            String address,
            String zipCode,
            String addressDetail,
            String defaultAddress) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.zipCode = zipCode;
        this.addressDetail = addressDetail;
        this.defaultAddress = defaultAddress;
    }
}
