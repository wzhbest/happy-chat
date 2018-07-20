package com.barran.lib.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String,Pattern...
 *
 * Created by tanwei on 2017/10/21.
 */

public class StringUtil {
    
    private static final String PATTERN_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
    private static final String PATTERN_CHAR_NUMBER = "[a-zA-Z0-9]+";
    private static final String PATTERN_NUMBER = "[0-9]+";
    private static final String PATTERN_CHINESE = "[\u4E00-\u9FA5]+";
    private static final String PATTERN_CHINESE_ENGLISH = "[a-zA-Z\u4E00-\u9FA5]+";
    private static final String PATTERN_CHINESE_ENGLISH_NUMBER = "[0-9a-zA-Z\u4E00-\u9FA5]+";
    private static final String PATTERN_CHINESE_ENGLISH_SPACE_DOT = "[a-zA-Z\u4E00-\u9FA5. ]+";
    private static final String PATTERN_PASSWORD = "[a-zA-Z0-9]+";
    private static final String PATTERN_CAR_NUMBER = "[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}";
    
    public static boolean checkEmail(String string) {
        return checkPattern(string, PATTERN_EMAIL);
    }
    
    public static boolean checkCharAndNumber(String string) {
        return checkPattern(string, PATTERN_CHAR_NUMBER);
    }
    
    public static boolean checkNumber(String string) {
        return checkPattern(string, PATTERN_NUMBER);
    }
    
    public static boolean checkChineseEnglish(String string) {
        return checkPattern(string, PATTERN_CHINESE_ENGLISH);
    }
    
    public static boolean checkChineseEnglishNumber(String string) {
        return checkPattern(string, PATTERN_CHINESE_ENGLISH_NUMBER);
    }
    
    public static boolean checkChineseEnglishSpaceDot(String string) {
        return checkPattern(string, PATTERN_CHINESE_ENGLISH_SPACE_DOT);
    }
    
    /**
     * 检验是否满足密码格式：数字+字母
     */
    public static boolean checkPassword(String string) {
        return checkPattern(string, PATTERN_PASSWORD);
    }
    
    public static boolean checkCarNumber(String string) {
        // 车牌号格式：汉字 + A-Z + 5位A-Z或0-9
        // （只包括了普通车牌号，教练车和部分部队车等车牌号不包括在内）
        return checkPattern(string, PATTERN_CAR_NUMBER);
    }
    
    private static boolean checkPattern(String string, String pattern) {
        Pattern _pattern = Pattern.compile(pattern);
        Matcher matcher = _pattern.matcher(string);
        return matcher.matches();
    }
    
    public static boolean containChinese(String str) {
        Pattern p = Pattern.compile(PATTERN_CHINESE);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static boolean containEmoji(String string) {
        int len = string.length();
        for (int i = 0; i < len; i++) {
            char codePoint = string.charAt(i);
            if (!isEmoji(codePoint)) { // 如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint
     *            比较的单个字符
     * @return
     */
    private static boolean isEmoji(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    private static boolean isUtf8GraphChar(char codePoint) {
        return ((codePoint >= 0x245F) && (codePoint <= 0x25E5))// utf8图形字符
                || ((codePoint >= 0x2600) && (codePoint <= 0x26FF));// utf8图形字符
    }

    public static boolean containUtf8GraphChar(String string) {
        int len = string.length();
        for (int i = 0; i < len; i++) {
            char codePoint = string.charAt(i);
            if (isUtf8GraphChar(codePoint)) { // 如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }
}
