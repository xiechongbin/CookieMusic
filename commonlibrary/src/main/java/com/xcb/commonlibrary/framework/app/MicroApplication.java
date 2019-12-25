package com.xcb.commonlibrary.framework.app;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import com.xcb.commonlibrary.framework.MicroAppContext;
import com.xcb.commonlibrary.framework.MicroContent;


/**
 * App接口
 *
 * @author jinpingchen
 */
public abstract class MicroApplication implements MicroContent {

    /**
     * 上下文
     */
    private MicroAppContext mContext;
    /**
     * 源ID
     */
    private String mSourceId;
    /**
     * 父App类名
     */
    private String mParentAppClassName;
    /**
     * 应用Id
     */
    private String mAppId;

    /**
     * 创建
     *
     * @param bundle 参数
     * @throws AppLoadException
     */
    public abstract void create(Bundle bundle);

    /**
     * 启动
     */
    public abstract void start() throws AppLoadException;

    /**
     * 重新启动
     *
     * @param bundle 参数
     */
    public abstract void restart(Bundle bundle);

    /**
     * 暂停
     */
    public abstract void stop();

    /**
     * 入口
     *
     * @return 入口Activity或者Service
     */
    public abstract String getEntryClassName();


    /**
     * 阻止被回收，当前一activity被finish，并启动另一activity
     */
    public abstract void setIsPrevent(boolean isPrevent);

    /**
     * 创建回调
     *
     * @param bundle
     */
    protected abstract void onCreate(Bundle bundle);

    /**
     * 启动回调
     */
    protected abstract void onStart();

    /**
     * 重新启动回调
     *
     * @param bundle
     */
    protected abstract void onRestart(Bundle bundle);

    /**
     * 暂停回调
     */
    protected abstract void onStop();

    /**
     * 销毁回调
     *
     * @param bundle
     */
    protected abstract void onDestroy(Bundle bundle);

    /**
     * 获取父App类名
     *
     * @return 父App类名
     */
    public String getParentAppClassName() {
        return mParentAppClassName;
    }

    /**
     * 设置父App类名
     *
     * @param parentAppClassName 父App类名
     */
    public void setParentAppClassName(String parentAppClassName) {
        mParentAppClassName = parentAppClassName;
    }

    public void setAppId(String appId) {
        this.mAppId = appId;
    }

    public String getAppId() {
        return mAppId;
    }

    public void destroy(Bundle params) {
        onDestroy(params);
    }

    /**
     * 依附MicroApplicationContext上下文
     *
     * @param applicationContext MicroApplicationContext上下文
     */
    public void attachContext(MicroAppContext applicationContext) {
        mContext = applicationContext;
    }

    /**
     * 获取MicroApplication上下文
     *
     * @return MicroApplication上下文
     */
    public MicroAppContext getMicroAppContext() {
        return mContext;
    }

    /**
     * 获取服务
     *
     * @param className 服务接口类名
     * @return 服务
     */
    @SuppressWarnings("unchecked")
    public <T> T getServiceByInterface(String className) {
        return (T) mContext.findServiceByInterface(className);
    }

    /**
     * @return 获取源Id
     */
    public String getSourceId() {
        return mSourceId;
    }

    /**
     * 设置源Id
     *
     * @param sourceId 源Id
     */
    public void setSourceId(String sourceId) {
        mSourceId = sourceId;
    }

    @Override
    public void saveState(Editor editor) {
    }

    @Override
    public void restoreState(SharedPreferences preferences) {
    }
}
