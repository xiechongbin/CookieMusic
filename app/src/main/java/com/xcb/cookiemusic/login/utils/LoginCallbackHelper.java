package com.xcb.cookiemusic.login.utils;

import com.xcb.cookiemusic.login.interfaces.OnLoginEntranceCallback;

/**
 * @author xcb
 * date：2020-01-20 15:12
 * description:
 */
public class LoginCallbackHelper {
    /**
     * qq 登录
     */
    public static void onLoginWithQQCallback(Object obj) {
        if (obj instanceof OnLoginEntranceCallback) {
            OnLoginEntranceCallback callback = (OnLoginEntranceCallback) obj;
            callback.onLoginWithQQ();
        }
    }

    /**
     * wx 登录
     */
    public static void onLoginWithWeiXinCallback(Object obj) {
        if (obj instanceof OnLoginEntranceCallback) {
            OnLoginEntranceCallback callback = (OnLoginEntranceCallback) obj;
            callback.onLoginWithWeiXin();
        }
    }

    /**
     * wb 登录
     */
    public static void onLoginWithWeiBoCallback(Object obj) {
        if (obj instanceof OnLoginEntranceCallback) {
            OnLoginEntranceCallback callback = (OnLoginEntranceCallback) obj;
            callback.onLoginWithWeiBo();
        }
    }

    /**
     * wy 登录
     */
    public static void onLoginWithWangYiCallback(Object obj) {
        if (obj instanceof OnLoginEntranceCallback) {
            OnLoginEntranceCallback callback = (OnLoginEntranceCallback) obj;
            callback.onLoginWithWangYi();
        }
    }

    /**
     * 手机 登录
     */
    public static void onLoginWithPhoneCallback(Object obj) {
        if (obj instanceof OnLoginEntranceCallback) {
            OnLoginEntranceCallback callback = (OnLoginEntranceCallback) obj;
            callback.onLoginWithPhone();
        }
    }

    /**
     * 直接进入不登录s
     */
    public static void onEnterNowCallback(Object obj) {
        if (obj instanceof OnLoginEntranceCallback) {
            OnLoginEntranceCallback callback = (OnLoginEntranceCallback) obj;
            callback.onEnterNow();
        }
    }

    /**
     * 下一步
     */
    public static void onNextStep(Object obj,String tag) {
        if (obj instanceof OnLoginEntranceCallback) {
            OnLoginEntranceCallback callback = (OnLoginEntranceCallback) obj;
            callback.onNextStep(tag);
        }
    }
}
