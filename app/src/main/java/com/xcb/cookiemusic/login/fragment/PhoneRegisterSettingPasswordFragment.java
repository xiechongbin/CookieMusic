package com.xcb.cookiemusic.login.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.xcb.commonlibrary.base.BaseFragment;
import com.xcb.cookiemusic.R;
import com.xcb.cookiemusic.view.VerifyCodeView;

/**
 * @author xcb
 * date：2020-01-20 18:10
 * description:手机号注册设置登录密码
 */
public class PhoneRegisterSettingPasswordFragment extends BaseFragment implements View.OnClickListener {
    private QMUITopBarLayout settingPasswordTopBar;

    public static PhoneRegisterSettingPasswordFragment newInstance() {
        Bundle args = new Bundle();
        PhoneRegisterSettingPasswordFragment fragment = new PhoneRegisterSettingPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViews(@NonNull View root, @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        settingPasswordTopBar = root.findViewById(R.id.settingPasswordTopBar);
        initTopBar();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_phone_register_setting_password;
    }

    @Override
    protected void initData() {

    }

    private void initTopBar() {
        settingPasswordTopBar.setTitle(R.string.phone_register);
        settingPasswordTopBar.setTitleGravity(Gravity.LEFT);
        settingPasswordTopBar.addLeftImageButton(R.drawable.ic_back, R.id.id_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBack();
            }
        });
    }

    @Override
    public void onClick(View v) {
    }

    /**
     * 回退
     */
    private void popBack() {
        if (getActivity() != null) {
            FragmentManager ft = getActivity().getSupportFragmentManager();
            ft.popBackStack();
        }
    }

    public void popBackToRoot() {
        if (getActivity() != null) {
            FragmentManager ft = getActivity().getSupportFragmentManager();
            for (int i = 0; i < ft.getBackStackEntryCount(); i++) {
                ft.popBackStack();
            }
        }
    }
}
