package com.whuthm.happychat.ui;

import android.content.Intent;
import android.os.Bundle;

import com.barran.lib.app.BaseActivity;
import com.whuthm.happychat.R;

public class MainActivity extends BaseActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        startActivity(new Intent(this, LoginActivity.class));
    }
}
