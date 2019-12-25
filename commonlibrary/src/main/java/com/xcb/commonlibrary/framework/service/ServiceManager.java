package com.xcb.commonlibrary.framework.service;

import android.content.SharedPreferences;

import com.xcb.commonlibrary.framework.MicroAppContext;


/**
 * @author jinpingchen
 */
public interface ServiceManager {
    /**
     * 依附项目上下文
     * @param applicationContext
     */
    void attachContext(MicroAppContext applicationContext);

    /**
     * 注册服务
     * @param className 服务接口类
     * @param service 服务
     * @param <T>
     * @return
     */
    <T> boolean registerService(String className, T service);

    /**
     *查找服务
     * @param className 服务接口类名
     * @param <T>
     * @return
     */
    <T> T findServiceByInterface(String className);

    /**
     * 销毁回调
     */
    void onDestroyService(MicroService microService);

    /**
     * 退出
     */
    void exit();

    /**
     * 保存状态
     * @param editor
     */
    void saveState(SharedPreferences.Editor editor);

    /**
     * 恢复状态
     * @param preferences
     */
    void restoreState(SharedPreferences preferences);

    /**
     * 注销服务
     * @param interfaceName
     * @param <T>
     * @return
     */
    <T> T unregisterService(String interfaceName);
}
