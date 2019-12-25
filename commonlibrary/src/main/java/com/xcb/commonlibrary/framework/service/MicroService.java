package com.xcb.commonlibrary.framework.service;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.xcb.commonlibrary.framework.MicroAppContext;
import com.xcb.commonlibrary.framework.MicroContent;


/**
 * @author jinpingchen
 */
public abstract class MicroService implements MicroContent {

    private MicroAppContext microAppContext;

    /**
     * 是否已经被激活（onCreate()被调用，onDestroy()还没有被调用）
     *
     * @return
     */
    public abstract boolean isActivated();

    /**
     * 创建
     *
     * @param params 参数
     */
    public abstract void create(Bundle params);

    /**
     * 销毁
     *
     * @param params 参数
     */
    public abstract void destroy(Bundle params);


    /**
     * 创建回调
     *
     * @param params 参数
     */
    protected abstract void onCreate(Bundle params);

    /**
     * 销毁回调
     *
     * @param params 参数
     */
    protected abstract void onDestroy(Bundle params);

    public void attachContext(MicroAppContext microAppContext) {
        this.microAppContext = microAppContext;
    }

    public MicroAppContext getMicroAppContext() {
        return this.microAppContext;
    }

    @Override
    public void saveState(SharedPreferences.Editor editor) {

    }

    @Override
    public void restoreState(SharedPreferences preferences) {

    }
}
