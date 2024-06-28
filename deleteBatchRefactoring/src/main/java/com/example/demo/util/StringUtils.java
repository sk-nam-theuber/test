package com.example.demo.util;

public class StringUtils {

    public static boolean isNullorEmpty(String text){
        if(text != null && !"".equals(text) && text.trim().length() > 0){
            return false;
        }
        return true;
    }
}
