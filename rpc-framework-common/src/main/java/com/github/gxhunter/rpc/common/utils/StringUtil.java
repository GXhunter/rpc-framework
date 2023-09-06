package com.github.gxhunter.rpc.common.utils;

/**
 * String 工具类
 *
 * @author hunter
 * 
 */
public class StringUtil {

    public static boolean isBlank(String s) {
        if (s == null || s.isEmpty()) {
            return true;
        }
        for (int i = 0; i < s.length(); ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
