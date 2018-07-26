package com.whuthm.happychat.ui.item;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.whuthm.happychat.R;

/**
 * 文本消息item
 *
 * Created by tanwei on 2018/7/23.
 */

public class TextMessageItem extends AbsMessageItem {
    
    private TextView tvContent;
    
    public TextMessageItem(Context context) {
        super(context);
    }
    
    public TextMessageItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    protected void inflateContent() {
        contentStub.setLayoutResource(R.layout.layout_message_content_text);
        View content = contentStub.inflate();
        
        tvContent = content.findViewById(R.id.layout_message_content_text);
    }
    
    public void setText(@StringRes int resId) {
        tvContent.setText(resId);
    }
    
    public void setText(String text) {
        tvContent.setText(text);
    }
}
