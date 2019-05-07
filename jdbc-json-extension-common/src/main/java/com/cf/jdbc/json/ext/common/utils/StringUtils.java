package com.cf.jdbc.json.ext.common.utils;

public class StringUtils {

    public static boolean hasText(String text) {
        return null != text && !text.trim().isEmpty();
    }

    public static boolean nullOrEmpty(String text) {
        return null == text || text.trim().isEmpty();
    }

}
