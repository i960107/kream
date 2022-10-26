package com.example.demo.src.user.model.entity;

import com.example.demo.config.BaseTimeEntity;
import com.example.demo.config.Status;
import com.example.demo.src.user.model.dto.PatchUserAddressReq;
import com.example.demo.utils.ValidationRegex;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity()
@Table(name = "address")
@Getter
@NoArgsConstructor
public class Address extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @NotNull
    @Column(name = "userIdx", columnDefinition = "int unsigned", nullable = false)
    private Long userIdx;

    @NotBlank
    @Length(max = 45)
    @Column(name = "name", columnDefinition = "varchar(45)", nullable = false)
    private String name;

    @Pattern(regexp = ValidationRegex.regexPhone)
    @NotBlank
    @Column(name = "phone", columnDefinition = "varchar(13)", nullable = false)
    private String phone;

    @NotBlank
    @Column(name = "zipCode", columnDefinition = "varchar(10)", nullable = false)
    private String zipCode;

    @NotNull
    @Column(name = "address", columnDefinition = "varchar(80)", nullable = false)
    private String address;

    @NotNull
    @Column(name = "addressDetail", columnDefinition = "varchar(45)", nullable = false)
    private String addressDetail;

    @NotNull
    @Pattern(regexp = ValidationRegex.regexBoolean)
    @Column(name = "defaultAddress", columnDefinition = "varchar(5) default 'FALSE'", nullable = false)
    private String defaultAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(10) default 'ACTIVATED'", nullable = false)
    private Status status;

    @Builder
    public Address(
            Long idx,
            Long userIdx,
            String name,
            String phone,
            String zipCode,
            String address,
            String addressDetail,
            String defaultAddress,
            Status status) {
        this.idx = idx;
        this.userIdx = userIdx;
        this.name = name;
        this.phone = phone;
        this.zipCode =zipCode;
        this.address = address;
        this.addressDetail = addressDetail;
        this.defaultAddress = defaultAddress;
        this.status = status;
    }

    public Address updateAsDefaultAddress(){
        this.defaultAddress = "TRUE";
        return this;
    }
    public Address deleteFromDefaultAddress(){
        this. defaultAddress = "FALSE";
        return this;
    }
    public Address updateAddress(PatchUserAddressReq patchUserAddressReq){
        if(patchUserAddressReq.getName()!=null){
            this.name= patchUserAddressReq.getName();
        }
        if(patchUserAddressReq.getPhone()!=null){
            this.phone = patchUserAddressReq.getPhone();
        }
        if(patchUserAddressReq.getZipCode()!=null){
            this.zipCode = patchUserAddressReq.getZipCode();
        }
        if(patchUserAddressReq.getAddress()!=null){
            this.address = patchUserAddressReq.getAddress();
        }
        if(patchUserAddressReq.getAddressDetail()!=null){
            this.addressDetail = patchUserAddressReq.getAddressDetail();
        }
        return this;
    }
}