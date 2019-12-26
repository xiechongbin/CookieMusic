package com.xcb.cookiemusic.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.xcb.cookiemusic.R;

/**
 * @author xcb
 * date：2019-12-26 12:00
 * description: 底部音乐播放控件自定义view
 */
public class MusicPlayBottomBarControlView extends FrameLayout implements View.OnClickListener {
    private ViewPager viewPager;
    private ImageView iv_pause;
    private ImageView iv_menu;

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

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_music_bottom_bar_control_view, null);
        viewPager = view.findViewById(R.id.viewpager);
        iv_pause = view.findViewById(R.id.iv_pause);
        iv_menu = view.findViewById(R.id.iv_menu);
        iv_pause.setOnClickListener(this);
        iv_menu.setOnClickListener(this);
        this.addView(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_menu:
                break;
            case R.id.iv_pause:
                break;
        }
    }
}
