package com.barran.lib.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * display相关参数
 *
 * Created by tanwei on 2017/10/21.
 */

public class DisplayUtil {
    
    private static DisplayMetrics sDisplayMetrics = null;
    private static float sDensity = -1.0f;
    private static float sScaleDensity;
    private static int sPixelHeight;
    private static int sPixelWidth;
    
    public synchronized static void init(Context con) {
        if (sDisplayMetrics == null) {
            sDisplayMetrics = new DisplayMetrics();
            
            ((WindowManager) con.getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay().getMetrics(sDisplayMetrics);
            
            sDensity = sDisplayMetrics.density;
            sScaleDensity = sDisplayMetrics.scaledDensity;
            sPixelHeight = sDisplayMetrics.heightPixels;
            sPixelWidth = sDisplayMetrics.widthPixels;
        }
    }
    
    public static int getScreenHeight() {
        return sPixelHeight;
    }
    
    public static int getScreenWidth() {
        return sPixelWidth;
    }
    
    public static float getScreenDensity() {
        return sDensity;
    }
    
    public static int dp2px(float dp) {
        return (int) (dp * sDensity + 0.5f);
    }
    
    public static int sp2px(float spValue) {
        return (int) (spValue * sScaleDensity + 0.5f);
    }
}
