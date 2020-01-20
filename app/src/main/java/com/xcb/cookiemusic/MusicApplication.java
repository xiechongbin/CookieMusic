package com.xcb.cookiemusic;

import android.content.Context;
import android.os.Process;

import com.xcb.commonlibrary.framework.LauncherAppAgent;
import com.xcb.cookiemusic.crash.CrashHandler;

/**
 * Created by xcb on 2018/5/4.
 */
public class MusicApplication extends LauncherAppAgent {
    public static Context applicationContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // if (getPackageName().equals(DeviceUtil.getProcessName(this, Process.myPid()))) {
        //     MultiDex.install(this);
        //  }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        CrashHandler.getInstance().init(this);
    }
}
