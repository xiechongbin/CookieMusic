package com.xcb.commonlibrary.framework.init;


import com.xcb.commonlibrary.framework.MicroAppContext;

/**
 * @author jinpingchen
 */
public interface BootLoader {

    /**
     * 获取上下文
     *
     * @return
     */
    MicroAppContext getContext();

    /**
     * 加载
     */
    void load();

}
