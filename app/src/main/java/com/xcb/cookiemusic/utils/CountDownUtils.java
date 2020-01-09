package com.xcb.cookiemusic.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.SoftReference;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author xcb
 * date：2020-01-03 11:24
 * description:倒计时
 */
public class CountDownUtils {
    private static final int WHAT = 1000;
    //秒
    private int timeLength;
    private int totalLength;
    private OnCountDownCallback callback;
    private Timer timer;
    private CountDownHandler handler;

    public interface OnCountDownCallback {
        void onTick(int millisUntilFinished);

        void onFinish();
    }

    public CountDownUtils(OnCountDownCallback callback) {
        this.callback = callback;
        handler = new CountDownHandler(callback);
    }

    public void setTimeLength(int timeLength) {
        this.timeLength = timeLength;
        this.totalLength = timeLength;
    }


    public int getTotalLength() {
        return totalLength;
    }

    /**
     * 开始倒计时
     */
    public void startCountDown() {
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (timeLength >= 0) {
                    Log.d("addccc1", "schedule timeLength = " + timeLength);
                    handler.sendMessage(handler.obtainMessage(WHAT, timeLength, 0));
                    timeLength--;
                }
            }
        }, 0, 1000);
    }

    /**
     * 取消
     */
    public void cancel() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        timeLength = 0;
    }

    private static class CountDownHandler extends Handler {
        private SoftReference<OnCountDownCallback> softReference;


        CountDownHandler(OnCountDownCallback countDownCallback) {
            softReference = new SoftReference<>(countDownCallback);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == WHAT) {
                OnCountDownCallback countDownCallback = softReference.get();
                int timeLength = msg.arg1;
                Log.d("addccc1", "handleMessage timeLength = " + timeLength);
                if (countDownCallback != null) {
                    if (timeLength > 0) {
                        countDownCallback.onTick(timeLength);
                    } else if (timeLength == 0) {
                        countDownCallback.onFinish();
                    }
                }
            }


        }
    }
}
