package com.whuthm.happychat.domain.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 会话实体
 * 
 * Created by huangming on 18/07/2018.
 */

@Entity
public class Conversation {
    
    private String conversionId;
    
    private int conversationType;
    
    private String conversionName;
    
    private String lastMessageBody;
    
    private long createTime;
    
    private int status;
    
    public Conversation() {
        
    }
    
    @Generated(hash = 1272194739)
    public Conversation(String conversionId, int conversationType,
            String conversionName, String lastMessageBody, long createTime,
            int status) {
        this.conversionId = conversionId;
        this.conversationType = conversationType;
        this.conversionName = conversionName;
        this.lastMessageBody = lastMessageBody;
        this.createTime = createTime;
        this.status = status;
    }

    public String getConversionId() {
        return this.conversionId;
    }
    
    public void setConversionId(String conversionId) {
        this.conversionId = conversionId;
    }
    
    public String getConversionName() {
        return this.conversionName;
    }
    
    public void setConversionName(String conversionName) {
        this.conversionName = conversionName;
    }
    
    public String getLastMessageBody() {
        return this.lastMessageBody;
    }
    
    public void setLastMessageBody(String lastMessageBody) {
        this.lastMessageBody = lastMessageBody;
    }
    
    public long getCreateTime() {
        return this.createTime;
    }
    
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    
    public int getStatus() {
        return this.status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }

    public int getConversationType() {
        return this.conversationType;
    }

    public void setConversationType(int conversationType) {
        this.conversationType = conversationType;
    }
}
