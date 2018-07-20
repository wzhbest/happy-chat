package com.barran.lib.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 封装item点击
 *
 * Created by tanwei on 2017/10/21.
 */

public abstract class BaseRecyclerAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    
    protected RecyclerViewItemClickListener itemClickListener;
    
    private View.OnClickListener mOnClickListener;
    
    public void setItemClickListener(RecyclerViewItemClickListener l) {
        if (l != null) {
            itemClickListener = l;
            
            mOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick((int) view.getTag());
                }
            };
        }
    }
    
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder viewHolder = createHolder(parent, viewType);
        if (itemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    
                }
            });
        }
        return viewHolder;
    }
    
    protected abstract RecyclerView.ViewHolder createHolder(ViewGroup parent,
            int viewType);
    
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position,
            List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        
        holder.itemView.setTag(position);
    }
    
    public interface RecyclerViewItemClickListener {
        void onItemClick(int position);
    }
}
