package com.xcb.commonlibrary.base;

import android.app.Activity;

/**
 * @author jinpingchen
 * @date 2019-10-23 20:28
 * description:
 */
public interface BaseView {

    void showToast(int resId);

    void showToast(CharSequence msg);

    void showLoadDialog();

    void showLoadDialog(String str);

    void hideLoadDialog();

    Activity getParentContext();

}
