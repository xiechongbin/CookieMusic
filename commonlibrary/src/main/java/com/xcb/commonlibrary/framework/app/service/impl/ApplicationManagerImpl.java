package com.xcb.commonlibrary.framework.app.service.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.xcb.commonlibrary.framework.MicroAppContext;
import com.xcb.commonlibrary.framework.app.AppDescription;
import com.xcb.commonlibrary.framework.app.AppLoadException;
import com.xcb.commonlibrary.framework.app.MicroApplication;
import com.xcb.commonlibrary.framework.app.service.ApplicationManager;
import com.xcb.commonlibrary.framework.utils.ReflectUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * 应用管理
 *
 * @author jinpingchen
 */
public class ApplicationManagerImpl implements ApplicationManager {

    final static String TAG = ApplicationManager.class.getSimpleName();

    /**
     * 应用栈
     */
    private Stack<MicroApplication> mApps;
    /**
     * 应用映射
     * key:appId
     * value:MicroApplication
     */
    private Map<String, MicroApplication> mAppsMap;
    /**
     * 应用描述
     */
    private List<AppDescription> mAppsDes;
    /**
     * 入口应用名
     */
    private String mEntryApp;

    /**
     * MicroApplication上下文
     */
    private MicroAppContext mMicroAppContext;

    public ApplicationManagerImpl() {
        mApps = new Stack<>();
        mAppsMap = new HashMap<>();
        mAppsDes = new ArrayList<>();
    }

    @Override
    public synchronized void startApp(String sourceAppId, String targetAppId, Bundle params) {
        if (targetAppId == null) {
            throw new RuntimeException("targetAppId should not be null");
        }
        Log.v(TAG, "startApp() sourceAppId: " + sourceAppId + " targetAppId: " + targetAppId + " currentThread: " + Thread.currentThread().getId());

        //判断源app合法性：源app还未初始化
        if (!mAppsMap.containsKey(sourceAppId)) {
            Log.w(TAG, sourceAppId + " is not a App or had not start up");
        }
        //目标app已经初始化过
        if (mAppsMap.containsKey(targetAppId)) {
            doRestart(targetAppId, params);
            return;
        }

        //优先以apk方式启动：这种方式被废弃
        if (startApkApp(sourceAppId, targetAppId, params)) {
            return;
        }

        //以nativeapp方式启动
        if (startNativeApp(sourceAppId, targetAppId, params)) {
            return;
        }

        //以webapp方式启动:这种方式也基本被废弃
        if (startWebApp(sourceAppId, targetAppId, params)) {
            return;
        }

//        LegacyService legacyService = mMicroAppContext.getExtServiceByInterface(LegacyService.class.getName());
//        legacyService.sendMessage(sourceAppId, targetAppId, "startapp", deSearialBundle(params));        
    }

    private boolean startApkApp(String sourceAppId, String targetAppId, Bundle params) {
        Context context = mMicroAppContext.getApplicationContext();
        String mPath = context.getFilesDir().getAbsolutePath() + "/apps/";

        String archiveFilePath = mPath + targetAppId + ".apk";// data/data/pkg/DemoApp.apk
//		if(new File(archiveFilePath).exists()){
//			WeakReference<Activity> topAcRef = mMicroAppContext.getTopActivity();
//			if(topAcRef != null && topAcRef.get() != null){
//				AndroidAppLoader nativeAppLoader = new AndroidAppLoader(topAcRef.get());
//				return nativeAppLoader.performLaunch(archiveFilePath, params);
//			}
//		}
        return false;
    }

    /**
     * 这个方法很好，模仿了android的activity的生命周期执行逻辑
     *
     * @param sourceAppId
     * @param targetAppId
     * @param params
     * @return
     */
    private boolean startNativeApp(String sourceAppId, String targetAppId, Bundle params) {
        //获取目的应用描述
        AppDescription targetAppDescription = findDescriptionById(targetAppId);
        //非老业务
        if (targetAppDescription != null) {
            MicroApplication app;
            try {
                //获取目的应用实例
                app = createNativeApp(targetAppDescription, params);
                //此时目的应用变成源应用
                app.setSourceId(sourceAppId);
                Log.v(TAG, "createApp() completed: " + targetAppId);

                if (!mApps.isEmpty()) {
                    mApps.peek().stop();//取栈顶元素不出栈
                }
                mApps.push(app);//入栈
                mAppsMap.put(targetAppId, app);

                app.start();
            } catch (AppLoadException e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        return false;
    }

    private boolean startWebApp(String sourceAppId, String targetAppId, Bundle params) {
        try {
            MicroApplication app = createWebApp(sourceAppId, targetAppId, params);
            app.setSourceId(sourceAppId);
            Log.v(TAG, "createApp() completed: " + targetAppId);

            if (!mApps.isEmpty()) {
                mApps.peek().stop();
            }
            mApps.push(app);//入栈
            mAppsMap.put(targetAppId, app);

            app.start();
        } catch (AppLoadException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 反序列化Bundle
     *
     * @param bundle
     * @return
     */
    private String deSearialBundle(Bundle bundle) {
        if (bundle == null)
            return null;
        StringBuilder params = new StringBuilder();
        for (String key : bundle.keySet()) {
            params.append(key + "=" + bundle.get(key) + "&");
        }

        return params.length() > 0 ? params.substring(0, params.length() - 1) : null;
    }

    private void doRestart(String targetAppId, Bundle params) {
        Log.v(TAG, "doRestart() targetAppId: " + targetAppId);
        MicroApplication app = mAppsMap.get(targetAppId);
        MicroApplication tmp = null;
        //出栈
        while (app != (tmp = mApps.peek())) {
            mApps.pop();
            Log.v(TAG, "doRestart() pop appId: " + tmp.getAppId());
            tmp.destroy(params);
        }
        //目标app ：restart
        app.restart(params);
    }

    private MicroApplication createNativeApp(AppDescription targetAppDes, Bundle params)
            throws AppLoadException {
        Object object;
        //获取到app的类名
        String targetAppClassName = targetAppDes.getClassName();
        try {
            //利用反射机制得到实例化app
            object = ReflectUtil.getInstance(mMicroAppContext.getApplicationContext().getClassLoader(), targetAppClassName);
        } catch (ClassNotFoundException e) {
            throw new AppLoadException("App ClassNotFoundException: " + e);
        } catch (IllegalAccessException e) {
            throw new AppLoadException("App IllegalAccessException: " + e);
        } catch (InstantiationException e) {
            throw new AppLoadException("App InstantiationException: " + e);
        }
        if (!(object instanceof MicroApplication)) {
            throw new AppLoadException("App " + targetAppClassName + " is not a App");
        }
        MicroApplication app = (MicroApplication) object;
        app.setAppId(targetAppDes.getAppId());
        app.attachContext(mMicroAppContext);

        app.create(params);
        return app;
    }

    private MicroApplication createWebApp(String sourceAppId, String targetAppId, Bundle params) throws AppLoadException {

        Context context = mMicroAppContext.getApplicationContext();
        File appPath = new File(context.getFilesDir(), "/apps/" + targetAppId);
        if (!appPath.exists()) {
            throw new AppLoadException("webapp is not exist");
        }
//    	
//    	WebAppLoader loader = new WebAppLoader(context, sourceAppId, targetAppId, appPath.getAbsolutePath() + "/");    	
//    	WebRunTimeApp webRunTimeApp = new WebRunTimeApp();
//    	webRunTimeApp.setWebRunTime(loader.load(params));
//    	
//    	webRunTimeApp.setAppId(targetAppId);
//    	webRunTimeApp.attachContext(mMicroAppContext);
//
//    	webRunTimeApp.create(params);
//        
//    	return webRunTimeApp;    	
        return null;
    }


    @Override
    public void finishApp(String sourceAppId, String targetId, Bundle params) {
        if (!mAppsMap.containsKey(sourceAppId)) {
            Log.w(TAG, sourceAppId + " is not a App");
        }

        //拿到目的app
        MicroApplication app = mAppsMap.get(targetId);
        if (app != null) {
            //调用销毁方法
            app.destroy(params);
        } else {
            Log.d(TAG, "can't find App: " + targetId);
            throw new IllegalStateException("can't find App: " + targetId);
        }
    }

    /**
     * 通过插件名查找App
     *
     * @param appId App类名
     */
    @Override
    public MicroApplication findAppById(String appId) {
        return mAppsMap.get(appId);
    }

    /**
     * 通过应用名查找应用描述
     *
     * @param appName 应用名
     * @return 应用描述
     */
    @Override
    public AppDescription findDescriptionByName(String appName) {
        //修复空值异常。
        if (mAppsDes != null && !TextUtils.isEmpty(appName)) {
            for (AppDescription description : mAppsDes) {
                if (description != null) {
                    if (appName.equalsIgnoreCase(description.getName())) {
                        return description;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public AppDescription findDescriptionById(String appId) {
        for (AppDescription description : mAppsDes) {
            if (appId.equalsIgnoreCase(description.getAppId())) {
                return description;
            }
        }
        return null;
    }

    /**
     * 添加应用描述
     *
     * @param description 应用描述
     */
    @Override
    public void addDescription(AppDescription description) {
        mAppsDes.add(description);
    }

    /**
     * 添加应用描述
     *
     * @param descriptions 应用描述s
     */
    @Override
    public void addDescription(List<AppDescription> descriptions) {
        mAppsDes.addAll(descriptions);
    }

    /**
     * 启动入口APP
     *
     * @throws AppLoadException
     */
    @Override
    public void startEntryApp(Bundle params) throws AppLoadException {
        AppDescription description = findDescriptionByName(mEntryApp);
        String appId = description.getAppId();
        startApp(null, appId, params);
    }

    /**
     * 设置入口应用名
     *
     * @param appName 应用名
     */
    @Override
    public void setEntryAppName(String appName) {
        mEntryApp = appName;
    }

    /**
     * 依附MicroApplication上下文
     *
     * @param applicationContext 依附MicroApplication上下文
     */
    public void attachContext(MicroAppContext applicationContext) {
        mMicroAppContext = applicationContext;
    }

    @Override
    public void exit() {
        //所有应用出栈
        while (!mApps.isEmpty()) {
            MicroApplication microApplication = mApps.pop();
            Log.v(TAG, "exit() pop appId: " + microApplication.getAppId());
            //销毁，执行一些内存释放的操作
            microApplication.destroy(null);
        }
        //应用映射清空
        mAppsMap.clear();
    }

    @Override
    public void clear() {
        //应用栈出栈
        mApps.clear();
        //应用映射map清空
        mAppsMap.clear();
    }

    @Override
    public void onDestroyApp(MicroApplication microApplication) {
        mApps.remove(microApplication);
        mAppsMap.remove(microApplication.getAppId());
        Log.v(TAG, "onDestroyApp() pop appId: " + microApplication.getAppId());
    }

    @Override
    public void clearTop(MicroApplication microApplication) {
        MicroApplication tmp;
        if (mApps == null) {
            return;
        }
        if (mApps.size() == 0) {
            return;
        }
        while (microApplication != (tmp = mApps.peek())) {
            mApps.pop();
            Log.v(TAG, "clearTop() pop appId: " + tmp.getAppId());
            mAppsMap.remove(tmp.getAppId());
            break;
        }
    }

    @Override
    public MicroApplication getTopRunningApp() {
        if (!mApps.isEmpty()) {
            return mApps.peek();
        }
        return null;
    }

    @Override
    public void saveState(Editor editor) {
        List<String> appIds = new ArrayList<String>();
        for (MicroApplication application : mApps) {
            String appId = application.getAppId();
            appIds.add(appId);
            application.saveState(editor);
        }
        editor.putString("ApplicationManager", JSON.toJSONString(appIds));
        editor.putString("ApplicationManager.EntryApp", mEntryApp);
    }

    @Override
    public void restoreState(SharedPreferences preferences) {
        mEntryApp = preferences.getString("ApplicationManager.EntryApp", null);
        String string = preferences.getString("ApplicationManager", null);
        if (null != string) {
            List<String> appIds = JSON.parseArray(string, String.class);
            for (String appId : appIds) {
                try {
                    AppDescription targetAppDes = findDescriptionById(appId);
                    MicroApplication application = createNativeApp(targetAppDes, null);
                    application.setSourceId(appId);
                    application.restoreState(preferences);

                    mApps.push(application);//入栈
                    Log.v(TAG, "restoreState() App pushed: " + application.getAppId());
                    mAppsMap.put(appId, application);
                } catch (AppLoadException exception) {
//                    Log.e(TAG, exception);
                }
            }
        }
    }

}
