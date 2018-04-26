package com.xcb.cookiemusic.utils;

import android.text.TextUtils;

import java.lang.reflect.Method;

/**
 * 系统工具类
 * Created by xcb on 2018/4/26.
 */

public class SystemUtils {
    public static boolean isFlyme() {
        String flag = getSystemProperty("ro.build.display.id");
        return !TextUtils.isEmpty(flag) && flag.toLowerCase().contains("flyme");
    }

    private static String getSystemProperty(String key) {
        try {
            Class<?> classType = Class.forName("android.os.SystemProperties");
            Method getMethod = classType.getDeclaredMethod("get", String.class);
            return (String) getMethod.invoke(classType, key);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return null;
    }
}
