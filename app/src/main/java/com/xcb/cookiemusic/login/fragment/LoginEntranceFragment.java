package com.xcb.cookiemusic.login.fragment;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.xcb.commonlibrary.base.BaseFragment;
import com.xcb.commonlibrary.view.WaveView;
import com.xcb.cookiemusic.R;
import com.xcb.cookiemusic.login.utils.LoginCallbackHelper;
import com.xcb.cookiemusic.utils.ToastUtils;

/**
 * @author xcb
 * date：2020-01-20 14:55
 * description:登录入口页面
 */
public class LoginEntranceFragment extends BaseFragment implements View.OnClickListener {
    private WaveView mWaveView;
    private TextView tv_login_phone;
    private TextView tv_enter_now;
    private AppCompatImageView iv_wei_xin;
    private AppCompatImageView iv_wei_bo;
    private AppCompatImageView iv_qq;
    private AppCompatImageView iv_wang_yi;

    private MaterialCheckBox checkBox;

    public static LoginEntranceFragment newInstance() {
        Bundle args = new Bundle();
        LoginEntranceFragment fragment = new LoginEntranceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViews(@NonNull View root, @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mWaveView = root.findViewById(R.id.waveView);
        tv_login_phone = root.findViewById(R.id.tv_login_with_phone);
        tv_enter_now = root.findViewById(R.id.tv_enter_now);
        iv_wei_bo = root.findViewById(R.id.login_wei_bo);
        iv_wei_xin = root.findViewById(R.id.login_wei_xin);
        iv_qq = root.findViewById(R.id.login_qq);
        iv_wang_yi = root.findViewById(R.id.login_wang_yi);
        checkBox = root.findViewById(R.id.cb_agree);

        tv_login_phone.setOnClickListener(this);
        tv_enter_now.setOnClickListener(this);
        iv_wei_xin.setOnClickListener(this);
        iv_wei_bo.setOnClickListener(this);
        iv_qq.setOnClickListener(this);
        iv_wang_yi.setOnClickListener(this);

        initWaveView();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_login_entrance;
    }

    @Override
    protected void initData() {

    }

    /**
     * 初始化水波纹效果
     */
    private void initWaveView() {
        mWaveView.setDuration(2000);
        mWaveView.setStyle(Paint.Style.STROKE);
        mWaveView.setSpeed(1000);
        mWaveView.setColor(Color.parseColor("#80ffffff"));
        mWaveView.setInterpolator(new AccelerateInterpolator(1.2f));
        mWaveView.start();
    }

    @Override
    public void onClick(View v) {
        if (!hasAgreePolicy()) {
            ToastUtils.toast(this.getContext(), R.string.agree_tips);
        }
        switch (v.getId()) {
            case R.id.tv_enter_now:
                break;
            case R.id.tv_login_with_phone:
                LoginCallbackHelper.onLoginWithPhoneCallback(getActivity());
                break;
            case R.id.login_wei_xin:
                break;
            case R.id.login_wei_bo:
                break;
            case R.id.login_wang_yi:
                break;
            case R.id.login_qq:
                break;
        }
    }

    /**
     * 是否同意用户隐私协定
     */
    private boolean hasAgreePolicy() {
        return checkBox != null && checkBox.isChecked();
    }
}
