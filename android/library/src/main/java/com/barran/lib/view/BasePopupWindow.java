package com.barran.lib.view;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * 修正在7.0上显示位置异常
 *
 * Created by tanwei on 2017/10/21.
 */

public class BasePopupWindow extends PopupWindow {
    
    public BasePopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }
    
    public BasePopupWindow(View contentView, int width, int height) {
        this(contentView, width, height, false);
    }
    
    public void showAsDropDown(Activity activity, View anchor) {
        if (Build.VERSION.SDK_INT < 24) {
            super.showAsDropDown(anchor);
        }
        else {
            int[] a = new int[2];
            anchor.getLocationInWindow(a);
            int offsetY = a[1] + anchor.getHeight();
            if (Build.VERSION.SDK_INT >= 25) {
                // 【note!】Gets the screen height without the virtual key
                WindowManager wm = (WindowManager) activity
                        .getSystemService(Context.WINDOW_SERVICE);
                int screenHeight = wm.getDefaultDisplay().getHeight();
                /*
                 * PopupWindow height for match_parent, will occupy the entire
                 * screen, it needs to do special treatment in Android 7.1
                 */
                if (Build.VERSION.SDK_INT >= 26) {
                    setHeight(screenHeight - offsetY * 2);
                }
                else {
                    setHeight(screenHeight - offsetY);
                }
            }
            
            showAtLocation(activity.getWindow().getDecorView(), Gravity.NO_GRAVITY, 0,
                    offsetY);
        }
    }
}
