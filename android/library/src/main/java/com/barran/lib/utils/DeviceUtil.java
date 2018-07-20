package com.barran.lib.utils;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.barran.lib.utils.log.Logs;

import java.util.UUID;

/**
 * 设备相关方法集
 *
 * Created by tanwei on 2017/10/21.
 */

public class DeviceUtil {
    
    private static final String TAG_RANDOM_UUID = "uuid_origin";
    private static final String TAG_IDENTIFICATION = "identification_origin";
    
    public static int getSDKInt() {
        return Build.VERSION.SDK_INT;
    }
    
    public static String getSDKVersion() {
        return Build.VERSION.SDK;
    }
    
    public static boolean isSDCardExists() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }
    
    public static String getAndroidID(Context context) {
        String val = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        
        return val;
    }
    
    public static String getDeviceID(Context context) {
        String val = ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        
        return val;
    }
    
    public static String getRandomUUID(Context context) {
        ContentResolver resolver = context.getContentResolver();
        String val = Settings.System.getString(resolver, TAG_RANDOM_UUID);
        if (TextUtils.isEmpty(val)) {
            generateRandomUUID(context);
            val = Settings.System.getString(resolver, TAG_RANDOM_UUID);
        }
        
        return val;
    }
    
    private static void generateRandomUUID(Context context) {
        ContentResolver resolver = context.getContentResolver();
        String uuid = UUID.randomUUID().toString();
        if (Settings.System.getString(resolver, TAG_RANDOM_UUID) == null)
            Settings.System.putString(resolver, TAG_RANDOM_UUID, uuid);
    }
    
    public static String getLine1Number(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getLine1Number();
        } catch (Exception e) {
            Logs.w(e.toString());
        }
        return "";
    }
    
    public static String getIMSI(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getSubscriberId();
        } catch (Exception e) {
            return "unknown";
        }
    }
    
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isAllowedUnknownSourceLevel17(Context context) {
        int nRet = Settings.Secure.getInt(context.getContentResolver(),
                Settings.Global.INSTALL_NON_MARKET_APPS, 0);
        return nRet == 1;
    }
    
    @SuppressWarnings("deprecation")
    public static boolean isAllowedUnknownSourceLevelDown(Context context) {
        int nRet = Settings.Secure.getInt(context.getContentResolver(),
                Settings.Secure.INSTALL_NON_MARKET_APPS, 0);
        return nRet == 1;
    }
    
    /**
     * 获得手机是否打开了“允许未知来源”的apk安装选项
     */
    public static boolean isAllowedUnknownSource(Context context) {
        if (getSDKInt() >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            return isAllowedUnknownSourceLevel17(context);
        else
            return isAllowedUnknownSourceLevelDown(context);
    }
    
}
