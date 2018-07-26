package com.whuthm.happychat.domain;

import android.support.annotation.Nullable;

import com.barran.lib.utils.log.Logs;
import com.whuthm.happychat.data.api.RetrofitClient;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by huangming on 18/07/2018.
 *
 * WebSocket
 */

public class ConnectionService {
    
    private static final String TAG = "socket";
    
    private String url = "ws://echo.websocket.org";// websocket官网测试url
    
    public void connect() {
        Request request = new Request.Builder().url(url).build();
        
        RetrofitClient.okHttp().newWebSocket(request, new SocketListener());
        
        RetrofitClient.okHttp().dispatcher().executorService().shutdown();
    }
    
    private class SocketListener extends WebSocketListener {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            Logs.v(TAG, "onOpen");
            
            Logs.v(TAG, "test send message");
            
            webSocket.send("hello yellow boss");
            webSocket.send("welcome");
            webSocket.send(ByteString.decodeHex("test byte"));
            webSocket.close(1000, "byebye");
        }
        
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            Logs.v(TAG, "onMessage : " + text);
        }
        
        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
            Logs.v(TAG, "onMessage : " + bytes);
        }
        
        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            Logs.v(TAG, "onClosing code:" + code + ", reason:" + reason);
        }
        
        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            Logs.v(TAG, "onClosed code:" + code + ", reason:" + reason);
        }
        
        @Override
        public void onFailure(WebSocket webSocket, Throwable t,
                @Nullable Response response) {
            super.onFailure(webSocket, t, response);
            Logs.v(TAG, "onFailure : " + t.getMessage());
        }
    }
    
}
