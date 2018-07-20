package com.whuthm.happychat.domain.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 消息实体
 * 
 * Created by huangming on 18/07/2018.
 */

@Entity
public class Message {
    
    private String messageId;
    
    private String body;
    
    private String fromUserId;
    
    private String toUserId;
    
    private long sendTime;
    
    private long receiveTime;
    
    private boolean read;
    
    private int status;
    
    public Message() {
        
    }
    
    @Generated(hash = 1698299478)
    public Message(String messageId, String body, String fromUserId, String toUserId,
            long sendTime, long receiveTime, boolean read, int status) {
        this.messageId = messageId;
        this.body = body;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.sendTime = sendTime;
        this.receiveTime = receiveTime;
        this.read = read;
        this.status = status;
    }
    
    public String getMessageId() {
        return this.messageId;
    }
    
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    
    public String getBody() {
        return this.body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
    
    public long getSendTime() {
        return this.sendTime;
    }
    
    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }
    
    public int getStatus() {
        return this.status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getFromUserId() {
        return this.fromUserId;
    }
    
    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }
    
    public String getToUserId() {
        return this.toUserId;
    }
    
    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }
    
    public long getReceiveTime() {
        return this.receiveTime;
    }
    
    public void setReceiveTime(long receiveTime) {
        this.receiveTime = receiveTime;
    }
    
    public boolean getRead() {
        return this.read;
    }
    
    public void setRead(boolean read) {
        this.read = read;
    }
}
