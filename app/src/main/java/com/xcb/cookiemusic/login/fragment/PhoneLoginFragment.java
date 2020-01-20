package com.xcb.cookiemusic.login.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.xcb.commonlibrary.base.BaseFragment;
import com.xcb.cookiemusic.R;
import com.xcb.cookiemusic.utils.ToastUtils;

/**
 * @author xcb
 * date：2020-01-20 14:37
 * description:电话登录
 */
public class PhoneLoginFragment extends BaseFragment implements View.OnClickListener {
    private QMUITopBarLayout phoneLoginTopBar;
    private TextView tv_nextStep;

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
        }
    }
}
