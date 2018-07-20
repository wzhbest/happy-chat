package com.whuthm.happychat.data;

import android.content.Context;

import com.whuthm.happychat.domain.model.Conversation;
import com.whuthm.happychat.domain.model.ConversationDao;
import com.whuthm.happychat.domain.model.DaoMaster;
import com.whuthm.happychat.domain.model.DaoSession;
import com.whuthm.happychat.domain.model.Message;
import com.whuthm.happychat.domain.model.MessageDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * 数据库操作封装类
 *
 * Created by tanwei on 2018/7/19.
 */

public class DBOperator {
    
    private static final String DB_NAME = "db";
    
    private Context context;
    
    private DaoMaster.DevOpenHelper mHelper;
    
    private DBOperator(Context context) {
        this.context = context;
        mHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
    }
    
    private static DBOperator sInstance;
    
    public static void init(Context context) {
        sInstance = new DBOperator(context);
    }
    
    public static void addConversation(Conversation conversation) {
        DaoMaster master = new DaoMaster(sInstance.mHelper.getWritableDb());
        DaoSession session = master.newSession();
        QueryBuilder<Conversation> queryBuilder = session.getConversationDao()
                .queryBuilder();
        if (queryBuilder.where(ConversationDao.Properties.ConversionId
                .eq(conversation.getConversionId())).count() > 0) {
            updateConversation(conversation);
        }
        else {
            session.getConversationDao().insert(conversation);
        }
        
        session.clear();
    }
    
    public static void updateConversation(Conversation conversation) {
        DaoMaster master = new DaoMaster(sInstance.mHelper.getWritableDb());
        DaoSession session = master.newSession();
        session.getConversationDao().update(conversation);
        
        session.clear();
    }
    
    public static void delConversation(Conversation conversation) {
        DaoMaster master = new DaoMaster(sInstance.mHelper.getWritableDb());
        DaoSession session = master.newSession();
        session.getConversationDao().delete(conversation);
        session.clear();
    }
    
    public static List<Conversation> getConversations() {
        DaoMaster master = new DaoMaster(sInstance.mHelper.getReadableDb());
        DaoSession session = master.newSession();
        QueryBuilder<Conversation> queryBuilder = session.getConversationDao()
                .queryBuilder();
        queryBuilder.orderDesc(ConversationDao.Properties.CreateTime);
        List<Conversation> list = queryBuilder.list();
        session.clear();
        return list;
    }
    
    public static void addMessage(Message message) {
        DaoMaster master = new DaoMaster(sInstance.mHelper.getWritableDb());
        DaoSession session = master.newSession();
        QueryBuilder<Message> queryBuilder = session.getMessageDao().queryBuilder();
        if (queryBuilder.where(MessageDao.Properties.MessageId.eq(message.getMessageId()))
                .count() > 0) {
            session.getMessageDao().update(message);
        }
        else {
            session.getMessageDao().insert(message);
        }
        
        session.clear();
    }
    
    public static void updateMessage(Message message) {
        DaoMaster master = new DaoMaster(sInstance.mHelper.getWritableDb());
        DaoSession session = master.newSession();
        session.getMessageDao().update(message);
        
        session.clear();
    }
    
    public static void delConversation(Message message) {
        DaoMaster master = new DaoMaster(sInstance.mHelper.getWritableDb());
        DaoSession session = master.newSession();
        session.getMessageDao().delete(message);
        session.clear();
    }
    
    public static List<Message> getMessages(String conversationId, int count) {
        DaoMaster master = new DaoMaster(sInstance.mHelper.getReadableDb());
        DaoSession session = master.newSession();
        QueryBuilder<Message> queryBuilder = session.getMessageDao().queryBuilder();
        queryBuilder.where(MessageDao.Properties.ToUserId.eq(conversationId)).limit(count)
                .orderDesc(MessageDao.Properties.SendTime);
        List<Message> list = queryBuilder.list();
        session.clear();
        return list;
    }
    
}
