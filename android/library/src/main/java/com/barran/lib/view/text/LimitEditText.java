package com.barran.lib.view.text;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.barran.lib.R;

/**
 * 封装针对EditText的统一处理逻辑
 *
 * <p>支持xml设置最大输入长度 maxLength</p>
 * <p>支持xml设置赛选类型 filterMode</p>
 *
 * Created by tanwei on 2018/1/10.
 */

public class LimitEditText extends AppCompatEditText {
    
    private LimitTextWatcher limitedTextWatcher;
    
    public LimitEditText(Context context) {
        super(context);
        
        initSetting();
    }
    
    public LimitEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        initSetting();
        
        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.LimitEditText);
        int max = array.getInt(R.styleable.LimitEditText_maxLength, -1);
        if (max > 0) {
            limitedTextWatcher.setMaxLength(max);
        }
        
        int mode = array.getInt(R.styleable.LimitEditText_filterMode, -1);
        if (mode >= 0) {
            limitedTextWatcher.setFilterMode(parseFilterMode(mode));
        }
        
        array.recycle();
    }
    
    protected void initSetting() {
        limitedTextWatcher = new LimitTextWatcher();
        super.addTextChangedListener(limitedTextWatcher);
    }
    
    /**
     * see
     * {@link LimitTextWatcher#setFilterMode(LimitTextWatcher.FilterMode)}
     *
     * @param mode
     *            see
     *            {@link com.barran.lib.view.text.LimitTextWatcher.FilterMode}
     *
     * @return this
     */
    public LimitEditText setFilterMode(LimitTextWatcher.FilterMode mode) {
        limitedTextWatcher.setFilterMode(mode);
        return this;
    }
    
    /**
     * see {@link LimitTextWatcher#setMaxLength(int)}
     * 
     * @param max
     *            最大输入长度
     * @return this
     */
    public LimitEditText setMaxLength(int max) {
        limitedTextWatcher.setMaxLength(max);
        return this;
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {

        // 支持设置null清除默认watcher
        if (watcher != limitedTextWatcher) {
            removeTextChangedListener(limitedTextWatcher);
        }

        if (watcher != null) {
            super.addTextChangedListener(watcher);
        }
    }
    
    private LimitTextWatcher.FilterMode parseFilterMode(int value) {
        switch (value) {
            case 0:
                return LimitTextWatcher.FilterMode.NUMBER;
            case 1:
                return LimitTextWatcher.FilterMode.A_Z_NUMBER;
            case 2:
                return LimitTextWatcher.FilterMode.EMAIL;
            case 3:
                return LimitTextWatcher.FilterMode.PASSWORD;
            case 4:
                return LimitTextWatcher.FilterMode.CHINESE_ENGLISH;
            case 5:
                return LimitTextWatcher.FilterMode.CHINESE_ENGLISH_NUMBER;
            case 6:
                return LimitTextWatcher.FilterMode.CHINESE_ENGLISH_SPACE_DOT;
            case 7:
                return LimitTextWatcher.FilterMode.NO_CHINESE;
            case 8:
                return LimitTextWatcher.FilterMode.NO_EMOJI;
            case 9:
                return LimitTextWatcher.FilterMode.NO_CHINESE_EMOJI;
            case 10:
                return LimitTextWatcher.FilterMode.NONE;
            
            default:
                return LimitTextWatcher.FilterMode.NO_EMOJI;
        }
    }
}
