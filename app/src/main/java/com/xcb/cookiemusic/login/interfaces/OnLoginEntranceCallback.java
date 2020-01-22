package com.xcb.cookiemusic.login.interfaces;

/**
 * @author xcb
 * date：2020-01-20 15:09
 * description:登录入口点击接口
 */
public interface OnLoginEntranceCallback {
    void onLoginWithPhone();

    void onEnterNow();

    void onLoginWithQQ();

    void onLoginWithWeiXin();

    void onLoginWithWangYi();

    void onLoginWithWeiBo();

    void onNextStep();
}
