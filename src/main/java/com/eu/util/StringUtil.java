package com.eu.util;

public class StringUtil {

    public static boolean isEmpty(String str){
        boolean flag = false;
        if(str==null){
            flag = true;
        }else if(str.trim().isEmpty()){
            flag = true;
        }
        return flag;
    }
}
