package com.xcb.commonlibrary.framework.service;

import com.xcb.commonlibrary.framework.init.impl.BundleInfo;

import java.util.List;

/**
 * @author jinpingchen
 */
public interface ServicesLoader {

    void load();

    void registerBundle();

    List<BundleInfo> getBundleList();

    /**
     * 在bootfinish之后执行
     */
    public void afterBootLoad();
}
