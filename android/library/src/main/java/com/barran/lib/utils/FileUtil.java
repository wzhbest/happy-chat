package com.barran.lib.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * File & path
 * 
 * Created by tanwei on 2017/10/21.
 */

public class FileUtil {
    
    public static File getExternalRootCacheFile(Context context) {
        if (DeviceUtil.isSDCardExists()) {
            File dir = context.getExternalCacheDir();
            if (dir == null) {
                dir = new File(Environment.getExternalStorageDirectory(),
                        "Android/data/" + context.getPackageName() + "/cache");
            }
            if (checkDir(dir)) {
                return dir;
            }
        }
        return getInternalRootCacheFile(context);
    }
    
    public static boolean checkDir(File dir) {
        if (dir != null) {
            // 这里在返回之前确保目录存在
            if (dir.exists() && dir.isDirectory())
                return true;
            else
                return dir.mkdirs();
        }
        return false;
    }
    
    public static File getInternalRootCacheFile(Context context) {
        return context.getCacheDir();
    }
}
