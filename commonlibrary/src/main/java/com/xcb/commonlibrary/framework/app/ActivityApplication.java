package com.xcb.commonlibrary.framework.app;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Activity的App
 */
public abstract class ActivityApplication extends MicroApplication {

    final static String TAG = ActivityApplication.class.getSimpleName();

    /**
     * 该App的Activity栈
     */
    private Stack<WeakReference<Activity>> mActivitys;

    private boolean mIsPrevent;
    private AtomicBoolean mIsDestroyed = new AtomicBoolean(false);
    private AtomicBoolean mIsCreated = new AtomicBoolean(false);

    public ActivityApplication() {
        mActivitys = new Stack<WeakReference<Activity>>();
        mIsPrevent = false;
    }

    @Override
    public final void create(Bundle bundle) {
        Log.d(TAG, "microapplication: " + getAppId() + "  create.");
        this.mIsCreated.set(true);
        onCreate(bundle);
    }

    public boolean isCreated() {
        return this.mIsCreated.get();
    }


    @Override
    public final void start() throws AppLoadException {
        // 如果子类主动返回null，我们把启动入口Activity的工作交给子类自己处理，不抛出异常。
        String className = getEntryClassName();
        if (className != null) {
            try {
                getMicroAppContext().startActivity(this, className);
            } catch (ActivityNotFoundException e) {
                throw new AppLoadException(e);
            }
        }

        Log.d(TAG, "microapplication: " + getAppId() + "  start.");
        onStart();
    }

    @Override
    public final void restart(Bundle bundle) {
        Log.d(TAG, "microapplication: " + getAppId() + "  restart.");
        onRestart(bundle);
    }

    @Override
    public final void stop() {
        Log.d(TAG, "microapplication: " + getAppId() + "  stop.");
        onStop();
    }

    @Override
    public final void destroy(Bundle bundle) {
        Log.d(TAG, "microapplication: " + getAppId() + "  destroy.");

        WeakReference<Activity> reference;
        Activity activity;
        while (!mActivitys.isEmpty() && (reference = mActivitys.pop()) != null) {
            activity = reference.get();
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
        getMicroAppContext().onDestroyContent(this);
        this.mIsCreated.set(false);
        super.destroy(bundle);
    }

    /**
     * Activity入栈
     *
     * @param activity
     */
    public final void pushActivity(Activity activity) {
        if (!mActivitys.isEmpty() && mActivitys.peek().get() == null) {//被恢复的时候替换
            mActivitys.pop();
        }
        WeakReference<Activity> item = new WeakReference<Activity>(activity);
        mActivitys.push(item);
        Log.v(TAG, "pushActivity(): " + activity.getComponentName().getClassName());
    }

    /**
     * 移除Activity
     */
    public void removeActivity(Activity activity) {
        WeakReference<Activity> dirtyItem = null;
        for (WeakReference<Activity> item : mActivitys) {
            if (item.get() == null) {
                Log.w(TAG, "activity has be finallized.");
                continue;
            }
            if (item.get() == activity) {
                dirtyItem = item;
                break;
            }
        }
        mActivitys.remove(dirtyItem);
        Log.d(TAG, "remove Activity:" + activity.getClass().getName());
        if (mActivitys.isEmpty() && !mIsPrevent) {
            destroy(null);
        }
    }

    /**
     * 通过Hashcode查找Activity
     *
     * @param code Hashcode
     * @return
     */
    public Activity findActivityByHashcode(int code) {
        for (WeakReference<Activity> reference : mActivitys) {
            Activity activity = reference.get();
            if (activity == null) {
                continue;
            }
            if (activity.hashCode() == code) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 获得焦点
     */
    public void windowFocus() {
        getMicroAppContext().onWindowFocus(this);
    }

    /**
     * 获取栈顶Activity
     */
    public Activity getTopActivity() {
        if (mActivitys.isEmpty()) {
            return null;
        }
        return mActivitys.peek().get();
    }

    @Override
    public void setIsPrevent(boolean isPrevent) {
        mIsPrevent = isPrevent;
    }

    @Override
    public void saveState(Editor editor) {
        editor.putInt(getAppId() + ".stack", mActivitys.size());
    }

    @Override
    public void restoreState(SharedPreferences preferences) {
        int num = preferences.getInt(getAppId() + ".stack", 0);
        for (int i = 0; i < num; i++) {
            mActivitys.push(new WeakReference<Activity>(null));
        }
    }
}
