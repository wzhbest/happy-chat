package com.barran.lib.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.barran.lib.utils.log.Logs;

/**
 * Activity基类，封装fragment管理
 *
 * Created by tanwei on 2017/10/19.
 */

public abstract class BaseActivity extends AppCompatActivity {
    
    protected static String sTAG;
    
    private boolean destroyed;
    
    protected FragmentStack mFragmentStack;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sTAG = getClass().getSimpleName();
        Logs.i(sTAG + ":onCreate");
        
        mFragmentStack = new FragmentStack(this, 0);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        Logs.i(sTAG + ":onStart");
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        Logs.i(sTAG + ":onStop");
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyed = true;
    }
    
    /**
     * 是否能执行ui操作
     *
     * @return 在生命周期内返回true
     */
    public boolean uiOperation() {
        return !destroyed;
    }
    
    public void setFragGroupID(int id) {
        mFragmentStack.setGroupID(id);
    }
    
    public void setFragBottom(BaseFragment frag) {
        mFragmentStack.setBottom(frag);
    }
    
    @Override
    public void onBackPressed() {
        boolean ret = false;
        BaseFragment frag = mFragmentStack.getTopFragment();
        if (frag != null) {
            ret = frag.onBackPressed();
        }
        
        if (!ret) {
            final int backEntryCount = mFragmentStack.getBackStackCount();
            if (backEntryCount > 0) {
                mFragmentStack.pop();
            }
            else {
                super.onBackPressed();
            }
        }
    }
}
