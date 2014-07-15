package com.cienet.musicplayer.activity;

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
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cienet.musicplayer.R;
import com.cienet.musicplayer.entity.Song;
import com.cienet.musicplayer.service.MusicPlayService;
import com.cienet.musicplayer.util.ArtworkUtils;

/**
 * @author chaochen
 * 
 */
public class SongPlayActivity extends Activity {
  private String TAG = this.getClass().getName();
  private ImageButton shuffleButtom, previousButton, playButton, nextButton, repeatButton,
      switchButton;
  private ImageView albumImage;
  private TextView songName, singer, timeStart, timeEnd;
  private boolean isShuffle = false;// 是否随机播放
  private boolean isRepeat = false;// 是否循环播放
  private boolean isPlaying = false;// 是否循环播放
  private static int musicPosition;
  private String tag;
  private Cursor mCursor;
  private List<Song> songs;
  private MusicPlayService mMusicPlayerService;
  private SeekBar seekBar;
  private Context context;


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
              if (songs.get(musicPosition).getImage() == null) {
                songs.get(musicPosition).setImage(
                    ArtworkUtils.getArtwork(context, songs.get(musicPosition).getName(),
                        songs.get(musicPosition).getSongId(),
                        songs.get(musicPosition).getAlbumId(), true));
              }
              mMusicPlayerService.setDataSource(songs.get(musicPosition).getUrl());
              singer.setText(songs.get(musicPosition).getSinger());
              songName.setText(songs.get(musicPosition).getName());
              timeEnd.setText(formatTime(songs.get(musicPosition).getDuration()));
              // 获得歌曲的长度并设置成播放进度条的最大值
              seekBar.setMax((int) songs.get(musicPosition).getDuration());
              albumImage.setImageBitmap(songs.get(musicPosition).getImage());
              mMusicPlayerService.start();
              // 启动
              handler.post(updateThread);
            }
          }
          break;
        default:
          break;
      }
    }
  };


  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.i(TAG, "----------onCreate");
    super.onCreate(savedInstanceState);
    this.setContentView(R.layout.song_play);
    initView();
    bindService(new Intent(this, MusicPlayService.class), mPlaybackConnection,
        Context.BIND_AUTO_CREATE);
    setListener();
  }

  Handler handler = new Handler();
  Runnable updateThread = new Runnable() {
    public void run() {
      // 获得歌曲现在播放位置并设置成播放进度条的值
      seekBar.setProgress(mMusicPlayerService.getPosition());
      timeStart.setText(formatTime(mMusicPlayerService.getPosition()));
      // 每次延迟100毫秒再启动线程
      handler.postDelayed(updateThread, 100);
    }
  };

  private void setListener() {
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
          // 启动
          handler.post(updateThread);
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
            if (songs.get(musicPosition).getImage() == null) {
              songs.get(musicPosition).setImage(
                  ArtworkUtils.getArtwork(context, songs.get(musicPosition).getName(),
                      songs.get(musicPosition).getSongId(), songs.get(musicPosition).getAlbumId(),
                      true));
            }
            singer.setText(songs.get(musicPosition).getSinger());
            songName.setText(songs.get(musicPosition).getName());
            albumImage.setImageBitmap(songs.get(musicPosition).getImage());
            timeEnd.setText(formatTime(songs.get(musicPosition).getDuration()));
            // 获得歌曲的长度并设置成播放进度条的最大值
            seekBar.setMax((int) songs.get(musicPosition).getDuration());
            mMusicPlayerService.setDataSource(songs.get(musicPosition).getUrl());
            mMusicPlayerService.start();
            // 启动
            handler.post(updateThread);
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
            if (songs.get(musicPosition).getImage() == null) {
              songs.get(musicPosition).setImage(
                  ArtworkUtils.getArtwork(context, songs.get(musicPosition).getName(),
                      songs.get(musicPosition).getSongId(), songs.get(musicPosition).getAlbumId(),
                      true));
            }
            mMusicPlayerService.setDataSource(songs.get(musicPosition).getUrl());
            singer.setText(songs.get(musicPosition).getSinger());
            songName.setText(songs.get(musicPosition).getName());
            timeEnd.setText(formatTime(songs.get(musicPosition).getDuration()));
            // 获得歌曲的长度并设置成播放进度条的最大值
            seekBar.setMax((int) songs.get(musicPosition).getDuration());
            albumImage.setImageBitmap(songs.get(musicPosition).getImage());
            mMusicPlayerService.start();
            // 启动
            handler.post(updateThread);
          }
        }
      }
    });

    shuffleButtom.setOnClickListener(new ImageButton.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (isShuffle) {
          isShuffle = false;
          shuffleButtom.setImageResource(R.drawable.btn_playback_shuffle);
        } else {
          isShuffle = true;
          shuffleButtom.setImageResource(R.drawable.btn_playback_shuffle_all);
        }

      }
    });

    // repeatButton.setOnClickListener(new ImageButton.OnClickListener() {
    // @Override
    // public void onClick(View v) {
    //
    // }
    // });
    //
    // switchButton.setOnClickListener(new ImageButton.OnClickListener() {
    // @Override
    // public void onClick(View v) {
    //
    // }
    // });


    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // fromUser判断是用户改变的滑块的值
        if (fromUser == true) {
          mMusicPlayerService.seek(progress);
        }
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {}

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {}
    });
    IntentFilter filter = new IntentFilter();
    filter.addAction(MusicPlayService.PLAY_PREPARED_END);
    filter.addAction(MusicPlayService.PLAY_COMPLETED);
    getApplicationContext().registerReceiver(mPlayerEvtReceiver, filter);
  }

  private void initView() {
    shuffleButtom = (ImageButton) findViewById(R.id.shuffle);
    previousButton = (ImageButton) findViewById(R.id.lastButton);
    playButton = (ImageButton) findViewById(R.id.playButton);
    nextButton = (ImageButton) findViewById(R.id.nextButton);
    repeatButton = (ImageButton) findViewById(R.id.repeat_one);
    albumImage = (ImageView) findViewById(R.id.albumImage);
    songName = (TextView) findViewById(R.id.song_name);
    singer = (TextView) findViewById(R.id.singer);
    timeStart = (TextView) findViewById(R.id.time_start);
    timeEnd = (TextView) findViewById(R.id.time_end);
    switchButton = (ImageButton) findViewById(R.id.switch_queue);
    seekBar = (SeekBar) findViewById(R.id.time_line);
    context = getApplicationContext();


    Bundle bundle = this.getIntent().getExtras();
    musicPosition = bundle.getInt("position");
    tag = bundle.getString("tag");
    isPlaying = bundle.getBoolean("isPlaying");

    if ("SongListFragment".equals(tag)) {
      songs = new ArrayList<Song>();
      mCursor =
          this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
              null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
      for (int i = 0; i < mCursor.getCount(); i++) {
        Song song = new Song();
        mCursor.moveToNext();
        String name = mCursor.getString((mCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));// 歌曲标题
        String artist = mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));// 艺术家
        String album =
            mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM));// 专辑
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
  }

  @Override
  public void onStart() {
    Log.i(TAG, "----------onStart");
    super.onStart();

    songName.setText(songs.get(musicPosition).getName());
    singer.setText(songs.get(musicPosition).getSinger());
    timeEnd.setText(formatTime(songs.get(musicPosition).getDuration()));
    if (songs.get(musicPosition).getImage() == null) {
      songs.get(musicPosition).setImage(
          ArtworkUtils.getArtwork(context, songs.get(musicPosition).getName(),
              songs.get(musicPosition).getSongId(), songs.get(musicPosition).getAlbumId(), true));
    }
    albumImage.setImageBitmap(songs.get(musicPosition).getImage());
    // 获得歌曲的长度并设置成播放进度条的最大值
    seekBar.setMax((int) songs.get(musicPosition).getDuration());

    albumImage.setImageBitmap(songs.get(musicPosition).getImage());
    // 启动
    handler.post(updateThread);
    if (isPlaying) {
      playButton.setBackgroundResource(R.drawable.btn_playback_pause);
    }

  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    unbindService(mPlaybackConnection);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    // 设置淡入淡出效果
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }

  private String formatTime(long time) {
    String min = time / (1000 * 60) + "";
    String sec = time % (1000 * 60) + "";
    if (min.length() < 2) {
      min = "0" + time / (1000 * 60) + "";
    } else {
      min = time / (1000 * 60) + "";
    }
    if (sec.length() == 4) {
      sec = "0" + (time % (1000 * 60)) + "";
    } else if (sec.length() == 3) {
      sec = "00" + (time % (1000 * 60)) + "";
    } else if (sec.length() == 2) {
      sec = "000" + (time % (1000 * 60)) + "";
    } else if (sec.length() == 1) {
      sec = "0000" + (time % (1000 * 60)) + "";
    }
    return min + ":" + sec.trim().substring(0, 2);
  }
}
