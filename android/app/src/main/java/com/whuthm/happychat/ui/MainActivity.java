package com.whuthm.happychat.ui;

import android.content.Intent;
import android.os.Bundle;

import com.whuthm.happychat.R;
import com.whuthm.happychat.base.BaseActivity;

public class MainActivity extends BaseActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        startActivity(new Intent(this, LoginActivity.class));
    }
}
