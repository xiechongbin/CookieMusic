package com.xcb.commonlibrary.framework.app.ui;

import android.app.Activity;
import android.util.Log;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Activity记录器
 * <p>
 *
 * @author jinpingchen
 */
public class ActivityCollections {
    private static ActivityCollections mInstance;

    private Map<String, ActivityRef> mActivityRefs;

    private ActivityCollections() {
        mActivityRefs = new HashMap<>();
    }

    /**
     * 获取Activity记录器
     *
     * @return Activity记录器
     */
    public static synchronized ActivityCollections getInstance() {
        if (mInstance == null) {
            throw new IllegalStateException(
                    "ActivityCollections must be create by call createInstance()");
        }
        return mInstance;
    }

    /**
     * 创建Activity记录器
     *
     * @return Activity记录器
     */
    public static synchronized ActivityCollections createInstance() {
        if (mInstance == null) {
            mInstance = new ActivityCollections();
        }
        return mInstance;
    }

    /**
     * 添加一条新的Activity记录
     */
    public void recordActivity(final Activity activity) {
        String key = activity.getClass().getName();
        ActivityRef reference;
        if (mActivityRefs.containsKey(key)) {
            reference = mActivityRefs.get(key);
            if (reference.get() != null) {
                Log.e("ActivityCollections", "Activity[" + key + "] has one more instance.");
            }
        }

        reference = new ActivityRef(activity);
        mActivityRefs.put(key, reference);
        dump();//dump activity 记录
    }

    private void dump() {
        Log.v("ActivityCollections", mActivityRefs.toString());
    }

    private class ActivityRef extends WeakReference<Activity> {

        public ActivityRef(Activity r) {
            super(r);
        }

        public ActivityRef(Activity r, ReferenceQueue<? super Activity> q) {
            super(r, q);
        }

        @Override
        public String toString() {
            if (get() != null) {
                return get().toString();
            }
            return super.toString();
        }
    }
}
