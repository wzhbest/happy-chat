package com.barran.lib.ui.text;

import android.support.annotation.ColorInt;
import android.text.TextPaint;
import android.view.View;

/**
 * 可点击span
 * 
 * Created by tanwei on 2018/4/13.
 */

public class ClickableSpan extends android.text.style.ClickableSpan {
    
    private boolean needShowUnderLine;
    private int customClickColor;
    private boolean hasCustomClickColor;
    private ClickableSpanListener clickListener;
    private String clickContent;
    
    public interface ClickableSpanListener {
        void onClick(View v, String content);
    }
    
    @Override
    public void onClick(View widget) {
        if (clickListener != null) {
            clickListener.onClick(widget, clickContent);
        }
    }
    
    public ClickableSpan setShowUnderLine(boolean needShowUnderLine) {
        this.needShowUnderLine = needShowUnderLine;
        return this;
    }
    
    public ClickableSpan setClickColor(@ColorInt int color) {
        customClickColor = color;
        hasCustomClickColor = true;
        return this;
    }
    
    public ClickableSpan setClickContent(String content) {
        clickContent = content;
        return this;
    }
    
    public String getClickContent() {
        return clickContent;
    }
    
    public ClickableSpan setOnClickListener(ClickableSpanListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }
    
    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        if (hasCustomClickColor) {
            ds.setColor(customClickColor);
        }
        if (!needShowUnderLine) {
            ds.setUnderlineText(false);
        }
    }
}
