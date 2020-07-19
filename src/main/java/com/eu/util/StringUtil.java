package com.eu.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author fuyangrong
 */
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
            fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        }
        return isEmpty(fileName) ? "workbook.xlsx" : fileName + ".xlsx";
    }
}
