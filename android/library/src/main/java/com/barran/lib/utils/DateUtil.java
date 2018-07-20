package com.barran.lib.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * date formatter
 *
 * Created by tanwei on 2017/10/21.
 */

public class DateUtil {
    
    public static final String FORMAT_YMD_HMS = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YMD_HM = "yyyy-MM-dd日 HH:mm";
    public static final String FORMAT_YMD = "yyyy-MM-dd";
    public static final String FORMAT_MD_HM = "MM-dd HH:mm";
    public static final String FORMAT_MD = "MM-dd";
    public static final String FORMAT_HM = "HH:mm";
    
    public static final String FORMAT_YMD_HMS_CN = "yyyy年MM月dd日 HH:mm:ss";
    public static final String FORMAT_YMD_HM_CN = "yyyy年MM月dd日 HH:mm";
    public static final String FORMAT_YMD_CN = "yyyy年MM月dd日";
    public static final String FORMAT_MD_HM_CN = "MM月dd日 HH:mm";
    public static final String FORMAT_MD_CN = "MM月dd日";
    
    private static final SimpleDateFormat DATE_FORMAT_YMD_HMS = new SimpleDateFormat(
            FORMAT_YMD_HMS, Locale.CHINA);
    private static final SimpleDateFormat DATE_FORMAT_YMD_HM = new SimpleDateFormat(
            FORMAT_YMD_HM, Locale.CHINA);
    private static final SimpleDateFormat DATE_FORMAT_MD_HM = new SimpleDateFormat(
            FORMAT_MD_HM, Locale.CHINA);
    private static final SimpleDateFormat DATE_FORMAT_YMD = new SimpleDateFormat(
            FORMAT_YMD, Locale.CHINA);
    private static final SimpleDateFormat DATE_FORMAT_MD_ = new SimpleDateFormat(
            FORMAT_MD, Locale.CHINA);
    private static final SimpleDateFormat DATE_FORMAT_HM_ = new SimpleDateFormat(
            FORMAT_HM, Locale.CHINA);
    
    public static String formatDate(Date date) {
        return formatDate(date);
    }
    
    public static String formatDate(long timeMillis) {
        return formatDate(new Date(timeMillis));
    }
    
    public static String formatDate(long timeMillis, String formatter) {
        return formatDate(new Date(timeMillis), formatter);
    }
    
    public static String formatDate(Date date, String formatter) {
        if (TextUtils.isEmpty(formatter)) {
            formatter = FORMAT_YMD_HMS;
        }
        switch (formatter) {
            case FORMAT_YMD_HMS:
                return DATE_FORMAT_YMD_HMS.format(date);
            
            case FORMAT_YMD_HM:
                return DATE_FORMAT_YMD_HM.format(date);
            
            case FORMAT_MD_HM:
                return DATE_FORMAT_MD_HM.format(date);
            
            case FORMAT_YMD:
                return DATE_FORMAT_YMD.format(date);
            
            case FORMAT_MD:
                return DATE_FORMAT_MD_.format(date);
            
            case FORMAT_HM:
                return DATE_FORMAT_HM_.format(date);
            default:
                return null;
        }
        
    }
}
