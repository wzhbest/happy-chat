package com.barran.lib.utils;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.text.Layout;
import android.text.TextPaint;
import android.view.View;
import android.widget.TextView;

/**
 * text: TextView,SpannableString...
 *
 * Created by tanwei on 2017/10/21.
 */

public class TextUtil {
    
    /**
     * 判断 TextView 的内容是否超过指定行数
     */
    public static boolean isMoreThanLines(TextView textView, int targetLine) {
        Layout l = textView.getLayout();
        if (l != null) {
            if (targetLine > 0) {
                try {
                    if (l.getEllipsisCount(targetLine - 1) > 0) {
                        return true;
                    }
                    else {
                        return false;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    return false;
                }
            }
        }
        return false;
    }
    
    /**
     * 根据 TextView 的最大宽度，计算可显示下的最大字体大小
     *
     * @param maxWidth
     *            pixel
     * @return pixel
     */
    public static float getMaxTextSizeWithinMaxWidth(TextView textView, int maxWidth) {
        Paint textPaint = textView.getPaint();
        
        if (textView.getText() == null) {
            return textPaint.getTextSize();
        }
        
        String text = textView.getText().toString();
        
        float textViewWidth = textPaint.measureText(text);
        
        float textSize = textPaint.getTextSize();
        if (textViewWidth > maxWidth) {
            textSize = getMaxTextSize(maxWidth, text, textPaint, 0f,
                    textPaint.getTextSize());
        }
        return textSize;
    }
    
    private static float getMaxTextSize(int maxWidth, String text, Paint paint,
            float start, float end) {
        if (Math.abs(start - end) <= 1) {
            return start;
        }
        
        paint.setTextSize((start + end) / 2);
        float halfWidth = paint.measureText(text);
        
        float maxSize;
        
        // 二分查找
        if (maxWidth < halfWidth) {
            maxSize = getMaxTextSize(maxWidth, text, paint, start, (start + end) / 2);
        }
        else {
            maxSize = getMaxTextSize(maxWidth, text, paint, (start + end) / 2, end);
        }
        return maxSize;
    }
    
    public static ClickableSpan createClickableSpan(Context context,
            ClickableSpan.ClickableSpanListener listener, String content) {
        return createClickableSpan(context, listener, content, 0);
    }
    
    public static ClickableSpan createClickableSpan(Context context,
            ClickableSpan.ClickableSpanListener listener, String content,
            @ColorRes int color) {
        ClickableSpan clickableSpan = new ClickableSpan(context);
        clickableSpan.setOnClickListener(listener).setClickContent(content);
        if (color != 0) {
            clickableSpan.setClickColorRes(color);
        }
        return clickableSpan;
    }
    
    public static class ClickableSpan extends android.text.style.ClickableSpan {
        
        private Context context;
        
        private boolean needShowUnderLine;
        private int customClickColor;
        private boolean hasCustomClickColor;
        private ClickableSpanListener clickListener;
        private String clickContent;
        
        public ClickableSpan(Context context) {
            this.context = context;
        }
        
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
        
        public ClickableSpan setClickColorRes(@ColorRes int colorRes) {
            return setClickColor(context.getResources().getColor(colorRes));
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
}
