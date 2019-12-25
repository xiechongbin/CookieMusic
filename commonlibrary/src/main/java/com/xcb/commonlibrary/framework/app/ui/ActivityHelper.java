package com.xcb.commonlibrary.framework.app.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.xcb.commonlibrary.framework.LauncherAppAgent;
import com.xcb.commonlibrary.framework.MicroAppContext;
import com.xcb.commonlibrary.framework.app.ActivityApplication;
import com.xcb.commonlibrary.framework.msg.MsgCodeConstants;
import com.xcb.commonlibrary.framework.service.ext.ExternalService;

import java.util.List;

/**
 * Activity辅助类
 *
 * @author jinpingchen
 */
public class ActivityHelper {
    final static String TAG = ActivityHelper.class.getSimpleName();

    /**
     * 对应的Activity
     */
    private Activity mActivity;

    /**
     * 对应的App
     */
    protected ActivityApplication mApp;

    /**
     * 上下文
     */
    protected MicroAppContext mMicroApplicationContext;

    /**
     * 对话框帮助类
     */
    private DialogHelper mDialogHelper;

    public ActivityHelper(Activity activity) {
        mActivity = activity;
        mDialogHelper = new DialogHelper(mActivity);

        String appId = mActivity.getIntent().getStringExtra("app_id");
        LauncherAppAgent application = LauncherAppAgent.getInstance();
        mMicroApplicationContext = application.getMicroAppContext();
        mApp = (ActivityApplication) mMicroApplicationContext.findAppById(appId);
        Log.v(TAG, "ActivityHelper() appId: " + appId);
        if (mApp == null) {
            // FIXME 这是一个恶心的做法，创建一个空的App，防止空指针，Activity生命周期的掌控在系统，与app的生命周期
            mApp = new ActivityApplicationStub();
            mApp.attachContext(mMicroApplicationContext);
            finish();
            Log.v(TAG, "ActivityHelper() return");
            return;
        }

        mApp.setIsPrevent(false);
        mApp.pushActivity(mActivity);

        LocalBroadcastManager broadcastManager = LocalBroadcastManager
                .getInstance(mActivity);
        Intent intent = new Intent(MsgCodeConstants.FRAMEWORK_ACTIVITY_CREATE);
        broadcastManager.sendBroadcast(intent);
    }

    public void onStart() {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager
                .getInstance(mActivity);
        Intent intent = new Intent(MsgCodeConstants.FRAMEWORK_ACTIVITY_START);
        broadcastManager.sendBroadcast(intent);
    }

    public void onResume() {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager
                .getInstance(mActivity);
        Intent intent = new Intent(MsgCodeConstants.FRAMEWORK_ACTIVITY_RESUME);
        broadcastManager.sendBroadcast(intent);
        mMicroApplicationContext.updateActivity(mActivity);//很重要的一个步骤
    }

    public void onPause() {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager
                .getInstance(mActivity);
        Intent intent = new Intent(MsgCodeConstants.FRAMEWORK_ACTIVITY_PAUSE);
        if (mApp != null) {
            intent.putExtra(MsgCodeConstants.FRAMEWORK_ACTIVITY_DATA, mApp.getAppId());
        }
        broadcastManager.sendBroadcast(intent);
    }

    public void onUserLeaveHint() {
        if (isApplicationBroughtToBackground()) {
            LocalBroadcastManager broadcastManager = LocalBroadcastManager
                    .getInstance(mActivity);
            Intent intent = new Intent(
                    MsgCodeConstants.FRAMEWORK_ACTIVITY_USERLEAVEHINT);
            broadcastManager.sendBroadcast(intent);
        }
    }

    private boolean isApplicationBroughtToBackground() {

        if (mActivity == null) {
            return false;
        }

        ActivityManager am = (ActivityManager) mActivity.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(mActivity.getPackageName())) {
                return true;
            }
        }

        return false;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager
                .getInstance(mActivity);
        Intent intent = new Intent(
                MsgCodeConstants.FRAMEWORK_WINDOW_FOCUS_CHANGED);
        intent.putExtra(MsgCodeConstants.FRAMEWORK_WINDOW_FOCUS_CHANGED,
                hasFocus);
        broadcastManager.sendBroadcast(intent);
        if (hasFocus) {
            mApp.windowFocus();
        }
    }

    public void dispatchOnTouchEvent(MotionEvent event) {

        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN
                || action == MotionEvent.ACTION_UP) {
            LocalBroadcastManager broadcastManager = LocalBroadcastManager
                    .getInstance(mActivity);
            Intent intent = new Intent(MsgCodeConstants.FRAMEWORK_VIEW_CLICK);
            intent.putExtra(MsgCodeConstants.FRAMEWORK_VIEW_CLICK, event);
            if (mApp != null) {
                intent.putExtra(MsgCodeConstants.FRAMEWORK_ACTIVITY_DATA, mApp.getAppId());
            }
            broadcastManager.sendBroadcast(intent);
        }

    }

    public void onSaveInstanceState(Bundle outState) {
        mMicroApplicationContext.saveState();
    }

    public void onDestroy() {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager
                .getInstance(mActivity);
        Intent intent = new Intent(MsgCodeConstants.FRAMEWORK_ACTIVITY_DESTROY);
        broadcastManager.sendBroadcast(intent);
    }

    public void finish() {
        if (mApp != null) {
            mApp.removeActivity(mActivity);
        }
        mDialogHelper.dismissProgressDialog();
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
    public void alert(String title, String msg, String positive,
                      DialogInterface.OnClickListener positiveListener, String negative,
                      DialogInterface.OnClickListener negativeListener) {
        mDialogHelper.alert(title, msg, positive, positiveListener, negative,
                negativeListener);
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
     * @param isCanceledOnTouchOutside 外部是否可点取消
     */
    public void alert(String title, String msg, String positive,
                      DialogInterface.OnClickListener positiveListener, String negative,
                      DialogInterface.OnClickListener negativeListener,
                      Boolean isCanceledOnTouchOutside) {
        mDialogHelper.alert(title, msg, positive, positiveListener, negative,
                negativeListener, isCanceledOnTouchOutside);
    }

    /**
     * TOAST
     *
     * @param msg    消息
     * @param period 时长
     */
    public void toast(String msg, int period) {
        mDialogHelper.toast(msg, period);
    }

    public <T> T findServiceByInterface(String interfaceName) {
        return (T) mMicroApplicationContext
                .findServiceByInterface(interfaceName);
    }

    public <T extends ExternalService> T getExtServiceByInterface(
            String className) {
        return (T) mMicroApplicationContext.getExtServiceByInterface(className);
    }

    public ActivityApplication getApp() {
        return mApp;
    }

    public MicroAppContext getMicroAppContext() {
        return mMicroApplicationContext;
    }

    private class ActivityApplicationStub extends ActivityApplication {

        @Override
        public String getEntryClassName() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        protected void onCreate(Bundle params) {
            // TODO Auto-generated method stub

        }

        @Override
        protected void onStart() {
            // TODO Auto-generated method stub

        }

        @Override
        protected void onRestart(Bundle params) {
            // TODO Auto-generated method stub

        }

        @Override
        protected void onStop() {
            // TODO Auto-generated method stub

        }

        @Override
        protected void onDestroy(Bundle params) {
            // TODO Auto-generated method stub

        }

    }
}
