package com.xcb.cookiemusic.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.xcb.cookiemusic.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xcb
 * date：2019-12-26 12:00
 * description: 底部音乐播放控件自定义view
 */
public class MusicPlayBottomBarControlView extends FrameLayout implements View.OnClickListener {
    private ViewPager viewPager;
    private PauseCircleProgressView pauseCircleProgressView;
    private ImageView iv_music_list;
    private List<BottomMusicContentView> list = new ArrayList<>();

    private InnerPagerAdapter adapter;

    public MusicPlayBottomBarControlView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MusicPlayBottomBarControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MusicPlayBottomBarControlView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_music_bottom_bar_control_view, null);
        viewPager = view.findViewById(R.id.viewpager);
        pauseCircleProgressView = view.findViewById(R.id.pauseView);
        iv_music_list = view.findViewById(R.id.iv_music_list);
        pauseCircleProgressView.setOnClickListener(this);
        iv_music_list.setOnClickListener(this);
        BottomMusicContentView view1 = new BottomMusicContentView(context);
        view1.setContent("理想", "李建");
        list.add(view1);
        BottomMusicContentView view2 = new BottomMusicContentView(context);
        view2.setContent("男孩", "梁博");
        list.add(view2);
        BottomMusicContentView view3 = new BottomMusicContentView(context);
        view3.setContent("差不多", "邓紫棋");
        list.add(view3);
        adapter = new InnerPagerAdapter();
        viewPager.setAdapter(adapter);
        this.addView(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_music_list:
                break;
            case R.id.pauseView:
                break;
        }
    }

    private class InnerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(list.get(position));
            Log.d("aaddx", "instantiateItem position = " + position);
            return list.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            Log.d("aaddx", "destroyItem position = " + position);
            container.removeView(list.get(position));
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }
    }
}
