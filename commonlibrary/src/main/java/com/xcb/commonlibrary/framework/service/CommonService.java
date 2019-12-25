package com.xcb.commonlibrary.framework.service;

import android.os.Bundle;

/**
 * @author jinpingchen
 */
public abstract class CommonService extends MicroService{

    private boolean mIsActivated = false;

    @Override
    public final boolean isActivated() {
        return mIsActivated;
    }

    @Override
    public final void create(Bundle params) {
        onCreate(params);
        mIsActivated = true;
    }

    @Override
    public final void destroy(Bundle params) {
        getMicroAppContext().onDestroyContent(this);
        onDestroy(params);
        mIsActivated = false;
    }
}
