package com.xcb.cookiemusic.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcb.cookiemusic.R;
import com.xcb.cookiemusic.adapter.FragmentAdapter;
import com.xcb.cookiemusic.fragment.LocalMusicFragment;
import com.xcb.cookiemusic.fragment.OnlineMusicFragment;

/**
 * app 主界面
 * Created by xcb on 2018/4/24.
 */
public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView iv_menu;
    private ImageView iv_search;
    private TextView tv_local_music;
    private TextView tv_online_music;
    private ViewPager mViewPager;
    private FrameLayout flPlayBar;

    private View navigationHeader;
    private LocalMusicFragment localMusicFragment;
    private OnlineMusicFragment onlineMusicFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setUpView();
    }

    private void findViews() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        iv_menu = (ImageView) findViewById(R.id.iv_menu);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        tv_local_music = (TextView) findViewById(R.id.tv_local_music);
        tv_online_music = (TextView) findViewById(R.id.tv_online_music);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        flPlayBar = (FrameLayout) findViewById(R.id.fl_play_bar);

        iv_menu.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        tv_local_music.setOnClickListener(this);
        tv_online_music.setOnClickListener(this);
        flPlayBar.setOnClickListener(this);

    }

    private void setUpView() {
        navigationHeader = LayoutInflater.from(this).inflate(R.layout.layout_navigation_header, navigationView, false);
        navigationView.addHeaderView(navigationHeader);//添加头部
        localMusicFragment = new LocalMusicFragment();
        onlineMusicFragment = new OnlineMusicFragment();
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(localMusicFragment);
        adapter.addFragment(onlineMusicFragment);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(this);
        tv_local_music.setSelected(true);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            tv_local_music.setSelected(true);
            tv_online_music.setSelected(false);
        } else {
            tv_local_music.setSelected(false);
            tv_online_music.setSelected(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_menu:
                drawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.iv_search:
                break;
            case R.id.tv_local_music:
                mViewPager.setCurrentItem(0);
                tv_local_music.setSelected(true);
                tv_online_music.setSelected(false);
                break;
            case R.id.tv_online_music:
                mViewPager.setCurrentItem(1);
                tv_local_music.setSelected(false);
                tv_online_music.setSelected(true);
                break;
            case R.id.fl_play_bar://展示播放详情页面
                break;
        }
    }
}
