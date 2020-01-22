package com.xcb.cookiemusic.login;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.xcb.commonlibrary.base.BaseActivity;
import com.xcb.cookiemusic.R;
import com.xcb.cookiemusic.login.fragment.LoginEntranceFragment;
import com.xcb.cookiemusic.login.fragment.PhoneLoginFragment;
import com.xcb.cookiemusic.login.fragment.PhoneVerifyFragment;
import com.xcb.cookiemusic.login.interfaces.OnLoginEntranceCallback;


/**
 * @author xcb
 * date：2020-01-19 14:02Ø
 * description:登陆注册页面
 */
public class LoginAndRegisterActivity extends BaseActivity implements OnLoginEntranceCallback {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginAndRegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_login_register;
    }

    @Override
    protected void initViews() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    @Override
    protected void initData() {
        enterLoginEntranceFragment();
    }

    /**
     * 登录入口
     */
    private void enterLoginEntranceFragment() {
        LoginEntranceFragment fragment = LoginEntranceFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.fl_container, fragment, LoginEntranceFragment.class.getSimpleName());
        transaction.commitAllowingStateLoss();
        transaction.addToBackStack(LoginEntranceFragment.class.getSimpleName());
    }

    /**
     * 手机登录
     */
    @Override
    public void onLoginWithPhone() {
        PhoneLoginFragment fragment = PhoneLoginFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.fl_container, fragment, PhoneLoginFragment.class.getSimpleName());
        transaction.commitAllowingStateLoss();
        transaction.addToBackStack(PhoneLoginFragment.class.getSimpleName());
    }

    /**
     * 立即体验
     */
    @Override
    public void onEnterNow() {

    }

    /**
     * QQ登录
     */
    @Override
    public void onLoginWithQQ() {

    }

    /**
     * 微博登录
     */
    @Override
    public void onLoginWithWeiXin() {

    }

    /**
     * 微信登录
     */
    @Override
    public void onLoginWithWangYi() {

    }

    /**
     * 网易账号登录
     */
    @Override
    public void onLoginWithWeiBo() {

    }

    /**
     * 下一步验证手机号
     */
    @Override
    public void onNextStep() {
        PhoneVerifyFragment fragment = PhoneVerifyFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.fl_container, fragment, PhoneVerifyFragment.class.getSimpleName());
        transaction.commitAllowingStateLoss();
        transaction.addToBackStack(PhoneVerifyFragment.class.getSimpleName());
    }
}
