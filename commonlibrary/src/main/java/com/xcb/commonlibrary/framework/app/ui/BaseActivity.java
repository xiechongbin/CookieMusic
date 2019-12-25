package com.xcb.commonlibrary.framework.app.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import com.xcb.commonlibrary.framework.MicroAppContext;
import com.xcb.commonlibrary.framework.app.ActivityApplication;
import com.xcb.commonlibrary.framework.service.ext.ExternalService;


/**
 * Activity封装类
 * <p>
 * 封装生命周期监控，对话框等
 *
 * @author jinpingchen
 */
public abstract class BaseActivity extends Activity implements ActivityResponsable {
    final static String TAG = BaseActivity.class.getSimpleName();
    /**
     * 对应的App
     */
    protected ActivityApplication mApp;

    /**
     * 上下文
     */
    protected MicroAppContext mMicroApplicationContext;
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
        mMicroApplicationContext = mActivityHelper.getMicroAppContext();
        String deviceModel = android.os.Build.DEVICE;

        if (TextUtils.equals(deviceModel, "M040")) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                // 当前view去掉硬件加速 防止mx crash
                getWindow().getDecorView().setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mActivityHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mActivityHelper.onPause();
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
        mActivityHelper.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivityHelper.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        mActivityHelper.finish();
    }

    /**
     * 弹对话框
     *
     * @param title            标题
     * @param msg              消息
     * @param positive         确定
     * @param positiveListener 确定回调
     * @param negative         否定
     * @param negativeListener 否定回调
     */
    @Override
    public void alert(String title, String msg, String positive,
                      DialogInterface.OnClickListener positiveListener, String negative,
                      DialogInterface.OnClickListener negativeListener) {
        mActivityHelper.alert(title, msg, positive, positiveListener, negative, negativeListener);
    }


    /**
     * 弹对话框
     *
     * @param title                    标题
     * @param msg                      消息
     * @param positive                 确定
     * @param positiveListener         确定回调
     * @param negative                 否定
     * @param negativeListener         否定回调
     * @param isCanceledOnTouchOutside 外部点是否可以取消对话框
     */
    @Override
    public void alert(String title, String msg, String positive,
                      DialogInterface.OnClickListener positiveListener, String negative,
                      DialogInterface.OnClickListener negativeListener, boolean isCanceledOnTouchOutside) {
        mActivityHelper.alert(title, msg, positive, positiveListener, negative, negativeListener, isCanceledOnTouchOutside);
    }

    @Override
    public void showProgressDialog(String msg) {

    }

    @Override
    public void showProgressDialog(String msg, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {

    }

    @Override
    public void dismissProgressDialog() {

    }

    /**
     * TOAST
     *
     * @param msg    消息
     * @param period 时长
     */
    @Override
    public void toast(String msg, int period) {
        mActivityHelper.toast(msg, period);
    }

    protected <T> T findServiceByInterface(String interfaceName) {
        return (T) mActivityHelper.findServiceByInterface(interfaceName);
    }

    protected <T extends ExternalService> T getExtServiceByInterface(String className) {
        return (T) mActivityHelper.getExtServiceByInterface(className);
    }

}
