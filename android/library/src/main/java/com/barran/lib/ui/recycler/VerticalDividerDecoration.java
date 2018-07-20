package com.barran.lib.ui.recycler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 垂直分隔线，支持顶部和底部的分割线
 * 
 * Created by tanwei on 2018/4/13.
 */

public class VerticalDividerDecoration extends RecyclerView.ItemDecoration {
    private Context context;
    private int dividerHeight;
    private Paint dividerPaint;
    private boolean includePadding = true;// 是否包含item的padding区域
    
    private boolean showHeaderDivider;// 是否显示第一行的顶部分割线
    
    private static int DEFAULT_DIVIDER_COLOR = Color.parseColor("#ECECEC");
    
    public VerticalDividerDecoration(Context context) {
        this.context = context;
        dividerPaint = new Paint();
        dividerPaint.setColor(DEFAULT_DIVIDER_COLOR);
        dividerHeight = 2;
    }
    
    public VerticalDividerDecoration setDividerHeight(int px) {
        dividerHeight = px;
        return this;
    }
    
    public VerticalDividerDecoration setDividerColor(@ColorRes int color) {
        dividerPaint.setColor(context.getResources().getColor(color));
        return this;
    }
    
    public VerticalDividerDecoration setDividerIncludePadding(boolean isInclude) {
        includePadding = isInclude;
        return this;
    }
    
    public VerticalDividerDecoration showHeaderDivider(boolean showHeaderDivider) {
        this.showHeaderDivider = showHeaderDivider;
        return this;
    }
    
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
            RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = dividerHeight;
    }
    
    /***
     * 在item 的draw 之前调用
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth();
        
        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + dividerHeight;
            if (!includePadding) {
                c.drawRect(left + view.getPaddingLeft(), top,
                        right - view.getPaddingRight(), bottom, dividerPaint);
            }
            else {
                c.drawRect(left, top, right, bottom, dividerPaint);
            }
        }
    }
    
    /***
     * 在item 的draw 之后调用
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        
        if (showHeaderDivider && parent.getChildCount() > 0) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth();
            
            View view = parent.getChildAt(0);
            float top = view.getTop();
            float bottom = top + dividerHeight;
            
            if (!includePadding) {
                left += view.getPaddingLeft();
                right -= view.getPaddingRight();
            }
            c.drawRect(left, top, right, bottom, dividerPaint);
        }
    }
}
