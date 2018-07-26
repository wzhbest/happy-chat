package com.whuthm.happychat.data.api;

import android.content.Context;

import com.whuthm.happychat.data.Constants;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.protobuf.ProtoConverterFactory;

/**
 * 封装retrofit
 * 
 * Created by tanwei on 2018/7/20.
 */

public class RetrofitClient {
    
    private static RetrofitClient sInstance;
    
    private Context context;
    
    private Retrofit mRetrofit;
    
    private OkHttpClient mOkHttpClient;
    
    private ApiService mApiService;
    
    private RetrofitClient(Context context) {
        this.context = context;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/x-protobuf");
        // TODO 添加token
        // headers.put("token", "");
        mOkHttpClient = builder
                .cache(new Cache(new File(context.getCacheDir().getAbsolutePath(),
                        Constants.HTTP_CACHE_DIR), Constants.HTTP_CACHE_SIZE))
                .addInterceptor(new HttpInterceptor(headers))
                .addNetworkInterceptor(new NetworkInterceptor(context))
                .connectTimeout(Constants.HTTP_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.HTTP_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectionPool(new ConnectionPool(3, 1, TimeUnit.MINUTES)).build();
        
        mRetrofit = new Retrofit.Builder().client(mOkHttpClient)
                .addConverterFactory(ProtoConverterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        
        mApiService = mRetrofit.create(ApiService.class);
    }
    
    public static void initRetrofit(Context context) {
        sInstance = new RetrofitClient(context);
    }
    
    public static ApiService api() {
        return sInstance.mApiService;
    }
    
    public static OkHttpClient okHttp() {
        return sInstance.mOkHttpClient;
    }
}
