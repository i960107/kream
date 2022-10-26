package com.example.demo.src.user.model.dto;

import lombok.Getter;

@Getter
public class GetUserAddressRes {
    private Long idx;
    private String name;
    private String phone;
    private String zipCode;
    private String address;
    private String addressDetail;
    private String defaultAddress;

    public GetUserAddressRes(
            Long idx,
            String name,
            String phone,
            String zipCode,
            String address,
            String addressDetail,
            String defaultAddress) {
        this.idx = idx;
        this.name = name;
        this.phone = phone;
        this.zipCode =zipCode;
        this.address = address;
        this.addressDetail = addressDetail;
        this.defaultAddress = defaultAddress;
    }
}
