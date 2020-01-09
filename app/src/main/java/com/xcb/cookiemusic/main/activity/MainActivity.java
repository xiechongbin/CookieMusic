package com.xcb.cookiemusic.main.activity;

import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.xcb.commonlibrary.base.BaseActivity;
import com.xcb.commonlibrary.view.CustomDrawerLayout;
import com.xcb.cookiemusic.R;
import com.xcb.cookiemusic.main.fragment.CloudFragment;
import com.xcb.cookiemusic.main.fragment.FindFragment;
import com.xcb.cookiemusic.main.fragment.MusicVideoFragment;
import com.xcb.cookiemusic.main.fragment.MyMusicFragment;
import com.xcb.cookiemusic.view.MusicPlayBottomBarControlView;

import java.util.ArrayList;
import java.util.List;

/**
 * app 主界面
 * Created by xcb on 2018/4/24.
 */
public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView iv_menu;
    private ImageView iv_search;
    private ViewPager mViewPager;
    private View navigationHeader;
    private MusicPlayBottomBarControlView musicPlayBottomBarControlView;


    private CloudFragment cloudFragment;
    private MusicVideoFragment musicVideoFragment;
    private FindFragment findFragment;
    private MyMusicFragment myMusicFragment;
    private TabLayout tabLayout;
    private List<Fragment> fragmentList = new ArrayList<>();
    private String[] titles;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        iv_menu = findViewById(R.id.iv_menu);
        iv_search = findViewById(R.id.iv_search);
        mViewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabLayout);
        musicPlayBottomBarControlView = findViewById(R.id.bottomMusicController);

        iv_menu.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        tabLayout.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        navigationHeader = LayoutInflater.from(this).inflate(R.layout.layout_navigation_header, navigationView, false);
        navigationView.addHeaderView(navigationHeader);//添加头部
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);//禁止滑动
        cloudFragment = CloudFragment.newInstance("", "");
        findFragment = FindFragment.newInstance("", "");
        musicVideoFragment = MusicVideoFragment.newInstance("", "");
        myMusicFragment = MyMusicFragment.newInstance("", "");
        titles = new String[4];
        titles[0] = getResources().getString(R.string.my);
        titles[1] = getResources().getString(R.string.find);
        titles[2] = getResources().getString(R.string.cloud);
        titles[3] = getResources().getString(R.string.video);
        initTabLayout();
    }

    private void initTabLayout() {
        fragmentList.add(myMusicFragment);
        fragmentList.add(findFragment);
        fragmentList.add(cloudFragment);
        fragmentList.add(musicVideoFragment);

        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabTextColors(getResources().getColor(R.color.color_787878), getResources().getColor(R.color.color_292929));
        //选中放大效果
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView textView = new TextView(getApplication());
                float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics());

                textView.setText(tab.getText());
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                textView.setTextColor(getResources().getColor(R.color.color_292929));
                tab.setCustomView(textView);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setCustomView(null);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        FragmentAdapter adapter = new FragmentAdapter(fragmentList, getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(this);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_menu:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.iv_search:
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    private class FragmentAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragments = new ArrayList<>();

        FragmentAdapter(List<Fragment> list, FragmentManager fm) {
            super(fm);
            this.fragments.addAll(list);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

}
