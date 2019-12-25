package com.xcb.commonlibrary.framework.service.ext;

import android.os.Bundle;

import com.xcb.commonlibrary.framework.service.MicroService;
/**
 * @author jinpingchen
 */
public abstract class ExternalService extends MicroService {

    private boolean mIsActivated = false;

    @Override
    public final boolean isActivated() {
        return mIsActivated;
    }

    @Override
    public void create(Bundle params) {
        onCreate(params);
        mIsActivated = true;
    }

    @Override
    public void destroy(Bundle params) {
        //当内存中移除当前服务
        getMicroAppContext().onDestroyContent(this);
        onDestroy(params);
        mIsActivated = false;
    }
}
