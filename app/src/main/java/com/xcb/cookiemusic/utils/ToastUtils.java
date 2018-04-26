package com.xcb.cookiemusic.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

/**
 * 吐司工具类
 * Created by xcb on 2018/4/26.
 */

public class ToastUtils {
    private static Toast toast;

    public static void toast(Context context, @NonNull String msg) {
        int duration = Toast.LENGTH_SHORT;
        if (msg.length() > 10) {
            duration = Toast.LENGTH_LONG;
        }
        if (toast == null) {
            toast = Toast.makeText(context, msg, duration);
        } else {
            toast.setText(msg);
        }
        toast.show();

    }

    public static void toast(Context context, int resID) {
        toast(context, context.getString(resID));
    }
}
