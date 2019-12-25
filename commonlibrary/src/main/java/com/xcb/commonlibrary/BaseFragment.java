package com.xcb.commonlibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.xcb.commonlibrary.ui.fragment.LoadingFragment;
import com.xcb.commonlibrary.ui.toast.IconToast;


import me.yokeyword.fragmentation.ExtraTransaction;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportFragmentDelegate;
import me.yokeyword.fragmentation.SupportHelper;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * fragment 基类
 */
public abstract class BaseFragment extends Fragment implements BaseView, ISupportFragment {

    final SupportFragmentDelegate mDelegate = new SupportFragmentDelegate(this);
    protected BaseActivity _mActivity;

    /**
     * 显示的布局View
     */
    private View mView;

    @Override
    public SupportFragmentDelegate getSupportDelegate() {
        return mDelegate;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mDelegate.onAttach(activity);
        _mActivity = (BaseActivity) mDelegate.getActivity();
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDelegate.onCreate(savedInstanceState);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return mDelegate.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDelegate.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mDelegate.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
            initViews(mView, inflater, container, savedInstanceState);
            return mView;
        }
        mView = inflater.inflate(getLayoutID(), container, false);
        initViews(mView, inflater, container, savedInstanceState);
        initData();
        return mView;
    }

    protected abstract void initViews(@NonNull View root, @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    /**
     * 获取布局LayoutId
     *
     * @return layoutId
     */
    @LayoutRes
    protected abstract int getLayoutID();


    /**
     * 初始化数据
     */
    protected abstract void initData();

    @Override
    public void onResume() {
        super.onResume();
        mDelegate.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mDelegate.onPause();
    }

    @Override
    public void onDestroyView() {
        mDelegate.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        hideLoadDialog();
        mDelegate.onDestroy();
        super.onDestroy();
    }

    @Override
    public void enqueueAction(Runnable runnable) {
        mDelegate.enqueueAction(runnable);
    }

    @Override
    public void post(Runnable runnable) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(runnable);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mDelegate.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mDelegate.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * Called when the enter-animation end.
     * 入栈动画 结束时,回调
     */
    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        mDelegate.onEnterAnimationEnd(savedInstanceState);
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        this.mDelegate.onLazyInitView(savedInstanceState);
    }

    @Override
    public void onSupportVisible() {
        this.mDelegate.onSupportVisible();
    }

    @Override
    public void onSupportInvisible() {
        this.mDelegate.onSupportInvisible();
    }

    @Override
    public final boolean isSupportVisible() {
        return this.mDelegate.isSupportVisible();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return mDelegate.onCreateFragmentAnimator();
    }

    @Override
    public FragmentAnimator getFragmentAnimator() {
        return mDelegate.getFragmentAnimator();
    }

    @Override
    public void showToast(@StringRes int resId) {
        IconToast.makeText(getContext(), resId);
    }


    @Override
    public void showToast(CharSequence msg) {
        IconToast.makeText(getContext(), msg.toString());
    }

    @Override
    public void showLoadDialog() {
        if (getActivity() == null || getActivity().isDestroyed() || getActivity().isFinishing() || isDetached() || isRemoving()) {
            return;
        }
        LoadingFragment.show(getChildFragmentManager());
    }

    @Override
    public void showLoadDialog(String str) {

    }

    @Override
    public void hideLoadDialog() {
        LoadingFragment.dismiss(getChildFragmentManager());
    }

    @Override
    public Activity getParentContext() {
        return getActivity();
    }

    /**
     * 类似 {@link Activity#setResult(int, Intent)}
     * <p>
     * Similar to {@link Activity#setResult(int, Intent)}
     *
     * @see (ISupportFragment, int)
     */
    @Override
    public void setFragmentResult(int resultCode, Bundle bundle) {
        mDelegate.setFragmentResult(resultCode, bundle);
    }

    /**
     * 类似  {@link Activity#onActivityResult(int, int, Intent)}
     * <p>
     * Similar to {@link Activity#onActivityResult(int, int, Intent)}
     *
     * @see #startForResult(ISupportFragment, int)
     */
    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        mDelegate.onFragmentResult(requestCode, resultCode, data);
    }

    /**
     * 设置Fragment内的全局动画
     */
    @Override
    public void setFragmentAnimator(FragmentAnimator fragmentAnimator) {
        mDelegate.setFragmentAnimator(fragmentAnimator);
    }

    /**
     * 在start(TargetFragment,LaunchMode)时,启动模式为SingleTask/SingleTop, 回调TargetFragment的该方法
     * 类似 {@link Activity#onNewIntent(Intent)}
     * <p>
     * Similar to {@link Activity#onNewIntent(Intent)}
     *
     * @param args putNewBundle(Bundle newBundle)
     * @see #start(ISupportFragment, int)
     */
    @Override
    public void onNewBundle(Bundle args) {
        mDelegate.onNewBundle(args);
    }

    /**
     * 添加NewBundle,用于启动模式为SingleTask/SingleTop时
     *
     * @see #start(ISupportFragment, int)
     */
    @Override
    public void putNewBundle(Bundle newBundle) {
        mDelegate.putNewBundle(newBundle);
    }


    /**
     * 按返回键触发,前提是SupportActivity的onBackPressed()方法能被调用
     *
     * @return false则继续向上传递, true则消费掉该事件
     */
    @Override
    public boolean onBackPressedSupport() {
        return mDelegate.onBackPressedSupport();
    }

    /**
     * 获取栈内的fragment对象
     */
    public <T extends ISupportFragment> T findFragment(Class<T> fragmentClass) {
        return SupportHelper.findFragment(getFragmentManager(), fragmentClass);
    }

    /**
     * 获取栈内的fragment对象
     */
    public <T extends ISupportFragment> T findChildFragment(Class<T> fragmentClass) {
        return SupportHelper.findFragment(getChildFragmentManager(), fragmentClass);
    }

}
