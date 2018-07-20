package com.barran.lib.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * 封装SharedPreferences操作
 *
 * Created by tanwei on 2017/10/20.
 */

public class SPWrapper {
    
    private SharedPreferences mSP;
    private static final String TAG = "SPWrapper";
    
    public SPWrapper(Context context, String spFileName) {
        mSP = context.getSharedPreferences(spFileName, Context.MODE_PRIVATE);
    }
    
    public boolean contains(final String key) {
        return mSP.contains(key);
    }
    
    public void put(String key, String value) {
        mSP.edit().putString(key, value).apply();
    }
    
    public void put(String key, int value) {
        mSP.edit().putInt(key, value).apply();
    }
    
    public void put(String key, long value) {
        mSP.edit().putLong(key, value).apply();
    }
    
    public void put(String key, boolean value) {
        mSP.edit().putBoolean(key, value).apply();
    }
    
    public String getString(String key) {
        return getString(key, null);
    }
    
    public String getString(String key, String defaultValue) {
        return mSP.getString(key, defaultValue);
    }
    
    public int getInt(String key) {
        return getInt(key, 0);
    }
    
    public int getInt(String key, int defaultValue) {
        return mSP.getInt(key, defaultValue);
    }
    
    public long getLong(String key) {
        return getLong(key, 0);
    }
    
    public long getLong(String key, long defaultValue) {
        return mSP.getLong(key, defaultValue);
    }
    
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }
    
    public boolean getBoolean(String key, boolean defaultValue) {
        return mSP.getBoolean(key, defaultValue);
    }
    
    public Set<String> getStringSet(String key, Set<String> defaultValue) {
        return mSP.getStringSet(key, defaultValue);
    }
    
    public void put(String key, Set<String> value) {
        remove(key);
        mSP.edit().putStringSet(key, value).apply();
    }
    
    public void remove(String key) {
        mSP.edit().remove(key).apply();
    }
    
    public void clear() {
        mSP.edit().clear().apply();
    }
}
