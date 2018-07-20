package com.barran.lib.view;

import java.util.Locale;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;

import com.barran.lib.R;

/**
 * 仿即刻点赞效果
 *
 * Created by tanwei on 2017/10/25.
 */

public class UpView extends View {
    
    private static final String TAG = "UpView";
    
    private static final int MAX_DIGITS = 4;
    
    private int mUpCount, mCount, mMaxDigit;// 点赞后的数量，当前数量，最大位数
    
    private boolean isUp;// 是否已点赞
    
    private Bitmap upImageNormal, upImageUp;
    
    private String mCountStr, mUpCountStr;
    
    private int mMaxCount;
    
    private String mMaxCountString;
    
    private Paint mImagePaint;
    
    private Paint mShiningPaint, mCirclePaint;// 绘制点赞后的闪光效果
    
    private Paint mTextPaint;// 绘制文字
    
    private int colorShining, colorText;
    
    private int margin;
    
    private float mMinWidth, mMinHeight;
    
    private OnClickListener mListener;
    
    private Path path;
    
    private Path mCirclePath;
    
    private float animFraction, imageAnimFraction;// 由于图片存在先放大后缩小的动画，需要单独设置动画插值器以及系数
    
    private Animator.AnimatorListener mAnimListener;
    
    private boolean isInAnim;
    
    private static final float ANIM_MAX_SCALE = 1.2f;
    
    public UpView(Context context) {
        super(context);
        
        init(null);
    }
    
    public UpView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        
        init(attrs);
    }
    
    public float getAnimFraction() {
        return animFraction;
    }
    
    public void setAnimFraction(float animFraction) {
        // Log.v(TAG, "animFraction : " + animFraction);
        this.animFraction = animFraction;
        invalidate();
    }
    
    public float getImageAnimFraction() {
        return imageAnimFraction;
    }
    
    public void setImageAnimFraction(float imageAnimFraction) {
        // Log.v(TAG, "imageAnimFraction : " + imageAnimFraction);
        this.imageAnimFraction = imageAnimFraction;
        invalidate();
    }

    private void init(AttributeSet attrs) {
        
        float textSize;
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs,
                    R.styleable.UpView, 0, 0);
            colorShining = array.getColor(R.styleable.UpView_shining_color,
                    getResources().getColor(R.color.color_shining));
            colorText = array.getColor(R.styleable.UpView_text_color,
                    getResources().getColor(R.color.color_text));
            mMaxDigit = array.getInt(R.styleable.UpView_max_digit, MAX_DIGITS);
            textSize = array.getDimension(R.styleable.UpView_text_size,
                    getResources().getDimensionPixelSize(R.dimen.text_14));
            
            array.recycle();
        }
        else {
            colorShining = getResources().getColor(R.color.color_shining);
            colorText = getResources().getColor(R.color.color_text);
            textSize = getResources().getDimensionPixelSize(R.dimen.text_14);
        }
        
        mImagePaint = new Paint();
        
        // default icon
        upImageNormal = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_up_normal);
        upImageUp = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_up_selected);
        margin = getResources().getDimensionPixelOffset(R.dimen.margin);
        
        mCirclePath = new Path();
        
        mShiningPaint = new Paint();
        mShiningPaint.setStrokeCap(Paint.Cap.ROUND);
        mShiningPaint.setStyle(Paint.Style.STROKE);
        mShiningPaint.setStrokeWidth(margin / 4);
        mShiningPaint.setColor(colorShining);
        
        mCirclePaint = new Paint();
        mCirclePaint.setStrokeWidth(margin / 4);
        mCirclePaint.setColor(colorShining);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        
        mTextPaint = new Paint();
        mTextPaint.setColor(colorText);
        mTextPaint.setTextSize(textSize);
        
        if (mMaxDigit <= 0) {
            mMaxDigit = MAX_DIGITS;
        }
        
        StringBuilder sb = new StringBuilder();
        int max = mMaxDigit;
        while (max > 0) {
            sb.append("9");
            max--;
        }
        mMaxCount = Integer.parseInt(sb.toString());
        sb.append("+");
        mMaxCountString = sb.toString();
        
        mMinWidth = upImageNormal.getWidth() + margin
                + mTextPaint.measureText(mMaxCountString, 0, mMaxCountString.length());
        // 加上图片左边的间距
        mMinWidth += margin;
        float textHeight = mTextPaint.descent() - mTextPaint.ascent();
        mMinHeight = Math.max(upImageNormal.getHeight() + margin, textHeight * 3);
        
        Log.v(TAG, String.format(Locale.CHINA, "minWidth : %1$f, minHeight : %2$f",
                mMinWidth, mMinHeight));
        
        mListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.v(TAG, "onClick");
                if (isInAnim) {
                    Log.v(TAG, "ignore, in anim");
                    return;
                }
                isUp = !isUp;
                
                showAnim();
            }
        };
        
        setOnClickListener(mListener);
        
        mCount = 0;
        mUpCount = 1;
        mCountStr = String.valueOf(mCount);
        mUpCountStr = String.valueOf(mUpCount);
        
        path = new Path();
        path.lineTo(mMinWidth, 0);
        path.lineTo(mMinWidth, mMinHeight);
        path.lineTo(0, mMinHeight);
        path.lineTo(0, 0);
        
        mAnimListener = new Animator.AnimatorListener() {
            
            @Override
            public void onAnimationStart(Animator animation) {
//                Log.v(TAG, "anim start");
                isInAnim = true;
            }
            
            @Override
            public void onAnimationEnd(Animator animation) {
//                Log.v(TAG, "anim end");
                isInAnim = false;
            }
            
            @Override
            public void onAnimationCancel(Animator animation) {
                isInAnim = false;
            }
            
            @Override
            public void onAnimationRepeat(Animator animation) {
                
            }
        };
    }

    public void setCount(int count) {
        setCountAndUp(count, false);
    }
    
    public void setCountAndUp(int count, boolean up) {
        isUp = up;
        if (isUp) {
            mUpCount = count;
            mCount = count - 1;
        }
        else {
            mCount = count;
            mUpCount = count + 1;
        }

        // 处理超出最大数量
        if (mCount == mMaxCount) {
            mCountStr = String.valueOf(mCount);
            mUpCountStr = mMaxCountString;
        }
        else if (mCount > mMaxCount) {
            mCountStr = mMaxCountString;
            mUpCountStr = mMaxCountString;
        }
        else {
            mCountStr = String.valueOf(mCount);
            mUpCountStr = String.valueOf(mUpCount);
        }
        
        invalidate();
    }
    
    public void showAnim() {
        
        if (isInAnim) {
            Log.v(TAG, "ignore, in anim");
            return;
        }
        
        if (isUp) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(this, "animFraction", 0, 1);
            ObjectAnimator imageAnimator = ObjectAnimator.ofFloat(this,
                    "imageAnimFraction", 0, 1);
            imageAnimator.setInterpolator(new AnticipateOvershootInterpolator());
            AnimatorSet set = new AnimatorSet();
            set.playTogether(animator, imageAnimator);
            animator.setDuration(300);
            animator.addListener(mAnimListener);
            imageAnimator.setDuration(200);
            set.start();
        }
        else {
            ObjectAnimator animator = ObjectAnimator.ofFloat(this, "animFraction", 1, 0);
            animator.setDuration(300);
            animator.addListener(mAnimListener);
            animator.start();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        
        switch (MeasureSpec.getMode(widthMeasureSpec)) {
            case MeasureSpec.UNSPECIFIED:
                width = (int) mMinWidth;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                if (width < mMinWidth) {
                    width = (int) mMinWidth;
                }
                break;
        }
        
        switch (MeasureSpec.getMode(heightMeasureSpec)) {
            case MeasureSpec.UNSPECIFIED:
                height = (int) mMinHeight;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                if (height < mMinHeight) {
                    height = (int) mMinHeight;
                }
                break;
        }
        
        setMeasuredDimension(width, height);
        
        Log.v(TAG, String.format(Locale.CHINA, "onMeasure width : %1$d, height : %2$d",
                width, height));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        
        if (getHeight() > mMinHeight || getWidth() > mMinWidth) {
            // 绘制到中心
            canvas.translate((getWidth() - mMinWidth) / 2,
                    (getHeight() - mMinHeight) / 2);
        }
        // 移到图片离左边的间距
        canvas.translate(margin, 0);
        
        // test lines
        // mTextPaint.setStyle(Paint.Style.STROKE);
        // canvas.drawPath(path, mTextPaint);
        // mTextPaint.setStyle(Paint.Style.FILL);
        
        float textHeight = mTextPaint.descent() - mTextPaint.ascent();
        // test lines
        // canvas.drawLine(0, 0, mMinWidth, 0, mTextPaint);
        // canvas.drawLine(0, textHeight, mMinWidth, textHeight, mTextPaint);
        // canvas.drawLine(0, textHeight * 2, mMinWidth, textHeight * 2,
        // mTextPaint);
        
        // draw image & circle
        int imageY = (int) ((mMinHeight - upImageNormal.getHeight()) / 2);
        Bitmap image = isUp ? upImageUp : upImageNormal;
        if (isInAnim) {
            
            mCirclePath.reset();
            float radius = Math.max(upImageNormal.getWidth(),
                    upImageNormal.getHeight() + margin) / 2 * animFraction;
            mCirclePath.addCircle(upImageNormal.getWidth() / 2, mMinHeight / 2, radius,
                    Path.Direction.CW);
            canvas.drawPath(mCirclePath, mCirclePaint);
            
            canvas.save();
            float scale;
            if (isUp) {
                scale = 1 + (ANIM_MAX_SCALE - 1) * imageAnimFraction;
            }
            else {
                scale = 1 + (ANIM_MAX_SCALE - 1) * animFraction;
            }
            canvas.scale(scale, scale, image.getWidth() / 2, mMinHeight / 2);
            canvas.drawBitmap(image, 0, imageY, mImagePaint);
            canvas.restore();
        }
        else {
            canvas.drawBitmap(image, 0, imageY, mImagePaint);
        }
        
        // shining
        if (isInAnim || isUp) {
            // shining height
            int length = (int) (margin * 0.6f);
            // thumb x, shining pivot x too
            int x = (int) (upImageNormal.getWidth() * 0.55f);
            // shining start y
            int y = imageY - margin;
            // shining pivot y
            int pivotY = imageY + length;
            
            canvas.save();
            
            if (isInAnim) {
                
                canvas.clipPath(mCirclePath);
                
                canvas.drawLine(x, y, x, y + length, mShiningPaint);
                canvas.rotate(45, x, pivotY);
                canvas.drawLine(x, y, x, y + length, mShiningPaint);
                canvas.rotate(-90, x, pivotY);
                canvas.drawLine(x, y, x, y + length, mShiningPaint);
                canvas.rotate(-45, x, pivotY);
                canvas.drawLine(x, y, x, y + length, mShiningPaint);
                
            }
            else {
                canvas.drawLine(x, y, x, y + length, mShiningPaint);
                canvas.rotate(45, x, pivotY);
                canvas.drawLine(x, y, x, y + length, mShiningPaint);
                canvas.rotate(-90, x, pivotY);
                canvas.drawLine(x, y, x, y + length, mShiningPaint);
                canvas.rotate(-45, x, pivotY);
                canvas.drawLine(x, y, x, y + length, mShiningPaint);
            }
            
            canvas.restore();
        }
        
        int textX = upImageNormal.getWidth() + margin;// text start x
        int textY = (int) (mMinHeight - mTextPaint.ascent()) / 2;// baseline的起始y坐标
        boolean exceedMax = mCount > mMaxCount;
        if (isInAnim && !exceedMax) {
            int start = calStartChangeIndex();
            String fix, newStr, oldStr;
            if (start == 0) {
                if (isUp) {
                    newStr = mUpCountStr;
                    oldStr = mCountStr;
                }
                else {
                    newStr = mCountStr;
                    oldStr = mUpCountStr;
                }
                
                fix = null;
            }
            else {
                if (isUp) {
                    newStr = mUpCountStr;
                    oldStr = mCountStr;
                }
                else {
                    newStr = mCountStr;
                    oldStr = mUpCountStr;
                }
                
                // 截取固定部分和变化部分字串
                fix = oldStr.substring(0, start);
                oldStr = oldStr.substring(start);
                newStr = newStr.substring(start);
            }
            
            if (fix != null) {
                canvas.drawText(fix, textX, textY, mTextPaint);
                // cal text x
                textX = textX + (int) mTextPaint.measureText(fix);
            }
            
            float offset;
            // cal textY of oldStr, newStr
            int color = colorText & 0x00ffffff;
            int alpha = colorText >> 24 & 0xff;
            int animAlpha;
            if (isUp) {
                offset = animFraction * textHeight;
                
                textY = (int) (textY - offset);
                animAlpha = (int) ((1 - animFraction) * alpha);
                mTextPaint.setColor(color | animAlpha << 24);
                canvas.drawText(oldStr, textX, textY, mTextPaint);
                
                textY = (int) (textY + textHeight);
                animAlpha = (int) (animFraction * alpha);
                mTextPaint.setColor(color | animAlpha << 24);
                canvas.drawText(newStr, textX, textY, mTextPaint);
            }
            else {
                offset = (1 - animFraction) * textHeight;
                
                textY = (int) (textY + offset);
                animAlpha = (int) (animFraction * alpha);
                mTextPaint.setColor(color | animAlpha << 24);
                canvas.drawText(oldStr, textX, textY, mTextPaint);
                
                textY = (int) (textY - textHeight);
                animAlpha = (int) ((1 - animFraction) * alpha);
                mTextPaint.setColor(color | animAlpha << 24);
                canvas.drawText(newStr, textX, textY, mTextPaint);
            }
            
            mTextPaint.setColor(colorText);
        }
        else {
            mTextPaint.setColor(colorText);
            canvas.drawText(isUp ? mUpCountStr : mCountStr, textX, textY, mTextPaint);
        }
    }

    // 计算文字滚动起始index
    private int calStartChangeIndex() {
        // 如果刚刚达到最大数量，直接返回最大数字个数即可
        if (mCount == mMaxCount) {
            return mMaxDigit;
        }
        if (mCountStr.length() != mUpCountStr.length()) {
            return 0;
        }
        int index = mCountStr.length() - 1;
        int reminder = mCount;
        // 末尾如果为9会进1，导致前一位数字变化，需要继续判断前一位，否则只需要从此位开始变化
        while (index >= 0) {
            
            if (reminder % 10 == 9) {
                reminder = reminder / 10;
                index--;
            }
            else {
                break;
            }
        }
        
        if (index < 0) {
            index = 0;
        }
        
        return index;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        
        if (l == mListener) {
            super.setOnClickListener(l);
        }
        else {
            Log.w(TAG, "not support");
        }
        
    }
}
