package com.barran.lib.ui.text;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Px;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

/**
 * Created by tanwei on 2018/4/13.
 */

public class SpanUtils {
    
    public static ClickableSpan createClickableSpan(
            ClickableSpan.ClickableSpanListener listener, String content) {
        return createClickableSpan(listener, content, 0);
    }
    
    public static ClickableSpan createClickableSpan(
            ClickableSpan.ClickableSpanListener listener, String content,
            @ColorInt int color) {
        ClickableSpan clickableSpan = new ClickableSpan();
        clickableSpan.setOnClickListener(listener).setClickContent(content);
        if (color != 0) {
            clickableSpan.setClickColor(color);
        }
        return clickableSpan;
    }
    
    public static ImageSpan createImageSpan(Context context,
            @DrawableRes int imageResId) {
        return new ImageSpan(context, imageResId, DynamicDrawableSpan.ALIGN_BASELINE);
    }
    
    public static AbsoluteSizeSpan createFontSizeSpan(@Px int pxSize) {
        return new AbsoluteSizeSpan(pxSize);
    }
    
    public static ForegroundColorSpan createFGColorSpan(@ColorInt int color) {
        return new ForegroundColorSpan(color);
    }
    
    public static Spannable setSpan(Spannable string, Object span, int start, int end) {
        if (string != null && span != null) {
            string.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        return string;
    }
    
    public static SpannableString getSpanColorString(String str, @ColorInt int color,
            int start, int end) {
        SpannableString spanString = new SpannableString(str);
        ForegroundColorSpan span = createFGColorSpan(color);
        setSpan(spanString, span, start, end);
        return spanString;
    }
    
    public static void setClickSpan(TextView textView, Spannable spannable) {
        
        textView.setText(spannable, TextView.BufferType.SPANNABLE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
    
    /**
     * 设置指定内容高亮部分关键字（搜索高亮）
     *
     * @param str
     *            指定内容（显示文本）
     * @param highLight
     *            需要高亮的关键字
     * @param color
     *            高亮的颜色
     * @param start
     *            起始位置，从此位置之后开始高亮
     * @param matchWholeWord
     *            是否全词匹配，true 时只有全词匹配才高亮，false 时逐字匹配高亮
     *
     * @return 高亮后的文本
     */
    public static SpannableString setContentHighlight(SpannableString str,
            String highLight, @ColorInt int color, int start, boolean matchWholeWord) {
        if (matchWholeWord) {
            return setWholeWordHighlight(str, highLight.trim(), color, start);
        }
        else {
            return setEachCharHighlight(str, highLight.trim(), color, start);
        }
    }
    
    private static SpannableString setWholeWordHighlight(SpannableString content,
            String highLight, @ColorInt int color, int start) {
        if (start >= content.length()) {
            return content;
        }
        
        int index = content.toString().indexOf(highLight, start);
        if (index >= 0) {
            content = (SpannableString) setSpan(content, createFGColorSpan(color), index,
                    index + highLight.length());
            return setEachCharHighlight(content, highLight, color,
                    index + highLight.length());
        }
        else {
            return content;
        }
    }
    
    private static SpannableString setEachCharHighlight(SpannableString content,
            String highLight, @ColorInt int color, int start) {
        if (start >= content.length()) {
            return content;
        }
        
        int firstIndex = Integer.MAX_VALUE;
        for (int i = 0; i < highLight.length(); i++) {
            int index = content.toString().indexOf(highLight.charAt(i), start);
            if (index >= 0) {
                content = (SpannableString) setSpan(content, createFGColorSpan(color),
                        index, index + 1);
                
                if (firstIndex > index) {
                    firstIndex = index;
                }
            }
        }
        
        if (firstIndex < content.length()) {
            return setEachCharHighlight(content, highLight, color, firstIndex + 1);
        }
        else {
            return content;
        }
    }
}
