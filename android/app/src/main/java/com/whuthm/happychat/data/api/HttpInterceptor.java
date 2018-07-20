package com.whuthm.happychat.data.api;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络请求拦截器，可设置统一的参数
 * 
 * Created by tanwei on 2018/7/20.
 */

public class HttpInterceptor implements Interceptor {
    
    private Map<String, String> headers;
    
    public HttpInterceptor() {
        
    }
    
    public HttpInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }
    
    @Override
    public Response intercept(Chain chain) throws IOException {
        
        Request.Builder builder = chain.request().newBuilder();
        if (headers != null && !headers.isEmpty()) {
            
            for (String key : headers.keySet()) {
                builder.addHeader(key, headers.get(key)).build();
            }
        }
        return chain.proceed(builder.build());
        
    }
}
