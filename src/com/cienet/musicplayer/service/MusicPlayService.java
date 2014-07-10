package com.cienet.musicplayer.service;

import java.io.IOException;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * MusicPlayService
 * 
 * @author chaochen
 * 
 */
public class MusicPlayService extends Service {
  private static String TAG = "MusicPlayService";
  private MediaPlayer mPlayer;
  public static final String PLAY_PREPARED_END = "com.cienet.musicplayer.service.prepared";
  public static final String PLAY_COMPLETED = "com.cienet.musicplayer.service.playcomplete";

  /**
   * preparedListener
   */
  MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {

    @Override
    public void onPrepared(MediaPlayer mp) {
      Log.d(TAG, PLAY_PREPARED_END);
      broadcastEvent(PLAY_PREPARED_END);
    }
  };

  /**
   * completionListener
   */
  MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
    @Override
    public void onCompletion(MediaPlayer mp) {
      Log.d(TAG, PLAY_COMPLETED);
      broadcastEvent(PLAY_COMPLETED);

    }
  };

  /**
   * send broadcast
   * 
   * @param what
   */
  private void broadcastEvent(String what) {
    Intent i = new Intent(what);
    sendBroadcast(i);
  }

  @Override
  public void onCreate() {
    super.onCreate();
    mPlayer = new MediaPlayer();
    mPlayer.setOnPreparedListener(mPreparedListener);
    mPlayer.setOnCompletionListener(mCompletionListener);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return super.onStartCommand(intent, flags, startId);
  }

  public void start() {
    mPlayer.start();
  }

  public void setDataSource(String path) {

    try {
      mPlayer.reset();
      mPlayer.setDataSource(path);
      mPlayer.prepare();
    } catch (IOException e) {
      return;
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  public void stop() {
    mPlayer.stop();
  }

  public void pause() {
    mPlayer.pause();
  }

  public int getDuration() {
    return mPlayer.getDuration();
  }


  public int getPosition() {
    return mPlayer.getCurrentPosition();
  }


  public long seek(long whereto) {
    mPlayer.seekTo((int) whereto);
    return whereto;
  }


  public boolean isPlaying() {
    return mPlayer.isPlaying();
  }

  private final IBinder binder = new MusicBinder();

  @Override
  public IBinder onBind(Intent intent) {
    return binder;
  }

  public class MusicBinder extends Binder {
    public MusicPlayService getService() {
      return MusicPlayService.this;
    }
  }

  public void reset() {
    mPlayer.reset();
  }

}
