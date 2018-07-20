package com.barran.lib.utils;

import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.PermissionChecker;

/**
 * Permission
 *
 * Created by tanwei on 2017/10/21.
 */

public class PermissionUtil {
    
    public static boolean hasLocationPermission(Context context) {
        return hasPermission(context, "android.permission.ACCESS_FINE_LOCATION");
    }
    
    public static boolean hasCallPhonePermission(Context context) {
        return hasPermission(context, "android.permission.CALL_PHONE")
                && hasPermission(context, "android.permission.PROCESS_OUTGOING_CALLS");
    }
    
    public static boolean hasPermission(Context context, String permissionName) {
        try {
            int permission = PermissionChecker.checkSelfPermission(context,
                    permissionName);
            return PermissionChecker.PERMISSION_GRANTED == permission;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static boolean hasPermission(Context context, String packageName,
            String permissionName) {
        return context.getPackageManager().checkPermission(permissionName,
                packageName) >= 0;
    }
    
    /**
     * 获取是否有通知栏权限
     * 
     * @return >= 19 才能正常工作，19以前会返回true
     */
    public static boolean checkNotificationPermission(Context context) {
        return NotificationManagerCompat.from(context).areNotificationsEnabled();
    }
}
