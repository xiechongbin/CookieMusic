package com.xcb.cookiemusic.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.xcb.cookiemusic.R;

/**
 * 底部音乐内容
 * Created by xcb on 2018/4/28.
 */

public class BottomMusicContentView extends LinearLayout {
    private Context context;
    private ImageView iv_play_bar_coverl;
    private TextView tv_play_bar_title;
    private TextView tv_play_bar_artist;


    public BottomMusicContentView(Context context) {
        super(context);
        init(context);
    }

    public BottomMusicContentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BottomMusicContentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_music_bottom_bar_content, null);
        iv_play_bar_coverl = view.findViewById(R.id.iv_play_bar_cover);
        tv_play_bar_title = view.findViewById(R.id.tv_play_bar_title);
        tv_play_bar_artist = view.findViewById(R.id.tv_play_bar_artist);
        this.addView(view);
    }

    public void setContent(String musicTitle, String artist) {
        tv_play_bar_title.setText(musicTitle);
        tv_play_bar_artist.setText(artist);
    }
}
