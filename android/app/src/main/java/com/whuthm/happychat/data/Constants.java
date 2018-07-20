package com.whuthm.happychat.data;

/**
 * 常量配置
 * 
 * Created by tanwei on 2018/7/19.
 */

public class Constants {
    
    public static final int HTTP_TIMEOUT = 10;// unit: second
    
    public static final String BASE_URL = "http://www.whuthm.com";
    
    public static final String HTTP_CACHE_DIR = "http_cache";
    
    public static final int HTTP_CACHE_SIZE = 5 * 1024 * 1024;
    
    public static enum ConversationType {
        SINGLE, GROUP,
    }
    
}
