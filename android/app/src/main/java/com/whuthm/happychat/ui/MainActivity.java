package com.whuthm.happychat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.barran.lib.app.BaseActivity;
import com.whuthm.happychat.R;
import com.whuthm.happychat.domain.ConnectionService;

public class MainActivity extends BaseActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ClickListener listener = new ClickListener();
        findViewById(R.id.activity_main_login).setOnClickListener(listener);
        findViewById(R.id.activity_main_socket).setOnClickListener(listener);
        findViewById(R.id.activity_main_chat).setOnClickListener(listener);
        findViewById(R.id.activity_main_conversation).setOnClickListener(listener);
    }
    
    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.activity_main_login:
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    break;
                
                case R.id.activity_main_socket:
                    ConnectionService service = new ConnectionService();
                    service.connect();
                    break;
                
                case R.id.activity_main_chat:
                    // startActivity(new Intent(MainActivity.this,
                    // LoginActivity.class));
                    break;
                
                case R.id.activity_main_conversation:
                    // startActivity(new Intent(MainActivity.this,
                    // LoginActivity.class));
                    break;
            }
        }
    }
}
