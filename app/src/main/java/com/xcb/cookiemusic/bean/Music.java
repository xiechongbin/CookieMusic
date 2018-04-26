package com.xcb.cookiemusic.bean;

import java.io.Serializable;

/**
 * 音乐实体类
 * Created by xcb on 2018/4/25.
 */

public class Music implements Serializable {
    private Long id;
    private int type;
    private long songId;//歌曲id(本地)
    private String title;//音乐标题
    private String artist;//艺术家
    private String album;//z专辑
    private long albumId;//专辑id(本地)
    private String coverPath;//专辑封面路径
    private long duration; // 持续时间
    private String path; // 播放地址
    private String fileName; // [本地]文件名
    private long fileSize; // [本地]文件大小

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public interface Type {
        int LOCAL = 0;
        int ONLINE = 1;
    }
}
