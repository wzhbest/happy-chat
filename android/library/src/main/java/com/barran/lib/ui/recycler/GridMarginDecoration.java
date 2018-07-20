package com.barran.lib.ui.recycler;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerView使用GridLayoutManager布局时实现分隔间隔
 * 
 * Created by tanwei on 2018/4/13.
 */

public class GridMarginDecoration extends RecyclerView.ItemDecoration {
    private int spanCount;
    private int spacing;
    private boolean includeEdge;
    
    /**
     * 传递单行item数量，间隔dimen
     *
     * @param spanCount
     *            单行item数量
     * @param space
     *            间隔dimen
     */
    public GridMarginDecoration(int spanCount, int space) {
        this(spanCount, space, false);
    }
    
    /**
     * 传递单行item数量，间隔dimen
     *
     * @param spanCount
     *            单行item数量
     * @param space
     *            间隔dimen
     *
     * @param includeEdge
     *            是否包含左右间距
     */
    public GridMarginDecoration(int spanCount, int space, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = space;
        this.includeEdge = includeEdge;
    }
    
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
            RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column
        
        if (includeEdge) {
            // spacing - column * ((1f / spanCount) * spacing)
            outRect.left = spacing - column * spacing / spanCount;
            // (column + 1) * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount;
            
            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.bottom = spacing; // item bottom
        }
        else {
            // column * ((1f / spanCount) * spacing)
            outRect.left = column * spacing / spanCount;
            // spacing - (column + 1) * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / spanCount;
            if (position >= spanCount) {
                outRect.top = spacing; // item top
            }
        }
    }
}
