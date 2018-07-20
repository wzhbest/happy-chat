package com.barran.lib.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.barran.lib.R;
import com.barran.lib.utils.log.Logs;

import java.lang.ref.WeakReference;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

/**
 * Fragment管理
 * <p>
 * Created by tanwei on 2017/10/21.
 */

public class FragmentStack {
    
    public enum FragOPMode {
        MODE_REPLACE, // replace
        MODE_SWITCH// hide,show
    }
    
    private static final String TAG_FRAG_STACK = "FragmentStack";
    
    private static final String TAG_BOTTOM = "STACK_BOTTOM";
    private static final String TAG_BACK_STACK_PREFIX = "BACK_STACK_";
    
    private FragmentManager mFragMgr;
    private int mGroupID;
    private Stack<WeakReference<BaseFragment>> mFragStack;
    private FragOPMode mFragOPMode = FragOPMode.MODE_REPLACE;
    private long mLastOpTime;
    private WeakReference<?> mUIObjectRef;
    
    // 如果要查看Fragment管理的详细操作信息，可以打开此开关
    // static {
    // FragmentManager.enableDebugLogging(true);
    // }
    
    public FragmentStack(FragmentActivity activity, int groupID) {
        this(activity, groupID, FragOPMode.MODE_REPLACE);
    }
    
    public FragmentStack(BaseFragment fragment, int groupID) {
        this(fragment, groupID, FragOPMode.MODE_REPLACE);
    }
    
    public FragmentStack(Object object, int groupID, FragOPMode opMode) {
        mUIObjectRef = new WeakReference<>(object);
        prepareFragMgr();
        mGroupID = groupID;
        mFragOPMode = opMode;
        mFragStack = new Stack<>();
        FragmentManager.OnBackStackChangedListener stackListener = new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Logs.d(TAG_FRAG_STACK, "cur fragment count = " + getFragmentsCount()
                        + "---cur back stack count=" + mFragMgr.getBackStackEntryCount());
            }
        };
        
        mFragMgr.addOnBackStackChangedListener(stackListener);
    }
    
    private void prepareFragMgr() {
        if (mUIObjectRef != null && mUIObjectRef.get() != null) {
            if (mUIObjectRef.get() instanceof FragmentActivity) {
                mFragMgr = ((FragmentActivity) mUIObjectRef.get())
                        .getSupportFragmentManager();
            }
            else if (mUIObjectRef.get() instanceof BaseFragment) {
                mFragMgr = ((BaseFragment) mUIObjectRef.get()).getChildFragmentManager();
            }
            else {
                throw new IllegalArgumentException("error ui object");
            }
        }
    }
    
    public FragmentStack setGroupID(int groupID) {
        mGroupID = groupID;
        return this;
    }
    
    public FragmentStack setFragOPMode(FragOPMode mode) {
        mFragOPMode = mode;
        return this;
    }
    
    private void checkReplaceMode() {
        if (mFragOPMode != FragOPMode.MODE_REPLACE) {
            throw new IllegalArgumentException(
                    "call setFragOPMode(FragOPMode.MODE_REPLACE)");
        }
    }
    
    private void checkSwitchMode() {
        if (mFragOPMode != FragOPMode.MODE_SWITCH) {
            throw new IllegalArgumentException(
                    "call setFragOPMode(FragOPMode.MODE_SWITCH)");
        }
    }
    
    private boolean isUIValid() {
        return mUIObjectRef != null && mUIObjectRef.get() != null
                && ((mUIObjectRef.get() instanceof BaseActivity
                        && ((BaseActivity) mUIObjectRef.get()).uiOperation())
                        || (mUIObjectRef.get() instanceof BaseFragment
                                && ((BaseFragment) mUIObjectRef.get()).uiOperation()));
    }
    
    /**
     * 设置栈底fragment
     */
    public void setBottom(BaseFragment fragment) {
        
        checkReplaceMode();
        FragmentTransaction transaction = mFragMgr.beginTransaction();
        transaction.replace(mGroupID, fragment, TAG_BOTTOM);
        transaction.commitAllowingStateLoss();
        
        mFragStack.clear();
        mFragStack.push(new WeakReference<>(fragment));
    }
    
    public int getFragmentsCount() {
        return mFragStack.size();
    }
    
    public BaseFragment getTopFragment() {
        
        try {
            WeakReference<BaseFragment> fragRef = mFragStack.peek();
            if (fragRef != null) {
                return fragRef.get();
            }
        } catch (EmptyStackException e) {
            return null;
        }
        
        return null;
    }
    
    public int getBackStackCount() {
        return mFragMgr.getBackStackEntryCount();
    }
    
    public void push(BaseFragment fragment) {
        push(fragment, true);
    }
    
    public void push(BaseFragment fragment, boolean needReplace) {
        push(fragment, needReplace, true);
    }
    
    public void push(BaseFragment fragment, boolean needReplace, boolean needAni) {
        
        if (!isUIValid())
            return;
        
        checkReplaceMode();
        int count = getFragmentsCount();
        
        // set bottom fragment first
        if (count <= 0) {
            Logs.d(TAG_FRAG_STACK, "setBottom first : frag=" + fragment);
            setBottom(fragment);
            return;
        }
        
        // error
        Fragment frag = getTopFragment();
        if (frag == null) {
            Logs.w(TAG_FRAG_STACK, "error: top is null");
            return;
        }
        
        // 如果fragment 已添加，则忽略
        if (isFragExist(fragment)) {
            Logs.w(TAG_FRAG_STACK, "fragment: " + fragment + " exists");
            return;
        }
        
        // time limit: 300ms
        long curTime = System.currentTimeMillis();
        if (curTime - mLastOpTime > 0 && curTime - mLastOpTime < 300) {
            return;
        }
        
        mLastOpTime = curTime;
        
        FragmentTransaction transaction = mFragMgr.beginTransaction();
        if (needAni) {
            transaction.setCustomAnimations(R.anim.slide_alpha_in_from_right,
                    R.anim.slide_alpha_out_to_left, R.anim.slide_alpha_in_from_left,
                    R.anim.slide_alpha_out_to_right);
        }
        
        if (!needReplace) {
            transaction.hide(frag);
            transaction.add(mGroupID, fragment);
        }
        else {
            transaction.replace(mGroupID, fragment);
        }
        transaction.addToBackStack(TAG_BACK_STACK_PREFIX + count);
        transaction.commitAllowingStateLoss();
        mFragStack.push(new WeakReference<>(fragment));
    }
    
    /**
     * 替换栈顶的fragment，不记录
     */
    public void replace(BaseFragment fragment) {
        
        checkReplaceMode();
        int count = getFragmentsCount();
        
        if (count <= 0) {
            throw new IllegalStateException("call setBottom first");
        }
        
        mFragStack.pop();
        mFragStack.push(new WeakReference<>(fragment));
        FragmentTransaction transaction = mFragMgr.beginTransaction();
        transaction.replace(mGroupID, fragment);
        transaction.commitAllowingStateLoss();
    }
    
    /**
     * 弹出顶部的fragment
     */
    public boolean pop() {
        checkReplaceMode();
        if (getBackStackCount() > 0) {
            if (getFragmentsCount() > 0)
                mFragStack.pop();
            mFragMgr.popBackStack();
            return true;
        }
        
        return false;
    }
    
    private void hideCurrentSwitchedFragment(FragmentTransaction transaction,
            Fragment exceptFrag) {
        List<Fragment> fragList = mFragMgr.getFragments();
        if (fragList != null && !fragList.isEmpty()) {
            for (Fragment frag : fragList) {
                if (exceptFrag != frag && frag != null) {
                    if (!frag.isHidden())
                        transaction.hide(frag);
                }
            }
        }
    }
    
    /**
     * 基于MODE_SWITCH，事先添加fragment，默认为hide
     *
     * @param fragment
     *            想要切换的fragment
     */
    public void addForSwitch(BaseFragment fragment) {
        Logs.d(TAG_FRAG_STACK, "addForSwitch  " + fragment);
        FragmentTransaction transaction = mFragMgr.beginTransaction();
        if (addForSwitch(transaction, fragment)) {
            transaction.commitAllowingStateLoss();
        }
    }
    
    private boolean isFragExist(BaseFragment fragment) {
        List<Fragment> fragList = mFragMgr.getFragments();
        if (fragList != null && !fragList.isEmpty()) {
            for (Fragment frag : fragList) {
                if (frag != null && fragment == frag) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private boolean addForSwitch(FragmentTransaction transaction, BaseFragment fragment) {
        
        if (!isUIValid())
            return false;
        
        checkSwitchMode();
        boolean exist = isFragExist(fragment);
        if (!exist) {
            transaction.add(mGroupID, fragment).hide(fragment);
            return true;
        }
        return false;
    }
    
    /**
     * 切换到某个fragment
     *
     * @param fragment
     *            fragment
     * @return 切换结果，true成功
     */
    public boolean switchTo(BaseFragment fragment) {
        
        if (!isUIValid())
            return false;
        
        checkSwitchMode();
        
        boolean exist = isFragExist(fragment);
        if (exist) {
            FragmentTransaction transaction = mFragMgr.beginTransaction();
            hideCurrentSwitchedFragment(transaction, fragment);
            transaction.show(fragment).commitAllowingStateLoss();
            
            // switch to top
            for (WeakReference ref : mFragStack) {
                if (ref.get() == fragment) {
                    mFragStack.remove(ref);
                    break;
                }
            }
            mFragStack.push(new WeakReference<>(fragment));
        }
        else {
            
            // 添加时间限制：300ms
            long curTime = System.currentTimeMillis();
            if (curTime - mLastOpTime > 0 && curTime - mLastOpTime < 300) {
                return false;
            }
            mLastOpTime = curTime;
            
            // 忽略已经添加过了
            for (WeakReference ref : mFragStack) {
                if (ref.get() == fragment) {
                    return false;
                }
            }
            FragmentTransaction transaction = mFragMgr.beginTransaction();
            hideCurrentSwitchedFragment(transaction, null);
            transaction.add(mGroupID, fragment).commitAllowingStateLoss();
            
            mFragStack.push(new WeakReference<>(fragment));
        }
        return true;
    }
    
    /**
     * 退出到指定fragment
     */
    public void popTillMatch(Class<? extends BaseFragment> frag) {
        checkReplaceMode();
        while (mFragStack.size() > 1) {
            WeakReference<BaseFragment> reference = mFragStack.peek();
            if (reference != null && reference.get().getClass().equals(frag)) {
                break;
            }
            else {
                mFragStack.pop();
                mFragMgr.popBackStackImmediate();
            }
        }
    }
    
}
