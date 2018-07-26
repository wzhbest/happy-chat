package com.whuthm.happychat.ui.item;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.whuthm.happychat.R;

/**
 * 通用消息显示组件
 * 
 * Created by tanwei on 2018/7/23.
 */

public abstract class AbsMessageItem extends ConstraintLayout {
    
    private ImageView mAvatar;
    
    private TextView mTvNick, mTvTime;
    
    protected ViewStub contentStub;
    
    protected boolean isSendBySelf;
    
    public AbsMessageItem(Context context) {
        super(context);
        installView();
    }
    
    public AbsMessageItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        installView();
    }
    
    private void installView() {
        if (isSendBySelf) {
            LayoutInflater.from(getContext()).inflate(R.layout.layout_message_item_sent,
                    this);
            
            mTvNick = findViewById(R.id.layout_message_user_nick);
            mTvTime = findViewById(R.id.layout_message_time);
        }
        else {
            LayoutInflater.from(getContext())
                    .inflate(R.layout.layout_message_item_received, this);
        }
        
        mAvatar = findViewById(R.id.layout_message_user_avatar);
        contentStub = findViewById(R.id.layout_message_content);
    }
    
    protected abstract void inflateContent();
}
