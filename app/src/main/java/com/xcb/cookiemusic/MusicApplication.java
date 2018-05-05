package com.xcb.cookiemusic;

import android.app.Application;
import android.content.Context;

import com.xcb.cookiemusic.crash.CrashHandler;

/**
 *
 * Created by xcb on 2018/5/4.
 */
public class MusicApplication extends Application{
    public static Context applicationContext;
    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        CrashHandler.getInstance().init(this);
    }
}
