package com.whuthm.happychat.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.barran.lib.utils.StringUtil;
import com.barran.lib.utils.log.Logs;
import com.barran.lib.view.text.LimitEditText;
import com.barran.lib.view.text.LimitTextWatcher;
import com.whuthm.happychat.R;
import com.whuthm.happychat.base.BaseActivity;
import com.whuthm.happychat.data.api.RetrofitClient;
import com.whuthm.happychat.proto.api.Authentication;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 登录界面
 * 
 * Created by huangming on 18/07/2018.
 */

public class LoginActivity extends BaseActivity {
    
    private LimitEditText mETAccount;
    private LimitEditText mETPassword;
    private TextView mTVSubmit;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        mETAccount = findViewById(R.id.activity_login_edit_account);
        mETAccount.addTextChangedListener(new LimitTextWatcher() {
            @Override
            public void afterTextChecked(Editable s) {
                checkSubmitButton();
            }
        }.setFilterMode(LimitTextWatcher.FilterMode.NO_EMOJI));
        mETPassword = findViewById(R.id.activity_login_edit_password);
        mETPassword.addTextChangedListener(new LimitTextWatcher() {
            @Override
            public void afterTextChecked(Editable s) {
                checkSubmitButton();
            }
        }.setFilterMode(LimitTextWatcher.FilterMode.NO_EMOJI));
        
        mTVSubmit = findViewById(R.id.fragment_pwd_login_tv_submit);
        mTVSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reqLogin();
            }
        });
    }
    
    private void checkSubmitButton() {
        if (!TextUtils.isEmpty(mETAccount.getText().toString())
                && !TextUtils.isEmpty(mETPassword.getText().toString())) {
            mTVSubmit.setEnabled(true);
        }
        else {
            mTVSubmit.setEnabled(false);
        }
    }
    
    private void reqLogin() {
        Authentication.LoginRequest.Builder builder = Authentication.LoginRequest
                .newBuilder();
        builder.setUsername(mETAccount.getText().toString())
                .setPassword(mETPassword.getText().toString()).setPublicKey("");
        RetrofitClient.api().login(builder.build()).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Authentication.LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        
                    }
                    
                    @Override
                    public void onNext(Authentication.LoginResponse value) {
                        Logs.v("login suc: token=" + value.getToken() + ", key= "
                                + value.getKeystore());
                        Toast.makeText(getApplication(), "success:" + value.getUserId(), Toast.LENGTH_LONG).show();
                    }
                    
                    @Override
                    public void onError(Throwable e) {
                        Logs.v("login fail " + e.getMessage());
                        Toast.makeText(getApplication(), "error", Toast.LENGTH_LONG).show();
                    }
                    
                    @Override
                    public void onComplete() {

                    }
                });
    }
}
