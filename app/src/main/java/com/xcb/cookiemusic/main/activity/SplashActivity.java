package com.xcb.cookiemusic.main.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.xcb.cookiemusic.R;
import com.xcb.cookiemusic.dialog.ProtocolAgreeFragmentDialog;
import com.xcb.cookiemusic.login.LoginAndRegisterActivity;
import com.xcb.cookiemusic.utils.Constant;
import com.xcb.cookiemusic.utils.SharedPreferenceUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler(this.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                showProtocolDialog();
                LoginAndRegisterActivity.startActivity(SplashActivity.this);
            }
        }, 2000);
    }


    /**
     * 显示协议对话框
     */
    private void showProtocolDialog() {
        boolean hasAgree = SharedPreferenceUtil.getInstance().getBoolean(Constant.SP_KEY_HAS_AGREE);
        if (!hasAgree) {
            ProtocolAgreeFragmentDialog dialog = ProtocolAgreeFragmentDialog.newInstance();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            dialog.show(transaction, ProtocolAgreeFragmentDialog.class.getSimpleName());
        } else {

        }
    }
}
