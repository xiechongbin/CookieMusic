package com.xcb.cookiemusic.log;

/**
 * 日志打印类
 * Created by xcb on 2018/5/4.
 */

import android.util.Log;
public class Logger {
    private static final String TAG = Logger.class.getSimpleName();
    private static final int STACK_INDEX = 4;
    private static boolean isDebug = true;

    /**
     * 设置是否dubug开启
     * 建议只初始化一次
     */
    public static void setDebug(boolean isDebug) {
        Logger.isDebug = isDebug;
    }

    /**
     * 写error日志
     */
    public static void e(String tag, String msg) {
        if (Logger.isDebug) {
            Log.e(tag, buildMsg(msg));
        }
    }

    /**
     * 写error日志
     */
    public static void e(String msg) {
        if (Logger.isDebug) {
            Log.e(buildTag(), buildMsg(msg));
        }
    }


    /**
     * 写warn日志
     */
    public static void w(String tag, String msg) {
        if (Logger.isDebug) {
            Log.w(tag, buildMsg(msg));
        }
    }

    /**
     * 写warn日志
     */
    public static void w(String msg) {
        if (Logger.isDebug) {
            Log.w(buildTag(), buildMsg(msg));
        }
    }

    /**
     * 写info日志
     */
    public static void i(String tag, String msg) {
        if (Logger.isDebug) {
            Log.i(tag, buildMsg(msg));
        }
    }

    /**
     * 写info日志
     */
    public static void i(String msg) {
        if (Logger.isDebug) {
            Log.i(buildTag(), buildMsg(msg));
        }
    }

    /**
     * 写debug日志
     */
    public static void d(String tag, String msg) {
        if (Logger.isDebug) {
            Log.d(tag, buildMsg(msg));
        }
    }

    /**
     * 写debug日志
     */
    public static void d(String msg) {
        if (Logger.isDebug) {
            Log.d(buildTag(), buildMsg(msg));
        }
    }

    /**
     * 写verbose日志
     */
    public static void v(String tag, String msg) {
        if (Logger.isDebug) {
            Log.v(tag, buildMsg(msg));
        }
    }

    /**
     * 写verbose日志
     */
    public static void v(String msg) {
        if (Logger.isDebug) {
            Log.v(buildTag(), buildMsg(msg));
        }
    }

    /**
     * 生成tag串
     */
    private static String buildTag() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        if (trace.length > STACK_INDEX) {
            StringBuilder builder = new StringBuilder();
            builder.append(TAG)
                    .append("=>").append(getSimpleClassName(trace[STACK_INDEX].getClassName()))
                    .append(".")
                    .append(trace[STACK_INDEX].getMethodName())
                    .append("(")
                    .append(trace[STACK_INDEX].getFileName())
                    .append(":")
                    .append(trace[STACK_INDEX].getLineNumber())
                    .append(")");
            return builder.toString();
        }
        return TAG;
    }

    /**
     * 生成msg串
     */
    private static String buildMsg(String msg) {
        return "-----" + msg + "-----";
    }

    /**
     * 截取简单类名
     */
    private static String getSimpleClassName(String name) {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }

}

