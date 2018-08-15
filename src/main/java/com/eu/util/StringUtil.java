package com.eu.util;

import java.net.URLEncoder;

public class StringUtil {

    public static boolean isEmpty(String str) {
        boolean flag = false;
        if (str == null) {
            flag = true;
        } else if (str.trim().isEmpty()) {
            flag = true;
        }
        return flag;
    }

    public static String encodeFileName(String fileName, String userAgent) throws Exception{
        if (userAgent.contains("msie") || userAgent.contains("like gecko") ) {
            // win10 ie edge 浏览器 和其他系统的ie
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } else {
            // 谷歌、火狐
            fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
        }
        return isEmpty(fileName) ? "workbook.xlsx" : fileName + ".xlsx";
    }
}
