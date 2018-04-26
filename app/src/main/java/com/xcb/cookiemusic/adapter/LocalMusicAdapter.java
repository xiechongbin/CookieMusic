package com.xcb.cookiemusic.adapter;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcb.cookiemusic.R;
import com.xcb.cookiemusic.bean.Music;
import com.xcb.cookiemusic.interfaces.OnMoreClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地音乐列表适配器
 * Created by xcb on 2018/4/25.
 */

public class LocalMusicAdapter extends BaseAdapter {
    private List<Music> musicList = new ArrayList<>();
    private OnMoreClickListener onMoreClickListener;

    @Override
    public int getCount() {
        return musicList.size();
    }

    @Override
    public Object getItem(int position) {
        return musicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_local_music_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.iv_cover.setImageBitmap(BitmapFactory.decodeFile(musicList.get(position).getCoverPath()));
        viewHolder.tv_title.setText(musicList.get(position).getTitle());
        viewHolder.tv_artist.setText(musicList.get(position).getArtist());

        viewHolder.iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onMoreClickListener != null) {
                    onMoreClickListener.onMoreClick(position);
                }
            }
        });
        return convertView;
    }

    public void setMusicList(List<Music> musicList) {
        this.musicList = musicList;
    }

    public List<Music> getMusicList() {
        return musicList;
    }

    public void setOnMoreClickListener(OnMoreClickListener listener) {
        onMoreClickListener = listener;
    }

    public void remove(Music music) {
        musicList.remove(music);
    }

    private static class ViewHolder {
        private View vPlaying;
        private ImageView iv_cover;
        private TextView tv_title;
        private TextView tv_artist;
        private View vDivider;
        private ImageView iv_more;

        private ViewHolder(View v) {
            vPlaying = v.findViewById(R.id.v_playing);
            vDivider = v.findViewById(R.id.v_divider);
            iv_cover = (ImageView) v.findViewById(R.id.iv_cover);
            iv_more = (ImageView) v.findViewById(R.id.iv_more);
            tv_title = (TextView) v.findViewById(R.id.tv_title);
            tv_artist = (TextView) v.findViewById(R.id.tv_artist);

        }
    }
}
