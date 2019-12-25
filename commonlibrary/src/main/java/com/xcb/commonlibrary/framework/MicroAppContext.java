package com.xcb.commonlibrary.framework;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;


import com.xcb.commonlibrary.framework.app.AppDescription;
import com.xcb.commonlibrary.framework.app.AppLoadException;
import com.xcb.commonlibrary.framework.app.MicroApplication;
import com.xcb.commonlibrary.framework.service.ext.ExternalService;

import java.lang.ref.WeakReference;

/**
 * @author jinpingchen
 */
public interface MicroAppContext {

    /**
     * 返回栈顶的Activity
     *
     * @return 栈顶Activity
     */
    WeakReference<Activity> getTopActivity();

    /**
     * 更新当前Activity
     * 备注：所有的activity继承了BaseActivity，其onResume中进行了调用
     *
     * @param activity Activity
     */
    void updateActivity(Activity activity);

    /**
     * 依附android上下文
     *
     * @param application
     */
    void attachContext(LauncherAppAgent application);

    /**
     * 获取android上下文
     *
     * @return
     */
    LauncherAppAgent getApplicationContext();

    /**
     * 获得当前运行的顶部app
     *
     * @return
     */
    MicroApplication findTopRunningApp();

    /**
     * 注册服务
     *
     * @param className 服务接口类名
     * @param service   服务
     * @param <T>
     * @return
     */
    <T> boolean registerService(String className, T service);

    /**
     * 注册服务
     *
     * @param clazz 服务接口类名
     * @param service   服务
     * @param <T>
     * @return
     */
    <T> boolean registerService(Class clazz, T service);

    /**
     * 反注册服务
     *
     * @param interfaceName 服务接口类名
     * @param <T>
     * @return
     */
    <T> T unregisterService(String interfaceName);

    /**
     * 查找服务
     *
     * @param className 服务接口类名
     * @param <T>
     * @return
     */
    <T> T findServiceByInterface(String className);

    /**
     * 查找服务
     *
     * @param clazz 服务接口类名
     * @param <T>
     * @return
     */
    <T> T findServiceByInterface(Class clazz);

    /**
     * 通过服务接口获取外部服务
     *
     * @param className
     * @param <T>
     * @return
     */
    <T extends ExternalService> T getExtServiceByInterface(String className);


    /**
     * 启动App
     *
     * @param sourceAppId 源App唯一Id
     * @param targetAppId 目标App唯一Id
     * @param params      参数
     * @throws AppLoadException
     */
    void startApp(String sourceAppId, String targetAppId, Bundle params)
            throws AppLoadException;

    /**
     * 关闭App
     *
     * @param sourceAppId 源App唯一Id
     * @param targetAppId 目标App唯一Id
     * @param params      参数
     */
    void finishApp(String sourceAppId, String targetAppId, Bundle params);


    /**
     * 查找App
     *
     * @param appId App唯一Id
     * @return MicroApplication
     */
    MicroApplication findAppById(String appId);

    /**
     * 根据app id获取ApplicationDescription
     *
     * @param appId
     * @return
     */
    AppDescription findDescriptionById(String appId);

    /**
     * 启动Activity
     *
     * @param microApplication
     * @param className        类名
     */
    void startActivity(MicroApplication microApplication, String className);

    /**
     * 启动Activity
     *
     * @param microApplication
     * @param intent           Intent
     */
    void startActivity(MicroApplication microApplication, Intent intent);

    /**
     * 启动Activity
     *
     * @param microApplication
     * @param className        类名
     * @param requestCode      请求码
     */
    void startActivityForResult(MicroApplication microApplication, String className,
                                int requestCode);

    /**
     * 启动Activity
     *
     * @param microApplication
     * @param intent           Intent
     * @param requestCode      请求码
     */
    void startActivityForResult(MicroApplication microApplication, Intent intent,
                                int requestCode);

    /**
     * 启动外部Activity
     *
     * @param microApplication
     * @param intent
     * @param requestCode
     */
    void startExtActivityForResult(MicroApplication microApplication, Intent intent,
                                   int requestCode);

    /**
     * 启动外部Activity
     *
     * @param microApplication
     * @param intent
     */
    void startExtActivity(MicroApplication microApplication, Intent intent);

    /**
     * microApplication获得窗口焦点
     *
     * @param application
     */
    void onWindowFocus(MicroApplication application);


    /**
     * 退出
     */
    void exit();

    /**
     * TOAST
     *
     * @param msg    消息
     * @param period 时长
     */
    void toast(String msg, int period);

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
    void alert(String title, String msg, String positive,
               DialogInterface.OnClickListener positiveListener, String negative,
               DialogInterface.OnClickListener negativeListener);


    /**
     * 销毁
     *
     * @param microContent
     */
    void onDestroyContent(MicroContent microContent);

    /**
     * 保存状态
     */
    void saveState();

    /**
     * 恢复状态
     */
    void restoreState();

    /**
     * 清楚状态
     */
    void clearState();

    /**
     * 是否已经初始化
     *
     * @return 是否已经初始化
     */
    boolean hasInited();

    void loadBundle(String bundleName);

    Resources getResourcesByBundle(String bundleName);
}
