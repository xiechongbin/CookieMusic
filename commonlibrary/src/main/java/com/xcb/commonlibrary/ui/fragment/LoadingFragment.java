package com.xcb.commonlibrary.ui.fragment;

import android.app.Dialog;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.xcb.commonlibrary.R;

public class LoadingFragment extends DialogFragment {
    public static final String TAG = "LoadingFragment";

    public static LoadingFragment newInstance() {
        Bundle args = new Bundle();
        LoadingFragment fragment = new LoadingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static void show(@NonNull FragmentManager manager) {
        Fragment fragment = manager.findFragmentByTag(TAG);
        LoadingFragment loadingFragment = null;
        if (fragment instanceof LoadingFragment) {
            loadingFragment = (LoadingFragment) fragment;
        }
        if (loadingFragment == null) {
            loadingFragment = LoadingFragment.newInstance();
        }
        if (!loadingFragment.isAdded()) {
            loadingFragment.show(manager, LoadingFragment.TAG);
        } else {
            manager.beginTransaction().show(loadingFragment).commit();
        }
    }

    public static void dismiss(@NonNull FragmentManager manager) {
        Fragment fragment = manager.findFragmentByTag(LoadingFragment.TAG);
        if (fragment instanceof LoadingFragment) {
            ((LoadingFragment) fragment).dismiss();
        }
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        resetWindow();
    }

    private void resetWindow() {
        Dialog dialog = getDialog();
        Window window = dialog != null ? dialog.getWindow() : null;
        if (window != null) {
            Rect rect = new Rect();
            window.getDecorView().getWindowVisibleDisplayFrame(rect);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = rect.height() > 0 ? rect.height() : WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
            window.setBackgroundDrawable(new ColorDrawable());
        }
    }
}
