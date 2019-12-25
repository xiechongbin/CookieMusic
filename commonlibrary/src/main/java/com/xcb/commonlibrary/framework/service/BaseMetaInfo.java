package com.xcb.commonlibrary.framework.service;

import com.xcb.commonlibrary.framework.app.AppDescription;
import com.xcb.commonlibrary.framework.broadcast.BroadcastReceiverDescription;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jinpingchen
 */
public abstract class BaseMetaInfo {

    //独立的一个一个app(Module或者Bundle)列表集合
    public List<AppDescription> applications = new ArrayList<AppDescription>();
    //非android系统提供的服务集合
    public List<ServiceDescription> services = new ArrayList<ServiceDescription>();
    //系统监控级广播集合
    public List<BroadcastReceiverDescription> broadcastReceivers = new ArrayList<>();
    //当前app入口
    public String entry = null;

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public List<AppDescription> getApplications() {
        return applications;
    }

    public void setApplications(List<AppDescription> applications) {
        this.applications = applications;
    }

    public void addApplication(AppDescription applicationDescription) {
        if (applications == null) {
            applications = new ArrayList<>();
        }
        applications.add(applicationDescription);
    }


    public List<ServiceDescription> getServices() {
        return services;
    }

    public void setServices(List<ServiceDescription> services) {
        this.services = services;
    }

    public void addService(ServiceDescription serviceDescription) {
        if (null == services) {
            services = new ArrayList<>();
        }
        services.add(serviceDescription);
    }

    public List<BroadcastReceiverDescription> getBroadcastReceivers() {
        return broadcastReceivers;
    }

    public void setBroadcastReceivers(List<BroadcastReceiverDescription> broadcastReceivers) {
        this.broadcastReceivers = broadcastReceivers;
    }

    public void addBroadcastReceiver(BroadcastReceiverDescription broadcastReceiverDescription) {
        if (broadcastReceivers == null) {
            broadcastReceivers = new ArrayList<>();
        }
        broadcastReceivers.add(broadcastReceiverDescription);
    }

}
