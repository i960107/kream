package com.example.demo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationRegex {
//    public static boolean isRegexEmail(String target) {
//        String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
//        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
//        Matcher matcher = pattern.matcher(target);
//        return matcher.find();
//    }
    public final static String regexNickName = "^[a-z_1-9]{4,10}$";//영문자 소문자,_,숫자 4~10자리
    public final static String regexPhone = "^\\d{10,11}$";//3-3(4)-4
    public final static String regexPassword = "^[a-z0-9!?@#$%^&*():;+-=~{}<>\\_\\[\\]\\|\\\\\\\"\\'\\,\\.\\/\\`\\₩]{8,12}$";//숫자영문자소문자특수문자 8~12자리
    public final static String regexBin = "^\\d{4}-\\d{4}-\\d{4}-\\d{4}$";//4-4-4-4
    public final static String regexBoolean = "^TRUE|FALSE$";
    public final static String location = "^HOME|PRODUCT|SHOP$";
}

