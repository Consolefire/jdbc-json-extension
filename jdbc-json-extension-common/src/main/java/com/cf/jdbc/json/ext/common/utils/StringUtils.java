package com.cf.jdbc.json.ext.common.utils;

public class StringUtils {

    public static boolean hasText(String text) {
        return null != text && !text.trim().isEmpty();
    }

    public static boolean nullOrEmpty(String text) {
        return null == text || text.trim().isEmpty();
    }

    public static boolean isEqual(String a, String b, boolean c) {
        if (null == a && null == b) {
            return true;
        }
        if (null == a || null == b) {
            return false;
        }
        if (c) {
            return a.equals(b);
        }
        return a.equalsIgnoreCase(b);
    };
}
