package com.cienet.musicplayer.activity;

import java.util.ArrayList;
import java.util.List;

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
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cienet.musicplayer.R;
import com.cienet.musicplayer.entity.Song;
import com.cienet.musicplayer.fragment.AlbumListFragment;
import com.cienet.musicplayer.fragment.ArtistListFragment;
import com.cienet.musicplayer.fragment.SongListFragment;
import com.cienet.musicplayer.service.MusicPlayService;
import com.cienet.musicplayer.util.ArtworkUtils;

/**
 * 主界面
 * 
 * @author chaochen
 * 
 */
public class MainViewActivity extends FragmentActivity implements
    SongListFragment.OnItemSelectedListener {
  private RelativeLayout bottomlayout;
  private ViewPager viewPager;
  private PagerTabStrip pagerTabStrip;
  private List<String> titleList;
  private MusicPlayService mMusicPlayerService;
  private ImageButton previousButton;
  private ImageButton playButton;
  private ImageButton nextButton;
  private ImageButton songImage;
  private TextView singer;
  private TextView songName;
  private int musicPosition;
  private List<Song> songs;
  private Cursor mCursor;
  private Context context;

  // private final static String AlbumListFragment = "AlbumListFragment";
  // private final static String ArtistListFragment = "ArtistListFragment";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_view);
    initView();
    startService(new Intent(this, MusicPlayService.class));
    bindService(new Intent(this, MusicPlayService.class), mPlaybackConnection,
        Context.BIND_AUTO_CREATE);
    setListener();
  }

  private void setListener() {
    /**
     * click the playOrPause button
     */
    playButton.setOnClickListener(new ImageButton.OnClickListener() {

      @Override
      public void onClick(View v) {
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
            singer.setText(songs.get(musicPosition).getSinger());
            songName.setText(songs.get(musicPosition).getName());
            mMusicPlayerService.setDataSource(songs.get(musicPosition).getUrl());
            mMusicPlayerService.start();
          }
        } else {
          singer.setText(songs.get(0).getSinger());
          songName.setText(songs.get(0).getName());
          if (songs.get(musicPosition).getImage() == null) {
            songs.get(musicPosition).setImage(
                ArtworkUtils.getArtwork(context, songs.get(musicPosition).getName(),
                    songs.get(musicPosition).getSongId(), songs.get(musicPosition).getAlbumId(),
                    true));
          }
          songImage.setImageBitmap(songs.get(musicPosition).getImage());
          mMusicPlayerService.setDataSource(songs.get(0).getUrl());
          mMusicPlayerService.start();
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
            mMusicPlayerService.setDataSource(songs.get(musicPosition).getUrl());
            singer.setText(songs.get(musicPosition).getSinger());
            songName.setText(songs.get(musicPosition).getName());
            if (songs.get(musicPosition).getImage() == null) {
              songs.get(musicPosition).setImage(
                  ArtworkUtils.getArtwork(context, songs.get(musicPosition).getName(),
                      songs.get(musicPosition).getSongId(), songs.get(musicPosition).getAlbumId(),
                      true));
            }
            songImage.setImageBitmap(songs.get(musicPosition).getImage());
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
        bundle.putBoolean("isPlaying", mMusicPlayerService.isPlaying());
        Intent intent = new Intent();
        intent.setClass(MainViewActivity.this, SongPlayActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

        // 设置淡入淡出效果
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
      }

    });
    IntentFilter filter = new IntentFilter();
    filter.addAction(MusicPlayService.PLAY_PREPARED_END);
    filter.addAction(MusicPlayService.PLAY_COMPLETED);
    getApplicationContext().registerReceiver(mPlayerEvtReceiver, filter);
  }

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
      String action = intent.getAction();
      switch (action) {
        case MusicPlayService.PLAY_PREPARED_END:
          playButton.setBackgroundResource(R.drawable.btn_playback_pause);
          break;
        case MusicPlayService.PLAY_COMPLETED:
          // Auto play the next song
          playButton.setBackgroundResource(R.drawable.btn_playback_play);
          if (musicPosition < songs.size()) {
            if (mMusicPlayerService != null) {
              if (mMusicPlayerService.isPlaying()) {
                mMusicPlayerService.reset();
              }
              musicPosition = musicPosition + 1;
              mMusicPlayerService.setDataSource(songs.get(musicPosition).getUrl());
              singer.setText(songs.get(musicPosition).getSinger());
              songName.setText(songs.get(musicPosition).getName());
              if (songs.get(musicPosition).getImage() == null) {
                songs.get(musicPosition).setImage(
                    ArtworkUtils.getArtwork(context, songs.get(musicPosition).getName(),
                        songs.get(musicPosition).getSongId(),
                        songs.get(musicPosition).getAlbumId(), true));
              }
              songImage.setImageBitmap(songs.get(musicPosition).getImage());
              mMusicPlayerService.start();
            }
          }
          break;
        default:
          break;
      }
    }
  };

  private void initView() {
    bottomlayout = (RelativeLayout) findViewById(R.id.bottom_bar);
    viewPager = (ViewPager) findViewById(R.id.viewpager);
    viewPager.setAdapter(new PlayerPageAdapter(getSupportFragmentManager()));
    pagerTabStrip = (PagerTabStrip) findViewById(R.id.pagertab);
    pagerTabStrip.setDrawFullUnderline(true);
    pagerTabStrip.setTextSpacing(50);
    previousButton = (ImageButton) findViewById(R.id.lastButton);
    playButton = (ImageButton) findViewById(R.id.playButton);
    nextButton = (ImageButton) findViewById(R.id.nextButton);
    singer = (TextView) findViewById(R.id.singer);
    songName = (TextView) findViewById(R.id.song_name);
    songImage = (ImageButton) findViewById(R.id.song_image);
    context = getApplicationContext();
    setListData();
  }

  private void setListData() {
    songs = new ArrayList<Song>();
    mCursor =
        this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
            null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
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
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.actionbar_top, menu);
    return true;
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_search:
        Toast.makeText(this, "action_search", Toast.LENGTH_LONG).show();
        break;
      case R.id.action_favorite:
        Toast.makeText(this, "action_favorite", Toast.LENGTH_LONG).show();
        break;
    }
    return true;
  }

  class PlayerPageAdapter extends FragmentPagerAdapter {

    private ArrayList<android.support.v4.app.Fragment> list =
        new ArrayList<android.support.v4.app.Fragment>();

    public PlayerPageAdapter(android.support.v4.app.FragmentManager fm) {
      super(fm);

      titleList = new ArrayList<String>();
      titleList.add(getResources().getString(R.string.apollo_playlists));
      titleList.add(getResources().getString(R.string.apollo_recent));
      titleList.add(getResources().getString(R.string.apollo_artists));
      titleList.add(getResources().getString(R.string.apollo_albums));
      titleList.add(getResources().getString(R.string.apollo_songs));
      titleList.add(getResources().getString(R.string.apollo_genres));

      list.add(new SongListFragment());
      list.add(new SongListFragment());
      list.add(new ArtistListFragment());
      list.add(new AlbumListFragment());
      list.add(new SongListFragment());
      list.add(new SongListFragment());
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
      return list.get(position);
    }

    @Override
    public int getCount() {
      return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return titleList.get(position);
    }

  }

  @Override
  public void onItemSelected(int position, String fragmentTag) {
    musicPosition = position;
    mMusicPlayerService.setDataSource(songs.get(musicPosition).getUrl());
    singer.setText(songs.get(musicPosition).getSinger());
    songName.setText(songs.get(musicPosition).getName());
    if (songs.get(musicPosition).getImage() == null) {
      songs.get(musicPosition).setImage(
          ArtworkUtils.getArtwork(context, songs.get(musicPosition).getName(),
              songs.get(musicPosition).getSongId(), songs.get(musicPosition).getAlbumId(), true));
    }
    songImage.setImageBitmap(songs.get(musicPosition).getImage());
    mMusicPlayerService.start();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unbindService(mPlaybackConnection);
  }
}
