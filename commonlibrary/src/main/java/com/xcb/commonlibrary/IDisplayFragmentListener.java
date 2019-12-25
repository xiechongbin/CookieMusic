package com.xcb.commonlibrary;

import androidx.fragment.app.Fragment;

/**
 * @author yinyangwu
 * @date 2019-12-04 18:58
 * description:展示fragment监听器
 */
public interface IDisplayFragmentListener {

    void setCurrentShowFragment(Class<? extends Fragment> clazz);

    Class<? extends Fragment> getCurrentShowFragment();
}
