package com.whuthm.happychat;

import android.app.Application;

import com.barran.lib.utils.log.Logs;
import com.whuthm.happychat.data.DBOperator;
import com.whuthm.happychat.data.api.RetrofitClient;

/**
 * 程序入口
 *
 * Created by tanwei on 2018/7/20.
 */

public class HappyApp extends Application {
    
    @Override
    public void onCreate() {
        initEnv();
        Logs.v("app onCreate");
        
        super.onCreate();
        
        initApp();
    }
    
    private void initEnv() {
        Logs.init(this);
        Logs.setDebug(true);
    }
    
    private void initApp() {
        RetrofitClient.initRetrofit(this);
        DBOperator.init(this);
    }
}
