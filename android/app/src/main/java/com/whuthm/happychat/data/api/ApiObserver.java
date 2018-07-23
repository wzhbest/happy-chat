package com.whuthm.happychat.data.api;

import android.content.Context;
import android.widget.Toast;

import com.barran.lib.utils.log.Logs;
import com.google.protobuf.MessageLite;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * api 接口返回基类，处理部分通用错误
 *
 * Created by tanwei on 2018/7/23.
 */

public abstract class ApiObserver<T extends MessageLite> implements Observer<T> {
    
    protected Context mContext;
    
    public ApiObserver(Context context) {
        mContext = context;
    }
    
    private Disposable mDisposable;
    
    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
    }
    
    @Override
    public void onNext(T value) {
        
    }
    
    @Override
    public void onError(Throwable e) {
        Logs.w("api", e.getClass().getName() + ":" + e.getMessage());
        if (e instanceof SocketTimeoutException) {
            Toast.makeText(mContext, "请求超时,请稍后重试", Toast.LENGTH_SHORT).show();
        }
        else if (e instanceof ConnectException) {
            Toast.makeText(mContext, "请求出错,请稍后重试", Toast.LENGTH_SHORT).show();
        }
        else if (e instanceof HttpException) {
            Toast.makeText(mContext, "请求出错,请稍后重试", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(mContext, "请求出错,请稍后重试或者检查网络状态", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public void onComplete() {
        
    }
}
