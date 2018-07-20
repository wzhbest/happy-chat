package com.whuthm.happychat.data.api;

import android.content.Context;

import com.barran.lib.utils.NetworkUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络拦截器：实现请求缓存
 * 
 * Created by tanwei on 2018/7/20.
 */

public class NetworkInterceptor implements Interceptor {
    
    private Context context;
    
    public NetworkInterceptor(Context context) {
        this.context = context;
    }
    
    @Override
    public Response intercept(Chain chain) throws IOException {
        
        Request request = chain.request();
        
        if (!NetworkUtil.isNetworkAvailable(context)) {
            // 无网络下强制使用缓存，无论缓存是否过期,此时该请求实际上不会被发送出去。
            request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
        }
        
        Response response = chain.proceed(request);
        if (NetworkUtil.isNetworkAvailable(context)) {// 有网络情况下，根据请求接口的设置，配置缓存。
            // 这样在下次请求时，根据缓存决定是否真正发出请求。
            String cacheControl = request.cacheControl().toString();
            // 当然如果你想在有网络的情况下都直接走网络，那么只需要
            // 将其超时时间这是为0即可:String cacheControl="Cache-Control:public,max-age=0"
            return response.newBuilder().header("Cache-Control", cacheControl)
                    .removeHeader("Pragma").build();
        }
        else {// 无网络
            return response.newBuilder()
                    .header("Cache-Control", "public,only-if-cached,max-stale=360000")
                    .removeHeader("Pragma").build();
            
        }
    }
}
