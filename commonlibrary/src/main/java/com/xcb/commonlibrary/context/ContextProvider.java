package com.xcb.commonlibrary.context;

import android.app.Application;
import android.content.ContentProvider;
import android.content.Context;

/**
 * @author xcb
 * date：2020/4/16 3:59 PM
 * description: context 全局提供者  全局无侵入式
 */
public class ContextProvider {
    private static volatile ContextProvider instance;
    private Context mContext;

    private ContextProvider(Context context) {
        this.mContext = context;
    }

    public static ContextProvider get() {
        if (instance == null) {
            synchronized (ContextProvider.class) {
                if (instance == null) {
                    Context context = ApplicationContextProvider.mContext;
                    if (context == null) {
                        throw new IllegalStateException("context is null ");
                    }
                    instance = new ContextProvider(context);
                }
            }
        }
        return instance;
    }

    /**
     * 获取上下文
     *
     * @return 上下文
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * 获取到application
     *
     * @return Application
     */
    public Application getApplication() {
        return (Application) mContext.getApplicationContext();
    }
}
