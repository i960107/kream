package com.example.demo.src.user.model.dto;

import com.example.demo.utils.ValidationRegex;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.LastModifiedDate;

import javax.annotation.RegEx;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class PatchUserAddressReq {
    @NotNull
    private Long addressIdx;

    @Length(max= 45)
    private String name;
    @Pattern(regexp = ValidationRegex.regexPhone)
    private String phone;
    private String zipCode;
    private String address;
    private String addressDetail;
    @NotBlank
    @Pattern(regexp = ValidationRegex.regexBoolean)
    private String defaultAddress;

    public PatchUserAddressReq(
            Long addressIdx,
            String name,
            String phone,
            String zipCode,
            String address,
            String addressDetail,
            String defaultAddress){
        this.addressIdx = addressIdx;
        this.name = name;
        this.phone = phone;
        this.zipCode = zipCode;
        this.address = address;
        this.addressDetail = addressDetail;
        this.defaultAddress = defaultAddress;
    }
}
