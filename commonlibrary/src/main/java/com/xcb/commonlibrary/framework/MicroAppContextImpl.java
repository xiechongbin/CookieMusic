package com.xcb.commonlibrary.framework;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.ArrayMap;
import android.util.Log;

import com.xcb.commonlibrary.framework.app.ActivityApplication;
import com.xcb.commonlibrary.framework.app.AppDescription;
import com.xcb.commonlibrary.framework.app.AppLoadException;
import com.xcb.commonlibrary.framework.app.MicroApplication;
import com.xcb.commonlibrary.framework.app.service.ApplicationManager;
import com.xcb.commonlibrary.framework.app.service.impl.ApplicationManagerImpl;
import com.xcb.commonlibrary.framework.app.ui.ActivityResponsable;
import com.xcb.commonlibrary.framework.broadcast.LocalBroadcastManagerWrapper;
import com.xcb.commonlibrary.framework.init.impl.BootLoaderImpl;
import com.xcb.commonlibrary.framework.service.MicroService;
import com.xcb.commonlibrary.framework.service.ServiceManager;
import com.xcb.commonlibrary.framework.service.ext.ExternalService;
import com.xcb.commonlibrary.framework.service.ext.ExternalServiceManager;
import com.xcb.commonlibrary.framework.service.impl.ServiceManagerImpl;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author jinpingchen
 */
public class MicroAppContextImpl implements MicroAppContext {

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private AtomicBoolean mInited = new AtomicBoolean(false);

    /**
     * android上下文
     */
    private LauncherAppAgent mApplication;

    /**
     * 当前Activity
     */
    private Activity mActiveActivity;

    /**
     * 服务管理
     */
    private ServiceManager mServiceManager;

    /**
     * 应用管理
     */
    private ApplicationManager mApplicationManager;

    /**
     * 应用内广播管理器
     */
    private LocalBroadcastManagerWrapper mLocalBroadcastManagerWrapper;

    @Override
    @TargetApi(19)
    public WeakReference<Activity> getTopActivity() {
        if (mActiveActivity == null) {
            try {
                Class activityThreadClass = Class.forName("android.app.ActivityThread");
                Object activityThread = activityThreadClass.getMethod("currentActivityThread")
                        .invoke(null);
                Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
                activitiesField.setAccessible(true);
                ArrayMap activities = (ArrayMap) activitiesField.get(activityThread);
                for (Object activityRecord : activities.values()) {
                    Class activityRecordClass = activityRecord.getClass();
                    Field pausedField = activityRecordClass.getDeclaredField("paused");
                    pausedField.setAccessible(true);
                    if (!pausedField.getBoolean(activityRecord)) {
                        Field activityField = activityRecordClass.getDeclaredField("activity");
                        activityField.setAccessible(true);
                        mActiveActivity = (Activity) activityField.get(activityRecord);
                        return new WeakReference<>(mActiveActivity);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            throw new RuntimeException("Didn't find the running activity");
        } else {
            return new WeakReference<>(mActiveActivity);
        }
    }

//    @Override
//    public WeakReference<Activity> getTopActivity() {
//        return new WeakReference<Activity>(mActiveActivity);
//    }

    /**
     * 更新激活的Activity
     * 备注：所有的activity继承了BaseActivity，其onResume中进行了调用
     *
     * @param activity
     */
    @Override
    public void updateActivity(Activity activity) {
        mActiveActivity = activity;
    }


    @Override
    public void attachContext(LauncherAppAgent application) {
        mApplication = application;
        init();
    }

    @Override
    public LauncherAppAgent getApplicationContext() {
        return mApplication;
    }

    /**
     * 初始化
     */
    private void init() {
        //serviceManager初始化(用户管理各种扩展的service)
        mServiceManager = new ServiceManagerImpl();
        mServiceManager.attachContext(this);//为服务管理器绑定项目上下文环境

        //AppManager初始化
        ApplicationManagerImpl applicationManager = new ApplicationManagerImpl();
        applicationManager.attachContext(this);
        mApplicationManager = applicationManager;
        mServiceManager.registerService(ApplicationManager.class.getName(), mApplicationManager);

        //应用内BroadcastReceiver管理器
        mLocalBroadcastManagerWrapper = LocalBroadcastManagerWrapper.getInstance(mApplication);
        mServiceManager.registerService(LocalBroadcastManagerWrapper.class.getName(),
                mLocalBroadcastManagerWrapper);

        //注册服务入口
        new BootLoaderImpl(MicroAppContextImpl.this).load();//初始化并进行加载load
    }

    @Override
    public <T> boolean registerService(String className, T service) {
        return mServiceManager.registerService(className, service);
    }

    @Override
    public <T> boolean registerService(Class clazz, T service) {
        return mServiceManager.registerService(clazz.getName(), service);
    }

    @Override
    public <T> T unregisterService(String interfaceName) {
        return mServiceManager.unregisterService(interfaceName);
    }

    @Override
    public <T> T findServiceByInterface(String className) {
        if (mServiceManager != null) {
            T service = mServiceManager.findServiceByInterface(className);
            if (service == null) {
                service = (T) getExtServiceByInterface(className);
            }
            return service;
        }
        return null;
    }

    @Override
    public <T> T findServiceByInterface(Class clazz) {
        if (mServiceManager != null) {
            T service = mServiceManager.findServiceByInterface(clazz.getName());
            if (service == null) {
                service = (T) getExtServiceByInterface(clazz.getName());
            }
            return service;
        }
        return null;
    }

    @Override
    public <T extends ExternalService> T getExtServiceByInterface(String className) {
        if (mServiceManager != null) {
            //ExternalServiceManager在Application中就加载了，具体在BootLoaderImpl
            ExternalServiceManager exm = mServiceManager
                    .findServiceByInterface(ExternalServiceManager.class.getName());
            if (exm != null) {
                return (T) exm.getExternalService(className);//外部扩展服务管理器去获得服务
            }
        }
        return null;
    }

    @Override
    public void startActivity(MicroApplication microApplication, String className) {
        //mActiveActivity加载入口的app时，具体在Activity(必须继承BaseActivity)的onResume更新这个数据
        if (mActiveActivity == null) {
            return;
        }
        if (microApplication instanceof ActivityApplication) {
            Class<?> clazz = getActivityClass(className);
            Intent intent = new Intent(mActiveActivity, clazz);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
            intent.putExtra("app_id", microApplication.getAppId());
            microApplication.setIsPrevent(true);
            mActiveActivity.startActivity(intent);
        } else {
            throw new RuntimeException("Service can't start activity");
        }
    }

    @Override
    public void startActivity(MicroApplication microApplication, Intent intent) {
        if (mActiveActivity == null) {
            return;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        intent.putExtra("app_id", microApplication.getAppId());
        microApplication.setIsPrevent(true);
        mActiveActivity.startActivity(intent);
    }

    private Class<?> getActivityClass(String className) {
        Class<?> clazz;
        try {
            ClassLoader classLoad = mApplication.getBaseContext().getClassLoader();
            clazz = classLoad.loadClass(className);
            if (clazz == null) {
                throw new ActivityNotFoundException("entry class must be set.");
            }
        } catch (ClassNotFoundException e) {
            throw new ActivityNotFoundException(e == null ? "" : e.getMessage());
        }
        return clazz;
    }


    @Override
    public MicroApplication findTopRunningApp() {
        return mApplicationManager.getTopRunningApp();
    }

    @Override
    public void startApp(final String sourceAppId, final String targetAppId, final Bundle params) throws AppLoadException {
        mHandler.post(new Runnable() {//放到主线程启动
            @Override
            public void run() {
                try {
                    mApplicationManager.startApp(sourceAppId, targetAppId, params);
                } catch (AppLoadException e) {
                    Log.e("MicroAppContextImpl", e.getMessage());
                }
            }
        });
    }

    @Override
    public void finishApp(final String sourceAppId, final String targetAppId, final Bundle params) {
        //ui线程
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mApplicationManager.finishApp(sourceAppId, targetAppId, params);
            }
        });
    }

    @Override
    public MicroApplication findAppById(String appId) {
        return mApplicationManager.findAppById(appId);
    }

    @Override
    public AppDescription findDescriptionById(String appId) {
        return mApplicationManager.findDescriptionById(appId);
    }

    @Override
    public void startActivityForResult(MicroApplication microApplication, String className, int requestCode) {
        if (mActiveActivity == null) {
            return;
        }
        if (microApplication instanceof ActivityApplication) {
            Class<?> clazz = getActivityClass(className);
            Intent intent = new Intent(mActiveActivity, clazz);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
            intent.putExtra("app_id", microApplication.getAppId());
            microApplication.setIsPrevent(true);
            mActiveActivity.startActivityForResult(intent, requestCode);
        } else {
            throw new RuntimeException("Service can't start activity");
        }
    }

    @Override
    public void startActivityForResult(MicroApplication microApplication, Intent intent, int requestCode) {
        if (mActiveActivity == null) {
            return;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        intent.putExtra("app_id", microApplication.getAppId());
        microApplication.setIsPrevent(true);
        mActiveActivity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void startExtActivityForResult(MicroApplication microApplication, Intent intent, int requestCode) {
        if (mActiveActivity == null) {
            return;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        mActiveActivity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void startExtActivity(MicroApplication microApplication, Intent intent) {
        if (mActiveActivity == null) {
            return;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        mActiveActivity.startActivity(intent);
    }

    @Override
    public void exit() {
        mApplicationManager.exit();
        clearState();
        // Try everything to make sure this process goes away.
        // References from com.android.internal.os.RuntimeInit.java
        // public static void wtf(String tag, Throwable t)
//        Process.killProcess(Process.myPid());
//        System.exit(10);
    }

    @Override
    public void toast(String msg, int period) {
        if (mActiveActivity instanceof ActivityResponsable) {
            ((ActivityResponsable) mActiveActivity).toast(msg, period);
        } else {
            throw new IllegalAccessError("current Activity must be ActivityInterface。");
        }
    }

    @Override
    public void alert(String title, String msg, String positive, DialogInterface.OnClickListener positiveListener, String negative, DialogInterface.OnClickListener negativeListener) {
        if (mActiveActivity instanceof ActivityResponsable) {

            //2.x系统，当前Activity没有焦点，alert无法正常弹出，应用会进入假死状态，所以在此增加一个条件判断。
//        	if( mActiveActivity.hasWindowFocus() ){
            ((ActivityResponsable) mActiveActivity).alert(title, msg, positive, positiveListener,
                    negative, negativeListener);
//        	}

        } else {
            throw new IllegalAccessError("current Activity must be ActivityInterface。");
        }
    }

    @Override
    public void saveState() {
        SharedPreferences preferences = LauncherAppAgent.getInstance().getSharedPreferences(
                "_share_tmp_", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("@@", true);//标识进程是否被非法关闭
        mApplicationManager.saveState(editor);
        mServiceManager.saveState(editor);
        editor.apply();
    }

    @Override
    public void restoreState() {
        SharedPreferences preferences = LauncherAppAgent.getInstance().getSharedPreferences(
                "_share_tmp_", Context.MODE_PRIVATE);
        mApplicationManager.restoreState(preferences);
        mServiceManager.restoreState(preferences);
    }

    @Override
    public void clearState() {
        SharedPreferences preferences = LauncherAppAgent.getInstance().getSharedPreferences(
                "_share_tmp_", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().apply();
    }

    @Override
    public boolean hasInited() {
        return mInited.get();
    }

    @Override
    public void onWindowFocus(MicroApplication application) {
        mApplicationManager.clearTop(application);
    }

    @Override
    public void onDestroyContent(MicroContent microContent) {
        if (microContent instanceof MicroService) {
            mServiceManager.onDestroyService((MicroService) microContent);
        }
    }

    @Override
    public void loadBundle(String bundleName) {
        // TODO: 18-1-30 加载某个bundle
    }

    @Override
    public Resources getResourcesByBundle(String bundleName) {
        return null;
    }
}
