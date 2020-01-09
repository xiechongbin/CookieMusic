package com.xcb.cookiemusic.main.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xcb.commonlibrary.base.BaseFragment;
import com.xcb.cookiemusic.R;
import com.xcb.cookiemusic.view.PauseCircleProgressView;

import java.util.Timer;
import java.util.TimerTask;


public class MyMusicFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private TextView tv_start;
    private TextView tv_pause;
    private PauseCircleProgressView progressView;
    private boolean isStart;
    private boolean hasStart;
    private Timer timer;
    private int progress = 0;

    public MyMusicFragment() {

    }

    public static MyMusicFragment newInstance(String param1, String param2) {
        MyMusicFragment fragment = new MyMusicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_my_music;
    }

    @Override
    protected void initViews(@NonNull View root, @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        progressView = root.findViewById(R.id.progressView);
        tv_start = root.findViewById(R.id.tv_start);
        tv_pause = root.findViewById(R.id.tv_pause);
        timer = new Timer();
        final int length = 24;

        progressView.setMaxProgress(length);
        tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressView.start();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (progress < length) {
                            progress++;
                            progressView.setProgress(progress);
                        } else {
                            timer.cancel();
                        }
                    }
                }, 0, 1000);
            }
        });
        tv_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressView.pause();
                timer.cancel();
            }
        });
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
}
