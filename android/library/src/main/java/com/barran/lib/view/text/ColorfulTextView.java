package com.barran.lib.view.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.barran.lib.R;
import com.barran.lib.utils.DisplayUtil;
import com.barran.lib.utils.log.Logs;


/**
 * <p>设置正圆形的radius： {@linkplain R.styleable#ColorfulTextView_bgRoundRadius}</p>
 *
 * <p>注意：可以使用通过xml定义的shape或者使用下面的属性来定义背景</p>
 *
 * <p>设置背景solid颜色(必须)： {@linkplain R.styleable#ColorfulTextView_bgSolidColor}</p>
 *
 * <p>设置背景stroke颜色： {@linkplain R.styleable#ColorfulTextView_bgStrokeColor}</p>
 *
 * <p>设置背景stroke宽度： {@linkplain R.styleable#ColorfulTextView_bgStrokeWidth}</p>
 *
 * <p>设置背景radius： {@linkplain R.styleable#ColorfulTextView_bgRadius}</p>
 *
 * Created by tanwei on 2017/12/20.
 */

public class ColorfulTextView extends AppCompatTextView{

    private static final String TAG = "ColorfulTextView";

    private static final int INVALID_COLOR = 1;

    private boolean roundRadius;

    private int touchUpRadius;

    private int solidColor, radius;

    private int strokeWidth, strokeColor;

    private int leftLineColor;

    private int lineWidth, lineHeight;

    public ColorfulTextView(Context context) {
        super(context);
    }

    public ColorfulTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.ColorfulTextView);

        // 默认是不使用正圆形的圆角，可修改默认值使全局生效
        roundRadius = array.getBoolean(R.styleable.ColorfulTextView_bgRoundRadius, false);

        // 自定义drawable属性
        solidColor = array.getColor(R.styleable.ColorfulTextView_bgSolidColor,
                INVALID_COLOR);
        radius = array.getDimensionPixelOffset(R.styleable.ColorfulTextView_bgRadius, 0);
        // stroke
        strokeWidth = array
                .getDimensionPixelOffset(R.styleable.ColorfulTextView_bgStrokeWidth, 0);
        strokeColor = array.getColor(R.styleable.ColorfulTextView_bgStrokeColor,
                INVALID_COLOR);

        // 左侧竖线
        leftLineColor = array.getColor(R.styleable.ColorfulTextView_leftLineColor, INVALID_COLOR);
        lineHeight = array.getDimensionPixelOffset(
                R.styleable.ColorfulTextView_lineHeight,
                ViewGroup.LayoutParams.MATCH_PARENT);
        lineWidth = array.getDimensionPixelOffset(R.styleable.ColorfulTextView_lineWidth,
                DisplayUtil.dp2px(3));

        // 设置xml自定义背景
        initDrawable();

        array.recycle();
    }

    private void initDrawable() {
        
        if (solidColor == INVALID_COLOR) {
            Drawable background = getBackground();
            if (background instanceof ColorDrawable) {
                solidColor = ((ColorDrawable) background).getColor();
            }
        }
        
        if (solidColor != INVALID_COLOR) {
            super.setBackground(buildDrawable());
        }
    }

    private GradientDrawable buildDrawable() {
        GradientDrawable xmlDrawable = new GradientDrawable();
        xmlDrawable.setColor(solidColor);
        xmlDrawable.setShape(GradientDrawable.RECTANGLE);
        if (strokeWidth > 0) {
            xmlDrawable.setStroke(strokeWidth, strokeColor);
        }
        if (radius > 0) {
            xmlDrawable.setCornerRadius(radius);
        }

        return xmlDrawable;
    }

    // 修正背景为正圆形radius
    private void touchUpRadius() {
        int height = getMeasuredHeight();
        if (height == 0) {
            return;
        }
        if (roundRadius) {
            Drawable background = getBackground();
            touchUpRadius = height / 2;
            handleDrawable(background);
        }
    }

    private void handleDrawable(Drawable drawable) {
        if (drawable instanceof StateListDrawable) {
            DrawableContainer.DrawableContainerState drawableContainerState = (DrawableContainer.DrawableContainerState) drawable
                    .getConstantState();
            if (drawableContainerState != null) {
                Drawable[] children = drawableContainerState.getChildren();
                if (children.length > 0) {
                    for (Drawable d : children) {
                        if (d != null) {
                            handleDrawable(d);
                        }
                    }
                }
            }
        }
        else if (drawable instanceof LayerDrawable) {
            int layerSize = ((LayerDrawable) drawable).getNumberOfLayers();
            for (int i = 0; i < layerSize; i++) {
                Drawable childDrawable = ((LayerDrawable) drawable).getDrawable(i);
                if (childDrawable != null) {
                    handleDrawable(childDrawable);
                }
            }
        }
        else if (drawable instanceof GradientDrawable) {
            ((GradientDrawable) drawable).setCornerRadius(touchUpRadius);
        }
        else if (drawable instanceof ColorDrawable) {
            solidColor = ((ColorDrawable) drawable).getColor();
            GradientDrawable background = buildDrawable();
            background.setCornerRadius(touchUpRadius);
            super.setBackground(background);
        }
        else {
            Logs.w(TAG, "invalid drawable type : "
                    + (drawable != null ? drawable.getClass().getSimpleName() : "null"));
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        final int height = getHeight();
        if (roundRadius && touchUpRadius == 0) {
            if (height > 0) {
                touchUpRadius();
            }
        }

        if (height > 0 && leftLineColor != INVALID_COLOR) {
            buildLineDrawable();
        }

        super.onLayout(changed, left, top, right, bottom);
    }

    private void buildLineDrawable() {
        GradientDrawable line = new GradientDrawable();
        line.setColor(leftLineColor);
        int height = getHeight();
        if (lineHeight == ViewGroup.LayoutParams.MATCH_PARENT) {
            lineHeight = height - DisplayUtil.dp2px(3);
        }
        else if (lineHeight > height) {
            lineHeight = height;
        }

        line.setBounds(0, 0, lineWidth, lineHeight);

        Drawable[] compoundDrawables = getCompoundDrawables();
        if (compoundDrawables.length >= 4) {
            setCompoundDrawables(line, compoundDrawables[1], compoundDrawables[2],
                    compoundDrawables[3]);
        }
        else {
            setCompoundDrawables(line, null, null, null);
        }
    }

    @Override
    public void setBackground(Drawable background) {
        super.setBackground(background);

        if (roundRadius) {
            requestLayout();
        }
    }

    public boolean isRoundRadius() {
        return roundRadius;
    }

    /**
     * 设置圆角是否为正圆形
     */
    public void setRoundRadius(boolean roundRadius) {
        this.roundRadius = roundRadius;
        requestLayout();
    }

    public void buildBackground(DrawableBuilder builder) {
        if (builder.solidColor != INVALID_COLOR) {
            GradientDrawable xmlDrawable = new GradientDrawable();
            xmlDrawable.setColor(builder.solidColor);
            xmlDrawable.setShape(GradientDrawable.RECTANGLE);

            if (builder.strokeWidth > 0) {
                xmlDrawable.setStroke(builder.strokeWidth,
                        builder.strokeColor != INVALID_COLOR ? builder.strokeColor
                                : builder.solidColor);
            }
            if (builder.radius > 0) {
                xmlDrawable.setCornerRadius(builder.radius);
            }
            roundRadius = builder.roundRadius;

            setBackground(xmlDrawable);

            if (roundRadius) {
                touchUpRadius();
            }
        }
    }

    public static class DrawableBuilder {
        protected int solidColor = INVALID_COLOR;
        protected int strokeColor = INVALID_COLOR;
        protected int strokeWidth;
        protected int radius;
        protected boolean roundRadius;

        public DrawableBuilder setSolidColor(int color) {
            solidColor = color;
            return this;
        }

        public DrawableBuilder setStrokeColor(int color) {
            strokeColor = color;
            return this;
        }

        public DrawableBuilder setStrokeWidth(int width) {
            strokeWidth = width;
            return this;
        }

        public DrawableBuilder setRadius(int radius) {
            this.radius = radius;
            return this;
        }

        public DrawableBuilder setRoundRadius(boolean round) {
            roundRadius = round;
            return this;
        }
    }
}
