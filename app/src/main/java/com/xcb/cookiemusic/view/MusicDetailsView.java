package com.xcb.cookiemusic.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xcb.cookiemusic.R;

/**
 * 底部弹出的歌曲详情界面
 * Created by xcb on 2018/4/28.
 */

public class MusicDetailsView extends View {
    private Context context;

    public MusicDetailsView(Context context) {
        super(context);
    }

    public MusicDetailsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MusicDetailsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public static class Builder {
        private View detailsView;
        private TextView tv_musicName;
        private TextView tv_fileName;
        private TextView tv_musicSize;
        private TextView tv_musicQuality;
        private TextView tv_musicPath;
        private TextView tv_duration;

        public Builder(Context context) {
            initView(context);
        }

        private void initView(Context context) {
            detailsView =  LayoutInflater.from(context).inflate(R.layout.layout_local_music_details, null);
            tv_musicName = (TextView) detailsView.findViewById(R.id.tv_music_details_name);
            tv_fileName = (TextView) detailsView.findViewById(R.id.tv_music_details_file_name);
            tv_musicSize = (TextView) detailsView.findViewById(R.id.tv_music_details_music_size);
            tv_musicQuality = (TextView) detailsView.findViewById(R.id.tv_music_details_music_quality);
            tv_musicPath = (TextView) detailsView.findViewById(R.id.tv_music_details_music_path);
            tv_duration = (TextView) detailsView.findViewById(R.id.tv_music_details_duration);
        }

        public Builder setMusicName(String musicName) {
            tv_musicName.setText(musicName);
            return this;
        }

        public Builder setFileName(String fileName) {
            tv_fileName.setText(fileName);
            return this;
        }

        public Builder setFileSize(String fileSize) {
            tv_musicSize.setText(fileSize);
            return this;
        }

        public Builder setMusicQuality(String musicQuality) {
            tv_musicQuality.setText(musicQuality);
            return this;
        }

        public Builder setMusicPath(String filePath) {
            tv_musicPath.setText(filePath);
            return this;
        }
        public Builder setMusicDuration(String duration) {
            tv_duration.setText(duration);
            return this;
        }

        public View create() {
            return detailsView;
        }
    }
}
