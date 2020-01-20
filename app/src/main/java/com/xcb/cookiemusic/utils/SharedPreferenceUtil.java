package com.xcb.cookiemusic.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.xcb.commonlibrary.framework.LauncherAppAgent;

import java.util.Map;


/**
 * SharedPreferences 操作帮助类
 * 请不要在这个里面存储过大的内容，过大的内容会在全部缓存到map中导致内存溢出的.
 * 注意: 添加数据时一定要调用{@link #apply()}方法,不然无法将数据存储进去(防止多次提交的性能损耗),如果需要即时获取请采用{@link #commit()}同步提交
 *
 * @author WJ
 * @date 23/5/19
 */

public final class SharedPreferenceUtil {

    private SharedPreferences mSp;
    private SharedPreferences.Editor mEditor;
    private static final String SP_NAME = "XGO";

    public static SharedPreferenceUtil getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * SPUtils构造函数
     * <p>在Application中初始化</p>
     */
    private SharedPreferenceUtil() {
        Context context = LauncherAppAgent.getInstance().getApplicationContext();
        mSp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        mEditor = mSp.edit();
        mEditor.apply();
    }

    /**
     * 异步提交数据
     */
    public final void apply() {
        mEditor.apply();
    }

    /**
     * 同步提交数据
     */
    public final void commit() {
        mEditor.commit();
    }

    /**
     * SP中写入String类型value
     *
     * @param key   键
     * @param value 值
     */
    public final SharedPreferenceUtil putString(@NonNull String key, String value) {
        mEditor.putString(key, value);
        return this;
    }

    /**
     * SP中读取String
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值{@code null}
     */
    public final String getString(@NonNull String key) {
        return getString(key, null);
    }

    /**
     * SP中读取String
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public final String getString(@NonNull String key, String defaultValue) {
        return mSp.getString(key, defaultValue);
    }

    /**
     * SP中写入int类型value
     *
     * @param key   键
     * @param value 值
     */
    public final SharedPreferenceUtil putInt(@NonNull String key, int value) {
        mEditor.putInt(key, value);
        return this;
    }

    /**
     * SP中读取int
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public final int getInt(@NonNull String key) {
        return getInt(key, 0);
    }

    /**
     * SP中读取int
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public final int getInt(@NonNull String key, int defaultValue) {
        return mSp.getInt(key, defaultValue);
    }

    /**
     * SP中写入long类型value
     *
     * @param key   键
     * @param value 值
     */
    public final SharedPreferenceUtil putLong(@NonNull String key, long value) {
        mEditor.putLong(key, value);
        return this;
    }

    /**
     * SP中读取long
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public final long getLong(@NonNull String key) {
        return getLong(key, -1L);
    }

    /**
     * SP中读取long
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public final long getLong(@NonNull String key, long defaultValue) {
        return mSp.getLong(key, defaultValue);
    }

    /**
     * SP中写入float类型value
     *
     * @param key   键
     * @param value 值
     */
    public final SharedPreferenceUtil putFloat(@NonNull String key, float value) {
        mEditor.putFloat(key, value);
        return this;
    }

    /**
     * SP中读取float
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public final float getFloat(@NonNull String key) {
        return getFloat(key, -1f);
    }

    /**
     * SP中读取float
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public final float getFloat(@NonNull String key, float defaultValue) {
        return mSp.getFloat(key, defaultValue);
    }


    /**
     * SP中写入boolean类型value
     *
     * @param key   键
     * @param value 值
     */
    public final SharedPreferenceUtil putBoolean(@NonNull String key, boolean value) {
        mEditor.putBoolean(key, value);
        return this;
    }

    /**
     * SP中读取boolean
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值{@code false}
     */
    public final boolean getBoolean(@NonNull String key) {
        return getBoolean(key, false);
    }

    /**
     * SP中读取boolean
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public final boolean getBoolean(@NonNull String key, boolean defaultValue) {
        return mSp.getBoolean(key, defaultValue);
    }

    /**
     * SP中获取所有键值对
     *
     * @return Map对象
     */
    public final Map<String, ?> getAll() {
        return mSp.getAll();
    }

    /**
     * SP中移除该key
     *
     * @param key 键
     */
    public final void remove(@NonNull String key) {
        mEditor.remove(key).apply();
    }

    /**
     * SP中是否存在该key
     *
     * @param key 键
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public final boolean contains(@NonNull String key) {
        return mSp.contains(key);
    }

    /**
     * SP中清除所有数据
     */
    public final void clear() {
        mEditor.clear().apply();
    }


    private final static class InstanceHolder {
        private static final SharedPreferenceUtil INSTANCE = new SharedPreferenceUtil();
    }
}