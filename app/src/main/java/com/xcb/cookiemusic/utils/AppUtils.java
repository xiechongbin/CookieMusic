package com.xcb.cookiemusic.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * app 相关工具
 * Created by xcb on 2018/5/4.
 */
public class AppUtils {
    /**
     * 获取设备的IMEI，用于单卡槽设备
     *
     * @param context 上下文对象
     * @return 设备的IMEI
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        if (deviceId == null) {
            return "";
        }
        return deviceId;
    }

    /**
     * 获取设备指定卡槽的IMEI，用于双卡双待设备
     *
     * @param context 上下文对象
     * @param slotId  卡槽号
     * @return 设备的IMEI
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static String getDeviceId(Context context, int slotId) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId(slotId);
        if (deviceId == null) {
            return "";
        }
        return deviceId;
    }

    /**
     * 获取sim卡的IMSI
     *
     * @param context 上下文对象
     * @return sim卡的IMSI
     */
    public static String getSubscriberId(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSubscriberId();
    }

    /**
     * 返回sim卡是否可用
     *
     * @return sim卡可用返回为true，不可用返回为false
     */
    public static boolean isSimAvailable(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimState() == TelephonyManager.SIM_STATE_READY;
    }


    /**
     * 获取Android系统ID，设备第一次运行Android系统会随机生成64位数字，当设备刷机或恢复出厂设置后会发生变化，
     * 部分定制厂商返回的都是相同的，CDMA设备返回空值
     *
     * @param context 上下文对象
     * @return Android系统ID
     */
    public static String getAndroidID(Context context) {
        return Settings.Secure
                .getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getAppCacheDir(Context context) {
        String cacheDir;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                ||!Environment.isExternalStorageRemovable()){
            cacheDir = context.getExternalCacheDir().getAbsolutePath();
        }else{
            cacheDir = context.getCacheDir().getAbsolutePath();
        }
        return cacheDir;
    }

}
