package com.xcb.commonlibrary.framework;

import android.content.SharedPreferences;

/**
 * @author jinpingchen
 */
public interface MicroContent {
    /**
     * 保存状态
     *
     * @param editor
     */
    void saveState(SharedPreferences.Editor editor);

    /**
     * 恢复状态
     *
     * @param preferences
     */
    void restoreState(SharedPreferences preferences);
}
