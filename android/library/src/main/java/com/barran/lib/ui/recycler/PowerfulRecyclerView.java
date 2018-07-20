package com.barran.lib.ui.recycler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 增强型 RecyclerView,目前支持最大高度
 *
 * Created by tanwei on 2018/4/13.
 */

public class PowerfulRecyclerView extends RecyclerView {
    private int maxHeight;
    
    public PowerfulRecyclerView(Context context) {
        super(context);
    }
    
    public PowerfulRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    
    public PowerfulRecyclerView(Context context, @Nullable AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public void setMaxHeight(int height) {
        maxHeight = height;
    }
    
    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        if (maxHeight > 0 && getMeasuredHeight() > maxHeight) {
            setMeasuredDimension(getMeasuredWidth(), maxHeight);
        }
    }
}
