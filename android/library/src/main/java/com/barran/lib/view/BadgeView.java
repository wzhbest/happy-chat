package com.barran.lib.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.barran.lib.R;
import com.barran.lib.utils.DisplayUtil;

/**
 * 展示红点数字(带圆圈描边)
 *
 * Created by tanwei on 2017/10/21.
 */

public class BadgeView extends LinearLayout {
    
    private int mStrokeColor;
    private int mFillColor;
    
    private int mStrokeWidth;
    
    private TextView mTextView;
    private ColorStateList mTextColor;
    
    private GradientDrawable mBackgroundDrawable;
    
    private int mMaxPlusCount = 99;
    
    private int DEFAULT_BADGE_BG = Color.parseColor("#ED524D");
    private int DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF");
    private int DEFAULT_TEXT_FONT_SIZE = DisplayUtil.sp2px(8);
    
    public BadgeView(Context context) {
        super(context);
        init(context, null);
    }
    
    public BadgeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    
    private void init(Context context, AttributeSet attrs) {
        
        setWillNotDraw(false);
        setGravity(Gravity.CENTER);
        
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BadgeView);
            mFillColor = ta.getColor(R.styleable.BadgeView_fillColor,
                    DEFAULT_BADGE_BG);
            mStrokeColor = ta.getColor(R.styleable.BadgeView_strokeColor,
                    DEFAULT_BADGE_BG);
            mTextColor = ta.getColorStateList(R.styleable.BadgeView_android_textColor);
            if (mTextColor == null) {
                mTextColor = ColorStateList.valueOf(DEFAULT_TEXT_COLOR);
            }
            ta.recycle();
        }
        else {
            mFillColor = DEFAULT_BADGE_BG;
            mStrokeColor = DEFAULT_BADGE_BG;
            mTextColor = ColorStateList.valueOf(DEFAULT_TEXT_COLOR);
        }
        
        mStrokeWidth = DisplayUtil.dp2px(1);
        mBackgroundDrawable = new GradientDrawable();
        mBackgroundDrawable.setColor(mFillColor);
        mBackgroundDrawable.setStroke(mStrokeWidth, mStrokeColor);
    }
    
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 0) {
            mTextView = new TextView(getContext());
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, DEFAULT_TEXT_FONT_SIZE);
            mTextView.setTextColor(mTextColor);
            mTextView.setGravity(Gravity.CENTER);
            final int defaultPadding = DisplayUtil.dp2px(1);
            mTextView.setPadding(defaultPadding, defaultPadding, defaultPadding,
                    defaultPadding);
            addView(mTextView);
        }
        else {
            mTextView = (TextView) getChildAt(0);
        }
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int radius = Math.max(mTextView.getMeasuredWidth(),
                mTextView.getMeasuredHeight());
        setMeasuredDimension(radius, radius);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int radius = Math.max(getWidth(), getHeight());
        mBackgroundDrawable.setCornerRadius(radius);
        mBackgroundDrawable.setSize(radius, radius);
        mBackgroundDrawable.setBounds(0, 0, radius, radius);
        mBackgroundDrawable.draw(canvas);
    }
    
    public void setFillColor(int color) {
        if (color != mFillColor) {
            mFillColor = color;
            mBackgroundDrawable.setColor(mFillColor);
            invalidate();
        }
    }
    
    public int getFillColor() {
        return mFillColor;
    }
    
    /** 设置描边颜色 */
    public void setStrokeColor(int color) {
        if (color != mStrokeColor) {
            mStrokeColor = color;
            mBackgroundDrawable.setStroke(mStrokeWidth, mStrokeColor);
            invalidate();
        }
    }
    
    public int getStrokeColor() {
        return mStrokeColor;
    }
    
    /** 设置描边颜色资源id */
    public void setStrokeColorId(int colorResId) {
        setStrokeColor(getResources().getColor(colorResId));
    }
    
    /** 设置描边宽度 */
    public void setStrokeWidth(int strokeWidth) {
        if (strokeWidth != mStrokeWidth) {
            mStrokeWidth = strokeWidth;
            mBackgroundDrawable.setStroke(mStrokeWidth, mStrokeColor);
            invalidate();
        }
    }
    
    public int getStrokeWidth() {
        return mStrokeWidth;
    }
    
    public void setBadgeText(String text) {
        if (TextUtils.isEmpty(text)) {
            setVisibility(GONE);
            return;
        }
        else {
            setVisibility(VISIBLE);
            mTextView.setText(text);
        }
    }
    
    public void setBadgeCount(int count) {
        if (count == 0) {
            setVisibility(GONE);
            return;
        }
        else {
            boolean needShowPlusCount = mMaxPlusCount > 0 && count > mMaxPlusCount;
            
            if (mMaxPlusCount > 0) {
                count = Math.min(mMaxPlusCount, count);
            }
            
            setVisibility(VISIBLE);
            mTextView.setText(needShowPlusCount ? count + "+" : String.valueOf(count));
        }
    }
    
    public int getBadgeCount() {
        int count = 0;
        try {
            count = Integer.parseInt(mTextView.getText().toString());
        } catch (Exception e) {}
        
        return count;
    }
    
    public void setMaxPlusCount(int maxPlusCount) {
        this.mMaxPlusCount = maxPlusCount;
    }
}
