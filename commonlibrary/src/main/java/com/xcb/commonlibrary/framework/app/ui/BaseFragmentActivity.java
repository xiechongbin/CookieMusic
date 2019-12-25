package com.xcb.commonlibrary.framework.app.ui;

import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.xcb.commonlibrary.framework.MicroAppContext;
import com.xcb.commonlibrary.framework.app.ActivityApplication;


/**
 * @author jinpingchen
 */
public abstract class BaseFragmentActivity extends FragmentActivity implements ActivityResponsable {
    /**
     * 所属APP
     */
    protected ActivityApplication mApp;
    /**
     * 上下文
     */
    protected MicroAppContext mMicroAppContext;
    /**
     * Activity辅助类
     */
    private ActivityHelper mActivityHelper;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mActivityHelper.dispatchOnTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityHelper = new ActivityHelper(this);
        mApp = mActivityHelper.getApp();
        mMicroAppContext = mActivityHelper.getMicroAppContext();

        String deviceModel = android.os.Build.DEVICE;
        if (TextUtils.equals(deviceModel, "M040")) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                // 当前view去掉硬件加速 防止mx crash
                getWindow().getDecorView().setLayerType(
                        View.LAYER_TYPE_SOFTWARE, null);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mActivityHelper != null) {
            mActivityHelper.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mActivityHelper != null) {
            mActivityHelper.onPause();
        }
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        mActivityHelper.onUserLeaveHint();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mActivityHelper.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mActivityHelper.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mActivityHelper != null) {
            mActivityHelper.onStart();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mActivityHelper != null) {
            mActivityHelper.onDestroy();
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (mActivityHelper != null) {
            mActivityHelper.finish();
        }
    }

    @Override
    public void alert(String title, String msg, String positive, OnClickListener positiveListener,
                      String negative, OnClickListener negativeListener) {
        mActivityHelper.alert(title, msg, positive, positiveListener, negative, negativeListener);
    }

    @Override
    public void alert(String title, String msg, String positive, OnClickListener positiveListener,
                      String negative, OnClickListener negativeListener, boolean isCanceledOnTouchOutside) {
        mActivityHelper.alert(title, msg, positive, positiveListener, negative, negativeListener, isCanceledOnTouchOutside);
    }

    @Override
    public void toast(String msg, int period) {
        mActivityHelper.toast(msg, period);
    }

}
