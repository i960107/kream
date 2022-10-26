package com.example.demo.config;

import lombok.Getter;

import java.util.Map;

@Getter
public class GetErrorRes {
    private Map<String, String> errors;

    public GetErrorRes(Map<String, String> errors) {
        this.errors = errors;
    }
}
