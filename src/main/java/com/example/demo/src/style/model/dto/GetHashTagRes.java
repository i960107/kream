package com.example.demo.src.style.model.dto;

import lombok.Getter;

@Getter
public class GetHashTagRes {
    private Long idx;
    private String name;

    public GetHashTagRes(Long idx, String name) {
        this.idx = idx;
        this.name = name;
    }
}
