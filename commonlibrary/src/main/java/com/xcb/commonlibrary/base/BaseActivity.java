package com.xcb.commonlibrary.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xcb.commonlibrary.framework.app.ui.BaseFragmentActivity;
import com.xcb.commonlibrary.ui.fragment.LoadingFragment;
import com.xcb.commonlibrary.ui.toast.IconToast;

import me.yokeyword.fragmentation.ExtraTransaction;
import me.yokeyword.fragmentation.ISupportActivity;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportActivityDelegate;
import me.yokeyword.fragmentation.SupportHelper;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public abstract class BaseActivity extends BaseFragmentActivity implements ISupportActivity, BaseView, IDisplayFragmentListener {

    final SupportActivityDelegate mDelegate = new SupportActivityDelegate(this);

    @Override
    public SupportActivityDelegate getSupportDelegate() {
        return mDelegate;
    }

    /**
     * Perform some extra transactions.
     * 额外的事务：自定义Tag，添加SharedElement动画，操作非回退栈Fragment
     */
    @Override
    public ExtraTransaction extraTransaction() {
        return mDelegate.extraTransaction();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDelegate.onCreate(savedInstanceState);
        if (getLayoutID() != 0) {
            setContentView(getLayoutID());
        }
        initViews();
        initData();
    }

    /**
     * 获取layout id
     *
     * @return 布局的LayoutId
     */
    protected abstract int getLayoutID();

    /**
     * 初始化View
     */
    protected abstract void initViews();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    @Override
    public void showToast(int resId) {
        IconToast.makeText(this, resId);
    }


    @Override
    public void showToast(CharSequence msg) {
        if (!TextUtils.isEmpty(msg)) {
            IconToast.makeText(this, msg.toString());
        }
    }

    @Override
    public void showLoadDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (isDestroyed()) {
                return;
            }
        }
        /*当当前界面结束时,不再做显示和创建对象的操作*/
        if (isFinishing()) {
            return;
        }
        LoadingFragment.show(getSupportFragmentManager());
    }

    @Override
    public void showLoadDialog(String str) {
    }

    @Override
    public void hideLoadDialog() {
        if (isFinishing()) {
            return;
        }
        LoadingFragment.dismiss(getSupportFragmentManager());
    }

    @Override
    public void showProgressDialog(String s) {

    }

    @Override
    public void showProgressDialog(String s, boolean b, DialogInterface.OnCancelListener onCancelListener) {

    }

    @Override
    public void dismissProgressDialog() {

    }

    @Override
    public Activity getParentContext() {
        return this;
    }

    @Override
    public void setCurrentShowFragment(Class<? extends Fragment> clazz) {
    }

    @Override
    public Class<? extends Fragment> getCurrentShowFragment() {
        return null;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDelegate.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        mDelegate.onDestroy();
        super.onDestroy();
    }

    /**
     * Note： return mDelegate.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mDelegate.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    /**
     * 不建议复写该方法,请使用 {@link #onBackPressedSupport} 代替
     */
    @Override
    final public void onBackPressed() {
        mDelegate.onBackPressed();
    }

    /**
     * 该方法回调时机为,Activity回退栈内Fragment的数量 小于等于1 时,默认finish Activity
     * 请尽量复写该方法,避免复写onBackPress(),以保证SupportFragment内的onBackPressedSupport()回退事件正常执行
     */
    @Override
    public void onBackPressedSupport() {
        mDelegate.onBackPressedSupport();
    }

    /**
     * 获取设置的全局动画 copy
     *
     * @return FragmentAnimator
     */
    @Override
    public FragmentAnimator getFragmentAnimator() {
        return mDelegate.getFragmentAnimator();
    }

    /**
     * Set all fragments animation.
     * 设置Fragment内的全局动画
     */
    @Override
    public void setFragmentAnimator(FragmentAnimator fragmentAnimator) {
        mDelegate.setFragmentAnimator(fragmentAnimator);
    }

    /**
     * Set all fragments animation.
     * 构建Fragment转场动画
     * <p/>
     * 如果是在Activity内实现,则构建的是Activity内所有Fragment的转场动画,
     * 如果是在Fragment内实现,则构建的是该Fragment的转场动画,此时优先级 > Activity的onCreateFragmentAnimator()
     *
     * @return FragmentAnimator对象
     */
    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return mDelegate.onCreateFragmentAnimator();
    }

    @Override
    public void post(Runnable runnable) {
        mDelegate.post(runnable);
    }

    /**
     * 获取栈内的fragment对象
     */
    public <T extends ISupportFragment> T findFragment(Class<T> fragmentClass) {
        return SupportHelper.findFragment(getSupportFragmentManager(), fragmentClass);
    }

}
