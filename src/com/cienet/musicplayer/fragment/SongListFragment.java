package com.cienet.musicplayer.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cienet.musicplayer.R;
import com.cienet.musicplayer.activity.SongPlayActivity;
import com.cienet.musicplayer.adapter.SongListAdapter;
import com.cienet.musicplayer.app.MusicPlayerApp;
import com.cienet.musicplayer.controller.MusicInfoController;
import com.cienet.musicplayer.entity.Song;
import com.cienet.musicplayer.service.MusicPlayService;
import com.cienet.musicplayer.util.ArtworkUtils;

public class SongListFragment extends Fragment {
  private String TAG = SongListFragment.class.getName();
  private List<Song> songs;
  private SongListAdapter songListAdapter;
  private ListView musicListView;
  private ImageButton previousButton;
  private ImageButton playButton;
  private ImageButton nextButton;
  private ImageButton albumImage;
  private TextView singer;
  private TextView songName;
  private static int musicPosition;
  private MusicPlayService mMusicPlayerService;
  private MusicInfoController mMusicInfoController;
  private Cursor mCursor;
  private RelativeLayout bottomlayout;
  /**
   * service connect
   */
  private ServiceConnection mPlaybackConnection = new ServiceConnection() {
    public void onServiceConnected(ComponentName className, IBinder service) {
      mMusicPlayerService = ((MusicPlayService.MusicBinder) service).getService();
    }

    public void onServiceDisconnected(ComponentName className) {
      mMusicPlayerService = null;
    }
  };
  /**
   * Get BroadcastReceiver from Service
   */
  protected BroadcastReceiver mPlayerEvtReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      Log.i(TAG, "BroadcastReceiver：" + intent.getAction());
      String action = intent.getAction();

      if (action.equals(MusicPlayService.PLAY_PREPARED_END)) {
        playButton.setBackgroundResource(R.drawable.btn_playback_pause);
        Log.i(TAG, "PLAY_PREPARED_END");
      } else if (action.equals(MusicPlayService.PLAY_COMPLETED)) {
        playButton.setBackgroundResource(R.drawable.btn_playback_play);
        Log.i(TAG, "PLAY_COMPLETED");
      }
    }
  };

  /**
   * onAttach
   */
  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    Log.i(TAG, "----------onAttach");
    bottomlayout = (RelativeLayout) activity.findViewById(R.id.bottom_bar);
    previousButton = (ImageButton) bottomlayout.findViewById(R.id.lastButton);
    playButton = (ImageButton) bottomlayout.findViewById(R.id.playButton);
    nextButton = (ImageButton) bottomlayout.findViewById(R.id.nextButton);
    singer = (TextView) bottomlayout.findViewById(R.id.singer);
    songName = (TextView) bottomlayout.findViewById(R.id.song_name);
    // albumImage = (ImageButton) bottomlayout.findViewById(R.id.song_image);
  }

  /**
   * onCreate
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.i(TAG, "--------onCreate");
    MusicPlayerApp musicPlayerApp = (MusicPlayerApp) getActivity().getApplication();
    mMusicInfoController = (musicPlayerApp).getMusicInfoController();
    /* bind playback service */
    Log.i(TAG, "startService");
    getAllSongs();
    getActivity().startService(new Intent(getActivity(), MusicPlayService.class));
    getActivity().bindService(new Intent(getActivity(), MusicPlayService.class),
        mPlaybackConnection, Context.BIND_AUTO_CREATE);
  }

  /**
   * Get music from sdCards
   */
  public ListAdapter getAllSongs() {
    songs = new ArrayList<Song>();
    mCursor =
        mMusicInfoController.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
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
        // song.setImage(ArtworkUtils.getArtwork(this.getActivity().getApplicationContext(), name,
        // songId, albumId, true));
        songs.add(song);
      }
    }
    songListAdapter = new SongListAdapter(getActivity(), songs);
    return songListAdapter;
  }

  /**
   * onCreateView
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.music_list, container, false);
    musicListView = (ListView) view.findViewById(R.id.music_list_view);
    musicListView.setAdapter(getAllSongs());
    /**
     * Click Item and Play the music
     */
    musicListView.setOnItemClickListener(new OnItemClickListener() {

      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (songs.size() <= 0) {
          return;
        }
        Log.i(TAG, "OnItemClick：" + position + songs.size());
        musicPosition = position;
        singer.setText(songs.get(position).getSinger());
        songName.setText(songs.get(position).getName());
        // albumImage.setImageBitmap(songs.get(position).getImage());
        mMusicPlayerService.setDataSource(songs.get(musicPosition).getUrl());
        mMusicPlayerService.start();
        playButton.setBackgroundResource(R.drawable.btn_playback_pause);
      }
    });

    musicListView.setOnItemLongClickListener(new OnItemLongClickListener() {

      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(view.getContext(), "Long Press the " + position + "  Item",
            Toast.LENGTH_LONG).show();
        return true;
      }

    });
    /**
     * click the playOrPause button
     */
    playButton.setOnClickListener(new ImageButton.OnClickListener() {

      @Override
      public void onClick(View v) {
        Log.e(TAG, "playButton setOnClickListener");
        if (mMusicPlayerService != null && mMusicPlayerService.isPlaying()) {
          mMusicPlayerService.pause();
          playButton.setBackgroundResource(R.drawable.btn_playback_play);
        } else if (mMusicPlayerService != null) {
          mMusicPlayerService.start();
          playButton.setBackgroundResource(R.drawable.btn_playback_pause);
        }
      }
    });

    /**
     * previous music
     */
    previousButton.setOnClickListener(new ImageButton.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (musicPosition >= 1) {
          if (mMusicPlayerService != null) {
            if (mMusicPlayerService.isPlaying()) {
              mMusicPlayerService.reset();
            }
            musicPosition = musicPosition - 1;
            playButton.setBackgroundResource(R.drawable.btn_playback_pause);
            singer.setText(songs.get(musicPosition).getSinger());
            songName.setText(songs.get(musicPosition).getName());
            // albumImage.setImageBitmap(songs.get(musicPosition).getImage());
            mMusicPlayerService.setDataSource(songs.get(musicPosition).getUrl());
            mMusicPlayerService.start();
          }
        }
      }
    });
    /**
     * next music
     */
    nextButton.setOnClickListener(new ImageButton.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (musicPosition < songs.size()) {
          if (mMusicPlayerService != null) {
            if (mMusicPlayerService.isPlaying()) {
              mMusicPlayerService.reset();
            }
            musicPosition = musicPosition + 1;
            playButton.setBackgroundResource(R.drawable.btn_playback_pause);
            mMusicPlayerService.setDataSource(songs.get(musicPosition).getUrl());
            singer.setText(songs.get(musicPosition).getSinger());
            songName.setText(songs.get(musicPosition).getName());
            // albumImage.setImageBitmap(songs.get(musicPosition).getImage());
            mMusicPlayerService.start();

          }
        }
      }
    });

    bottomlayout.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {

        Bundle bundle = new Bundle();
        bundle.putInt("position", musicPosition);
        bundle.putString("tag", "SongListFragment");
        Intent intent = new Intent();
        intent.setClass(getActivity(), SongPlayActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

        // 设置淡入淡出效果
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
      }

    });
    IntentFilter filter = new IntentFilter();
    filter.addAction(MusicPlayService.PLAY_PREPARED_END);
    filter.addAction(MusicPlayService.PLAY_COMPLETED);
    getActivity().registerReceiver(mPlayerEvtReceiver, filter);
    return view;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    Log.i(TAG, "--------onActivityCreated");
  }

}
