package com.barran.lib.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by tanwei on 2017/10/21.
 */

public class PackageUtil {
    
    public static String getVersionName(Context context) {
        PackageInfo packInfo = getPackageInfo(context);
        if (packInfo != null) {
            return packInfo.versionName;
        }
        return "";
    }
    
    /**
     * 获取版本号
     */
    public static int getVersionCode(Context context) {
        PackageInfo packInfo = getPackageInfo(context);
        if (packInfo != null) {
            return packInfo.versionCode;
        }
        else
            return 0;
    }
    
    /**
     * 获取版本号
     */
    public static String getPackageName(Context context) {
        PackageInfo packInfo = getPackageInfo(context);
        if (packInfo != null) {
            return packInfo.packageName;
        }
        else
            return "";
    }
    
    /**
     * 获取 应用名称
     */
    public static String getLabel(Context context) {
        PackageInfo packInfo = getPackageInfo(context);
        if (packInfo != null) {
            return packInfo.applicationInfo.loadLabel(context.getPackageManager())
                    .toString();
        }
        else
            return "";
    }
    
    public static PackageInfo getPackageInfo(Context context) {
        
        PackageInfo sSelfPackageInfo = getPackageInfo(context, context.getPackageName());
        
        return sSelfPackageInfo;
    }
    
    /**
     * 根据包名直接返回PackageInfo
     *
     * @param packageName
     *            指定的包名 指定包名
     */
    public static PackageInfo getPackageInfo(Context context, String packageName) {
        try {
            return context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_ACTIVITIES);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static String getPackagerVersion(Context context, String packageName) {
        String lsv = "";
        PackageInfo packInfo = getPackageInfo(context, packageName);
        if (packInfo != null) {
            lsv = packInfo.versionName;
        }
        return lsv;
    }
}
