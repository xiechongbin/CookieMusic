package com.xcb.commonlibrary.framework.app.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

/**
 * 对话框辅助类
 *
 * @author jinpingchen
 */
public class DialogHelper {
    private Activity mActivity;
    private AlertDialog mAlertDialog;
    private Toast mToast;

    public DialogHelper(Activity activity) {
        mActivity = activity;
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
    public void alert(final String title, final String msg, final String positive,
                      final DialogInterface.OnClickListener positiveListener,
                      final String negative, final DialogInterface.OnClickListener negativeListener) {
        alert(title, msg, positive, positiveListener, negative, negativeListener, false);
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
     * @param isCanceledOnTouchOutside 是否可以点击外围框
     */
    public void alert(final String title, final String msg, final String positive,
                      final DialogInterface.OnClickListener positiveListener,
                      final String negative, final DialogInterface.OnClickListener negativeListener, final Boolean isCanceledOnTouchOutside) {
        dismissProgressDialog();
        //run on UI Thread
        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mActivity == null || mActivity.isFinishing()) {
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                if (title != null) {
                    builder.setTitle(title);
                }
                if (msg != null) {
                    builder.setMessage(msg);
                }
                if (positive != null) {
                    builder.setPositiveButton(positive, positiveListener);
                }
                if (negative != null) {
                    builder.setNegativeButton(negative, negativeListener);
                }
                mAlertDialog = builder.show();
                mAlertDialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
                mAlertDialog.setCancelable(false);
            }
        });
    }

    /**
     * TOAST
     *
     * @param msg    消息
     * @param period 时长
     */
    public void toast(final String msg, final int period) {
        //run on UI Thread
        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
//                mToast = new toast(mActivity);
//                View view = LayoutInflater.from(mActivity).inflate(R.layout.transient_notification, null);
//                TextView tv = (TextView) view.findViewById(android.R.id.message);
//                tv.setText(msg);
//                mToast.setView(view);
//                mToast.setDuration(period);
//                mToast.setGravity(Gravity.CENTER, 0, 0);
//                mToast.show();
            }
        });
    }

    public void dismissProgressDialog() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mAlertDialog != null && mAlertDialog.isShowing() && !mActivity.isFinishing()) {
                    mAlertDialog.dismiss();
                    mAlertDialog = null;
                }
            }
        });
    }
}
