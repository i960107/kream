package com.example.demo.src.style.model.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Getter
public class PostStyleReq {
    private List<Long> products;
    private String content;

}
