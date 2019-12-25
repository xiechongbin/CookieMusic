package com.xcb.commonlibrary.framework.service.impl;//package com.mib.mobile.framework.service.impl;
//
//import com.mib.mobile.framework.LauncherAppAgent;
//import com.mib.mobile.framework.MicroAppContext;
//import com.mib.mobile.framework.init.impl.BundleInfo;
//import com.mib.mobile.framework.service.MicroService;
//import com.mib.mobile.framework.service.ServicesLoader;
//
//import java.util.List;
//
///**
// * @author jinpingchen
// */
//public class CommonServiceLoadAgent implements ServicesLoader {
//
//    protected MicroAppContext microAppContext;
//
//    public CommonServiceLoadAgent() {
//        microAppContext = LauncherAppAgent.getInstance().getMicroAppContext();
//    }
//
//    public void preLoad() {
//        // to do something before load service
//    }
//
//    public void postLoad() {
//        //to do something after load service
//    }
//
//    @Override
//    public final void load() {
//        preLoad();
//
//        //初始化各种服务
////        registerService(TaskScheduleService.class.getName(), new TaskScheduleServiceImpl());
////        registerService(TimerTaskService.class.getName(), new TimerTaskServiceImpl());
//
//        //懒加载，看服务的优化级，使用是否频敏
////        registerLazyService(TaskScheduleService.class.getName(), TaskScheduleServiceImpl.class.getName());
//
//        postLoad();
//    }
//
//    @Override
//    public void registerBundle() {
//
//    }
//
//    @Override
//    public List<BundleInfo> getBundleList() {
//        return null;
//    }
//
//    @Override
//    public void afterBootLoad() {
//
//    }
//
//    /**
//     * 注入服务
//     *
//     * @param serviceName
//     * @param service
//     */
//    public final void registerService(String serviceName, MicroService service) {
//        service.attachContext(microAppContext);
//        microAppContext.registerService(serviceName, service);
//    }
//
//    /**
//     * 注入服务-懒加载
//     *
//     * @param serviceName
//     * @param className
//     */
//    public final void registerLazyService(String serviceName, String className) {
//        microAppContext.registerService(serviceName, className);
//    }
//}
