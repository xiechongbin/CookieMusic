package com.xcb.cookiemusic.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.xcb.cookiemusic.R;
import com.xcb.cookiemusic.adapter.LocalMusicAdapter;
import com.xcb.cookiemusic.bean.Music;
import com.xcb.cookiemusic.interfaces.OnMoreClickListener;
import com.xcb.cookiemusic.log.Logger;
import com.xcb.cookiemusic.utils.MusicUtils;
import com.xcb.cookiemusic.utils.PermissionReq;
import com.xcb.cookiemusic.utils.ToastUtils;
import com.xcb.cookiemusic.view.MusicDetailsView;

import java.io.File;
import java.util.List;

public class LocalMusicFragment extends BaseFragment implements AdapterView.OnItemClickListener, OnMoreClickListener {
    private ListView localMusicListView;
    private TextView tv_searching_tips;
    private View view;
    private LocalMusicAdapter adapter;
    private Music choosedMusic;

    public LocalMusicFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_local_music, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        localMusicListView = (ListView) view.findViewById(R.id.lv_local_music);
        tv_searching_tips = (TextView) view.findViewById(R.id.tv_searching);
        localMusicListView.setOnItemClickListener(this);
        adapter = new LocalMusicAdapter();
        adapter.setOnMoreClickListener(this);
        localMusicListView.setAdapter(adapter);
        scanMusic();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionReq.RQUEST_CODE_WRITE_SETTINGS) {
            Logger.d("更改系统设置权限申请成功");
        }
    }

    /**
     * 扫描本地音乐
     */
    private void scanMusic() {
        PermissionReq.with(getActivity())
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .result(new PermissionReq.Result() {
                    @Override
                    public void onGranted() {
                        new AsyncTask<Void, Void, List<Music>>() {

                            @Override
                            protected List<Music> doInBackground(Void... params) {
                                return MusicUtils.getLocalMusics(getActivity());
                            }

                            @Override
                            protected void onPostExecute(List<Music> musics) {
                                tv_searching_tips.setVisibility(View.GONE);
                                localMusicListView.setVisibility(View.VISIBLE);
                                adapter.setMusicList(musics);
                                adapter.notifyDataSetChanged();
                            }
                        }.execute();
                    }

                    @Override
                    public void onDenied() {

                    }
                }).request();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //// TODO: 2018/4/26
    }

    @Override
    public void onMoreClick(final int position) {
        showMoreDialog(adapter.getMusicList().get(position));
    }

    /**
     * 展示更多页面对话框
     */
    private void showMoreDialog(final Music music) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(music.getTitle())
                .setItems(R.array.local_music_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                share(music);
                                break;
                            case 1:
                                choosedMusic = music;
                                setMusicAsRings(music);
                                break;
                            case 2:
                                showMusicDetails(music);
                                break;
                            case 3:
                                deleteMusic(music);
                                break;
                        }
                    }
                })
                .create();
        dialog.show();
    }

    /**
     * 设置音乐为手机铃声
     */
    private void setMusicAsRings(final Music music) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(getActivity())) {
                Uri uri = MediaStore.Audio.Media.getContentUriForPath(music.getPath());
                //查询音乐文件在媒体库中是否存在
                Cursor cursor = getActivity().getContentResolver().query(uri, null, MediaStore.MediaColumns.DATA + "=?", new String[]{music.getPath()}, null);
                if (cursor == null) {
                    ToastUtils.toast(getActivity(), getActivity().getString(R.string.music_not_exist, music.getTitle()));
                    return;
                }
                if (cursor.moveToFirst() && cursor.getCount() > 0) {
                    String id = cursor.getString(0);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.Audio.Media.IS_MUSIC, true);
                    contentValues.put(MediaStore.Audio.Media.IS_RINGTONE, true);
                    contentValues.put(MediaStore.Audio.Media.IS_ALARM, true);
                    contentValues.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
                    contentValues.put(MediaStore.Audio.Media.IS_PODCAST, true);
                    getActivity().getContentResolver().update(uri, contentValues, MediaStore.MediaColumns.DATA + "=?", new String[]{music.getPath()});
                    Uri newUri = ContentUris.withAppendedId(uri, Long.valueOf(id));
                    RingtoneManager.setActualDefaultRingtoneUri(getActivity(), RingtoneManager.TYPE_RINGTONE, newUri);
                    ToastUtils.toast(getActivity(), getActivity().getString(R.string.set_ringstone_success));
                }
            } else {
                PermissionReq.getWriteSettingsPermission(getActivity());
            }
        }
    }


    /**
     * 删除音乐
     */
    private void deleteMusic(final Music music) {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(music.getTitle())
                .setMessage(getString(R.string.delete_music, music.getTitle()))
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File file = new File(music.getPath());
                        if (file.exists()) {
                            if (file.delete()) {
                                //删除成功
                                adapter.remove(music);
                                adapter.notifyDataSetChanged();
                                //刷新媒体库
                                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://".concat(music.getPath())));
                                getContext().sendBroadcast(intent);
                            }
                        } else {
                            ToastUtils.toast(getActivity(), R.string.music_delete_failed);
                        }
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    /**
     * 展示音乐详情
     */
    private void showMusicDetails(Music music) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogStyle);
        View musicDetailsView = new MusicDetailsView.Builder(getActivity())
                .setMusicName(music.getTitle())
                .setMusicPath(music.getPath())
                .setFileName(music.getFileName())
                .setMusicDuration(MusicUtils.convertTimeMilesToString(music.getDuration()))
                .setFileSize(MusicUtils.convertMB(music.getFileSize()))
                .setMusicQuality("普通").create();
        bottomSheetDialog.setContentView(musicDetailsView);
        bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT);//去除背景 设置圆角效果
        bottomSheetDialog.show();
    }

    /**
     * 分享
     */
    private void share(Music music) {
        File file = new File(music.getPath());
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("audio/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        startActivity(Intent.createChooser(intent, getString(R.string.share)));
    }
}
