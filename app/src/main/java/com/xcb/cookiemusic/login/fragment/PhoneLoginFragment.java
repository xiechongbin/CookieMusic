package com.xcb.cookiemusic.login.fragment;

import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentManager;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.xcb.commonlibrary.base.BaseFragment;
import com.xcb.commonlibrary.framework.LauncherAppAgent;
import com.xcb.commonlibrary.utils.PhoneUtils;
import com.xcb.cookiemusic.R;
import com.xcb.cookiemusic.login.utils.LoginCallbackHelper;
import com.xcb.cookiemusic.utils.ToastUtils;

import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;

/**
 * @author xcb
 * date：2020-01-20 14:37
 * description:电话登录
 */
public class PhoneLoginFragment extends BaseFragment implements View.OnClickListener, TextWatcher {
    private QMUITopBarLayout phoneLoginTopBar;
    private TextView tv_nextStep;
    private EditText ed_phone;
    private AppCompatImageView iv_clear;
    private PhoneNumberUtil util = null;

    public static PhoneLoginFragment newInstance() {
        Bundle args = new Bundle();
        PhoneLoginFragment fragment = new PhoneLoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViews(@NonNull View root, @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        phoneLoginTopBar = root.findViewById(R.id.phoneLoginTopBar);
        initTopBar();
        tv_nextStep = root.findViewById(R.id.tv_next_step);
        tv_nextStep.setOnClickListener(this);
        ed_phone = root.findViewById(R.id.ed_phone);
        ed_phone.addTextChangedListener(this);
        iv_clear = root.findViewById(R.id.iv_clear);
        iv_clear.setOnClickListener(this);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_login_with_phone;
    }

    @Override
    protected void initData() {

    }

    private void initTopBar() {
        phoneLoginTopBar.setTitle(R.string.phone_login);
        phoneLoginTopBar.setTitleGravity(Gravity.LEFT);
        phoneLoginTopBar.addLeftImageButton(R.drawable.ic_back, R.id.id_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBack();
            }
        });
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_next_step) {
            String phoneNumber = ed_phone.getText().toString().replace(" ", "").substring(3);
            if (PhoneUtils.isValidNumber(phoneNumber, "CHN")) {
                ToastUtils.toast(LauncherAppAgent.getInstance().getApplicationContext(), R.string.phone_is_invalid);
                return;
            }
            LoginCallbackHelper.onNextStep(getActivity(), this.getClass().getSimpleName());
        } else if (v.getId() == R.id.iv_clear) {
            ed_phone.setText("");
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s != null) {
            String s1 = "";
            String prefix = "+86";
            s1 = s.toString();
            if (s1.startsWith(prefix)) {
                s1 = s1.replace(" ", "");
                s1 = s1.substring(3);
            }
            ed_phone.removeTextChangedListener(this);
            ed_phone.setText(prefix + " " + s1);
            ed_phone.setSelection(ed_phone.getText().length());
            ed_phone.addTextChangedListener(this);
        }
    }
}
