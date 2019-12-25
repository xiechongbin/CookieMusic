package com.xcb.commonlibrary;

import java.lang.ref.WeakReference;

/**
 * 以后全部的Presenter 必须是这个类的子类
 *
 * @author WJ
 * @date 2018/6/28
 */
public abstract class BasePresenter<V extends BaseView> {

    private WeakReference<V> mWeakRef;

    public BasePresenter(V mView) {
        mWeakRef = new WeakReference<>(mView);
    }

    public final V getView() {
        if (mWeakRef == null) {
            return null;
        }
        return mWeakRef.get();
    }

    public void detachView() {
        if (mWeakRef != null) {
            mWeakRef.clear();
            mWeakRef = null;
        }
    }

}
