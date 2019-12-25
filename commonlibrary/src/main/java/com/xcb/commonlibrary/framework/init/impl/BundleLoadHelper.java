package com.xcb.commonlibrary.framework.init.impl;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.util.Log;

import com.xcb.commonlibrary.framework.MicroAppContext;
import com.xcb.commonlibrary.framework.app.AppDescription;
import com.xcb.commonlibrary.framework.app.service.ApplicationManager;
import com.xcb.commonlibrary.framework.broadcast.BroadcastReceiverDescription;
import com.xcb.commonlibrary.framework.broadcast.LocalBroadcastManagerWrapper;
import com.xcb.commonlibrary.framework.init.BootLoader;
import com.xcb.commonlibrary.framework.service.BaseMetaInfo;
import com.xcb.commonlibrary.framework.service.ServiceDescription;
import com.xcb.commonlibrary.framework.service.ext.ExternalServiceManager;

import java.util.List;

/**
 * @author jinpingchen
 */
public class BundleLoadHelper {

    private BootLoader mBootLoader;
    private MicroAppContext mMicroAppContext;
    private ApplicationManager mApplicationManager;
    private ExternalServiceManager mExternalServiceManager;

    /**
     * 应用内广播管理器
     */
    private LocalBroadcastManagerWrapper mLocalBroadcastManagerWrapper;

    public BundleLoadHelper(BootLoader bootLoader) {
        mBootLoader = bootLoader;
        mMicroAppContext = mBootLoader.getContext();
        mApplicationManager = mMicroAppContext.findServiceByInterface(ApplicationManager.class.getName());
        mExternalServiceManager = mMicroAppContext.findServiceByInterface(ExternalServiceManager.class.getName());
        mLocalBroadcastManagerWrapper = mMicroAppContext.findServiceByInterface(LocalBroadcastManagerWrapper.class.getName());
    }

    public void loadBundleDefinitions(List<BundleInfo> bundleList) {
        try {
            for (BundleInfo bundle : bundleList) {
                loadBundle(bundle);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loadBundle(BundleInfo bundle) throws ClassNotFoundException, NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException {
        BaseMetaInfo baseMetaInfo = null;
        try {
            //这里是为了获取在module下的MetaInfo文件类，此类中注册了服务
            String pkg = bundle.getPackageName();
            baseMetaInfo = (BaseMetaInfo) Class.forName(pkg + ".MetaInfo").newInstance();
            Log.d("xxm", "BundleLoadHelper loadBundle called!" + (pkg + ".MetaInfo"));
        } catch (Exception e) {
            //打印出错信息
        }

        if (null == baseMetaInfo) {
            return;
        }

        // Load application
        List<AppDescription> apps = baseMetaInfo.getApplications();
        if (null != apps && apps.size() > 0) {
            Log.d("xxm", "BundleLoadHelper loadBundle AppDescription" + apps);
            mApplicationManager.addDescription(apps);
            // 设置应用入口点
            if (bundle.isEntry()) {
                String entry = baseMetaInfo.getEntry();
                if (null != entry) {
                    mApplicationManager.setEntryAppName(entry);
                }
            }
        }

        // Load service
        List<ServiceDescription> services = baseMetaInfo.getServices();
        Log.d("xxm", "BundleLoadHelper loadBundle services" + services);
        if (null != services && services.size() > 0) {
            for (ServiceDescription serviceDescription : services) {
                Log.d("xxm", "BundleLoadHelper loadBundle serviceDescription" + serviceDescription.getClassName());
                if (null == serviceDescription) {
                    continue;
                }
                //外部服务管理器,这里只注册不加载
                mExternalServiceManager.registerExternalServiceOnly(serviceDescription);
            }
        }

        //load broadcast
        List<BroadcastReceiverDescription> broadcastReceivers = baseMetaInfo.getBroadcastReceivers();
        if (null != broadcastReceivers && broadcastReceivers.size() > 0) {
            for (BroadcastReceiverDescription broadcastReceiverDescription : broadcastReceivers) {
                if (null == broadcastReceiverDescription.getClassName()) {
                    Log.e("BundleLoadHelper",
                            "pkg:" + bundle.getPackageName()
                                    + "的MetaInfo中存在className为空的BroadcastReceiverDescription！");
                    continue;
                }
                if (null == broadcastReceiverDescription.getMsgCode()
                        || broadcastReceiverDescription.getMsgCode().length < 1) {
                    Log.e("BundleLoadHelper", broadcastReceiverDescription.getClassName()
                            + "订阅的事件为空！");
                    continue;
                }

                BroadcastReceiver broadcastReceiver = null;
                try {
                    broadcastReceiver = (BroadcastReceiver) Class.forName(broadcastReceiverDescription.getClassName()).newInstance();
                } catch (InstantiationException e) {
//                    Log.printStackTraceAndMore(e);
                }

                if (null != broadcastReceiver) {
                    IntentFilter intentFilter = new IntentFilter();
                    for (String msgCode : broadcastReceiverDescription.getMsgCode()) {
                        intentFilter.addAction(msgCode);
                    }
                    mLocalBroadcastManagerWrapper.registerReceiver(broadcastReceiver, intentFilter);
                }
            }
        }
    }
}
