package com.xcb.commonlibrary.framework.init.impl;

import android.app.Application;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.webkit.CookieSyncManager;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.xcb.commonlibrary.framework.MicroAppContext;
import com.xcb.commonlibrary.framework.app.ui.ActivityCollections;
import com.xcb.commonlibrary.framework.init.BootLoader;
import com.xcb.commonlibrary.framework.msg.MsgCodeConstants;
import com.xcb.commonlibrary.framework.service.ServicesLoader;
import com.xcb.commonlibrary.framework.service.ext.ExternalServiceManager;
import com.xcb.commonlibrary.framework.service.ext.ExternalServiceManagerImpl;
import com.xcb.commonlibrary.framework.utils.ReflectUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jinpingchen
 */
public class BootLoaderImpl implements BootLoader {

    private List<BundleInfo> mBundles;

    private MicroAppContext microAppContext;
    private ServicesLoader mServiceLoader;

    public BootLoaderImpl(MicroAppContext microAppContext) {
        this.microAppContext = microAppContext;
        mBundles = new ArrayList<>();
    }

    @Override
    public MicroAppContext getContext() {
        return microAppContext;
    }

    @Override
    public void load() {
        Application application = microAppContext.getApplicationContext();//获取android上下文
        //读取metaData
        String agentCommonServiceLoad = null;
        try {
            ApplicationInfo applicationInfo = application.getPackageManager().getApplicationInfo(application.getPackageName(), PackageManager.GET_META_DATA);
            agentCommonServiceLoad = applicationInfo.metaData.getString("agent.commonservice");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(agentCommonServiceLoad)) {
            agentCommonServiceLoad = "com.mib.mobile.bizservice.CommonServiceLoadAgent";
        }

        //step1. 首先初始化外部服务管理,这个服务可重要
        ExternalServiceManager externalServiceManager = new ExternalServiceManagerImpl();
        externalServiceManager.attachContext(microAppContext);//绑定项目上下文
        //这里必须注册ExternalServiceManager服务，否则所有的扩展服务都不能被初始化
        microAppContext.registerService(ExternalServiceManager.class.getName(), externalServiceManager);

        //step2. 然后初始化框架中提供的所有基础服务
        try {
            Class<?> clazz = application.getClassLoader().loadClass(agentCommonServiceLoad);
            mServiceLoader = (ServicesLoader) ReflectUtil.getInstance(clazz);
            mServiceLoader.load();
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }

        ActivityCollections.createInstance();

        if (Runtime.getRuntime().availableProcessors() > 2) {//系统是多处理器时

            final HandlerThread loadServiceThread = new HandlerThread("name");
            loadServiceThread.start();
            Handler handler = new Handler(loadServiceThread.getLooper());

            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        //WebView利用CookieSyncManager获取活设置Cookies的策略
                        //http://blog.csdn.net/stzy00/article/details/50586979
                        CookieSyncManager.createInstance(microAppContext.getApplicationContext());
                    } catch (Throwable e) {
                    }
                    if (mServiceLoader != null) {
                        mServiceLoader.afterBootLoad();
                    }
                }
            });
            if (mServiceLoader != null) {
                mServiceLoader.registerBundle();
                mBundles = mServiceLoader.getBundleList();
            }
            //load其他bundle(Module)中的服务,这里加载的服务都是懒加载的服务
            if (mBundles != null && !mBundles.isEmpty()) {
                new BundleLoadHelper(this).loadBundleDefinitions(mBundles);
            }
        }

        // 初始化完成
        Intent intent = new Intent();
        intent.setAction(MsgCodeConstants.FRAMEWORK_INITED);
        LocalBroadcastManager.getInstance(
                microAppContext.getApplicationContext())
                .sendBroadcast(intent);
    }
}
