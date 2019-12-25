package com.xcb.commonlibrary.framework.service;

import android.os.Bundle;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author jinpingchen
 * @date 2019-10-22 16:37
 * 任务调度服务
 */
public abstract class TaskScheduleService extends CommonService {

    /**
     * 串行执行
     *
     * @param command    Runnable
     * @param threadName ThreadName
     */
    public abstract void serialExecute(Runnable command, String threadName);

    /**
     * 并行执行
     *
     * @param command    Runnable
     * @param threadName ThreadName
     */
    public abstract void parallelExecute(Runnable command, String threadName);

    public abstract ScheduledFuture<?> schedule(Runnable task, String threadName, long delay, TimeUnit unit);

    public abstract ScheduledFuture<?> scheduleAtFixedRate(Runnable task, String threadName, long initialDelay, long period, TimeUnit unit);

    public abstract ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, String threadName, long initialDelay, long delay, TimeUnit unit);

    /**
     * 根据调度特点及典型的使用场景划分的executor类型
     *
     * @see TaskScheduleService#acquireExecutor(ScheduleType)
     * <p>
     * 以下调度类型以其最适合被使用的场景来命名，
     * 但调用者需要客观评估怎么合适地使用各种线程池，比如登陆RPC虽然是网络特定的后台任务，
     * 但优先级非常高，一般建议放在URGENT类型的线程池中
     */
    public static enum ScheduleType {
        URGENT, /* 为前台UI所依赖，优先级最高，不能容忍排队 */
        NORMAL, /* 普通不太紧急，可以容忍排队的后台任务 */
        IO, /* 文件IO类操作，持久化任务，耗时可以预计，要么不久成功，要么发生异常 */
    }

    /**
     * 获取期望的Executor实例(线程安全)
     *
     * @return
     */
    public abstract ThreadPoolExecutor acquireExecutor(ScheduleType type);

    /**
     * 获取支持调度的Executor实例
     * <p>
     * Note: 在所有需要Timer的地方都应该迁移至使用本接口
     *
     * @return
     */
    public abstract ScheduledThreadPoolExecutor acquireScheduledExecutor();

    /**
     * 获取OrderedExecutor实例
     * <p>
     * 提交给OrderedExecutor的拥有相同KEY的Task会保证有序串行执行（但不一定全在同一线程），不同的KEY对应的Task之间会并发
     *
     * @return
     */
//    public abstract OrderedExecutor acquireOrderedExecutor();

    /**
     * 获取OrderedExecutor实际的执行线程池
     * <p>
     * 供调用方获取内部线程池的执行状态
     *
     * @return
     */
    public abstract ThreadPoolExecutor getOrderedExecutorCore();

    /**
     * invoke to collect the runtime statistics of TaskScheduleService
     * Note: expect to be used for profiling purpose
     *
     * @return
     */
    public abstract Bundle dump();

}