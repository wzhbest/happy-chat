package com.barran.lib.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.barran.lib.utils.log.Logs;

/**
 * 基类fragment,包括fragment周期事件的log和回调、返回事件处理等
 *
 * Created by tanwei on 2017/10/19.
 */

public abstract class BaseFragment extends Fragment {
    
    protected static String sTAG;
    
    protected FragListener mFragListener;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sTAG = getClass().getSimpleName();
        Logs.i(sTAG + ":onCreate");
    }
    
    @Override
    public void onStart() {
        super.onStart();
        Logs.i(sTAG + ":onStart");
        if (mFragListener != null) {
            mFragListener.onStart();
        }
    }
    
    @Override
    public void onStop() {
        super.onStop();
        Logs.i(sTAG + ":onStop");
        if (mFragListener != null) {
            mFragListener.onStop();
        }
    }
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mFragListener != null) {
            mFragListener.onAttach();
        }
    }
    
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Logs.i(sTAG + ":onHiddenChanged " + hidden);
        if (mFragListener != null) {
            mFragListener.onHiddenChanged(hidden);
        }
    }
    
    public void setTitle(int titleResId) {
        if (uiOperation()) {
            getActivity().setTitle(titleResId);
        }
    }
    
    public void setTitle(CharSequence title) {
        if (uiOperation()) {
            getActivity().setTitle(title);
        }
    }
    
    /**
     * 是否能执行ui操作
     *
     * @return 在生命周期内返回true
     */
    public boolean uiOperation() {
        return (getActivity() != null) && isAdded() && !isDetached();
    }
    
    /**
     * 子类覆写此方法处理返回事件
     *
     * @return 需要拦截返回事件返回true，默认返回false
     */
    public boolean onBackPressed() {
        return false;
    }
}
