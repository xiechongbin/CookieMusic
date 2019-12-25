package com.xcb.commonlibrary.framework;

import android.app.Application;
import android.util.Log;

/**
 * @author jinpingchen
 */
public class LauncherAppAgent extends Application {
    /**h
     * android上下文
     */
    private static LauncherAppAgent mInstance;

    /**
     * app上下文(自定义)
     */
    private MicroAppContext mMicroAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        //android上下文初始化
        mInstance = this;
        try {
            //这么做是为了满足单一原则，NarutoApplicationContext维护了扩展的android服务管理
            //反射形式生成对象目的：规避库循环引用的问题
            //NarutoApplicationContextImpl初始化
            mMicroAppContext = (MicroAppContext) Class.forName(MicroAppContextImpl.class.getName()).newInstance();
            //关联上Application上下文
            mMicroAppContext.attachContext(this);
        } catch (Exception e) {
            Log.e("xxm", "MicroAppContextImpl newInstance failed!", e);
            e.printStackTrace();
        }
    }

    public MicroAppContext getMicroAppContext() {
        return mMicroAppContext;
    }

    public static LauncherAppAgent getInstance() {
        return mInstance;
    }

    @Override
    public void onTerminate() {
        mMicroAppContext.clearState();
        super.onTerminate();
    }
}
