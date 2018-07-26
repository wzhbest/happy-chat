package com.whuthm.happychat.ui.item;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.whuthm.happychat.GlideApp;
import com.whuthm.happychat.R;

/**
 * 图片消息item
 * 
 * Created by tanwei on 2018/7/23.
 */

public class ImageMessageItem extends AbsMessageItem {
    
    private ImageView imageView;
    
    public ImageMessageItem(Context context) {
        super(context);
    }
    
    public ImageMessageItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    protected void inflateContent() {
        contentStub.setLayoutResource(R.layout.layout_message_content_image);
        View content = contentStub.inflate();
    }
    
    public void setImageUrl(String url) {
        GlideApp.with(getContext()).load(url).centerCrop()
                .placeholder(R.drawable.ic_launcher).into(imageView);
    }
}
