package com.example.demo.src.user.model.dto;

import com.example.demo.utils.ValidationRegex;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.parameters.P;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;

@Getter
public class PatchUserReq {

    @Pattern(regexp = ValidationRegex.regexNickName)
    String nickName;
    @Length(max=45)
    String name;
    @Length(max =100)
    String introduction;
}
