package com.xcb.commonlibrary.utils;

import com.xcb.commonlibrary.framework.LauncherAppAgent;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;

/**
 * @author xcb
 * date：2020-01-22 17:45
 * description:
 */
public class PhoneUtils {
    /**
     * 判断手机号是否合法
     *
     * @param number 手机号码
     * @return 是否合法
     */
    public static boolean isValidNumber(CharSequence number, String region) {
        PhoneNumberUtil util = PhoneNumberUtil.createInstance(LauncherAppAgent.getInstance().getApplicationContext());
        if (util == null) {
            return false;
        }
        try {
            final Phonenumber.PhoneNumber phoneNumber = util.parse(number, region);
            return util.isValidNumber(phoneNumber);
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
