package com.example.demo.config;


import com.example.demo.config.Role;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Auth {
    public Role role();
}
