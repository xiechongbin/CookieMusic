package com.xcb.cookiemusic.login.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.xcb.commonlibrary.base.BaseFragment;
import com.xcb.cookiemusic.R;

/**
 * @author xcb
 * date：2020-01-20 14:37
 * description:电话登录
 */
public class PhoneLoginFragment extends BaseFragment {
    private QMUITopBarLayout phoneLoginTopBar;

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
        phoneLoginTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
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
}
