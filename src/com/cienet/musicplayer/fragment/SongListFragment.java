package com.cienet.musicplayer.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.cienet.musicplayer.R;
import com.cienet.musicplayer.adapter.SongListAdapter;
import com.cienet.musicplayer.entity.Song;

public class SongListFragment extends Fragment {
  private String TAG = SongListFragment.class.getName();
  private List<Song> songs;
  private ListView musicListView;
  private Cursor mCursor;
  private OnItemSelectedListener mListener;

  /**
   * onAttach
   */
  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (OnItemSelectedListener) activity;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * onCreate
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.i(TAG, "onCreate");
  }

  /**
   * onCreateView
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.music_list, container, false);
    musicListView = (ListView) view.findViewById(R.id.music_list_view);
    setListData();
    musicListView.setAdapter(new SongListAdapter(getActivity(), songs));
    /**
     * Click Item and Play the music
     */
    musicListView.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mListener.onItemSelected(position, "SongListFragment");
      }
    });

    return view;
  }

  private void setListData() {
    songs = new ArrayList<Song>();
    mCursor =
        this.getActivity()
            .getContentResolver()
            .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    for (int i = 0; i < mCursor.getCount(); i++) {
      Song song = new Song();
      mCursor.moveToNext();
      String name = mCursor.getString((mCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));// 歌曲标题
      String artist = mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));// 艺术家
      String album = mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM));// 专辑
      String url = mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.DATA));// 路径
      long songId = mCursor.getLong(mCursor.getColumnIndex(MediaStore.Audio.Media._ID));
      long albumId = mCursor.getLong(mCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
      long duration = mCursor.getLong(mCursor.getColumnIndex(MediaStore.Audio.Media.DURATION)); // 时长
      int isMusic = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));// 是否为音乐
      if (isMusic != 0) { // add music to list
        song.setName(name);
        song.setSinger(artist);
        song.setAlbum(album);
        song.setUrl(url);
        song.setAlbumId(albumId);
        song.setSongId(songId);
        song.setDuration(duration);
        songs.add(song);
      }
    }
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    Log.i(TAG, "onActivityCreated");
  }

  public interface OnItemSelectedListener {
    public void onItemSelected(int position, String fragmentTag);
  }
}
