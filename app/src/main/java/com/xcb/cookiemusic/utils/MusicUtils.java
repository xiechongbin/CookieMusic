package com.xcb.cookiemusic.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import com.xcb.cookiemusic.bean.Music;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 音乐工具类
 * Created by xcb on 2018/4/26.
 */

public class MusicUtils {
    public static final String SELECTION = MediaStore.Audio.AudioColumns.SIZE + " >= ? AND " + MediaStore.Audio.AudioColumns.DURATION + " >= ?";

    /**
     * 获取本地音乐列表
     */
    public static List<Music> getLocalMusics(Context context) {
        List<Music> musicList = new ArrayList<>();
        long filterSize = 1 * 1024 * 1024;
        long filterTime = 1 * 1000;
        int i = 0;
        Cursor cursor = context.getContentResolver()
                .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        new String[]{
                                BaseColumns._ID,
                                MediaStore.Audio.AudioColumns.IS_MUSIC,
                                MediaStore.Audio.AudioColumns.TITLE,
                                MediaStore.Audio.AudioColumns.ARTIST,
                                MediaStore.Audio.AudioColumns.ALBUM,
                                MediaStore.Audio.AudioColumns.ALBUM_ID,
                                MediaStore.Audio.AudioColumns.DATA,
                                MediaStore.Audio.AudioColumns.DISPLAY_NAME,
                                MediaStore.Audio.AudioColumns.SIZE,
                                MediaStore.Audio.AudioColumns.DURATION
                        },
                        SELECTION,
                        new String[]{
                                String.valueOf(filterSize),
                                String.valueOf(filterTime),
                        },
                        MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor == null) {
            return musicList;
        }
        while (cursor.moveToNext()) {
            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.IS_MUSIC));
            // 是否为音乐，魅族手机上始终为0
            if (!SystemUtils.isFlyme() && isMusic == 0) {
                continue;
            }
            long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
            String title = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE)));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST));
            String album = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM)));
            long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
            String fileName = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DISPLAY_NAME)));
            long fileSize = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));

            Music music = new Music();
            music.setSongId(id);
            music.setType(Music.Type.LOCAL);
            music.setTitle(title);
            music.setArtist(artist);
            music.setAlbum(album);
            music.setAlbumId(albumId);
            music.setDuration(duration);
            music.setPath(path);
            music.setFileName(fileName);
            music.setFileSize(fileSize);
            if (++i <= 20) {
                // 只加载前20首的缩略图
                //  CoverLoader.get().loadThumb(music);
            }
            musicList.add(music);
        }
        cursor.close();
        return musicList;
    }

    /**
     * 字节转化成mb 保留2位小数
     *
     * @param size 文件大小
     * @return mb
     */
    public static String convertMB(float size) {
        DecimalFormat format = new DecimalFormat("0.0");
        return format.format(size / 1024 / 1024) + "Mb";
    }

    /**
     * @param miles 时长 毫秒
     * @return 格式化后的时长
     */
    public static String convertTimeMilesToString(long miles) {
        if (miles < 60000) {
            return "00:" + miles / 1000;
        } else {
            long sec = miles / 1000;
            long min = sec / 60;
            long sec1 = sec % 60;
            if (sec1 > 0 && sec1 < 10) {
                return min + ":0" + sec;
            } else {
                return min + ":" + sec1;
            }

        }
    }
}
