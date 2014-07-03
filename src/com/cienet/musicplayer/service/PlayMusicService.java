package com.cienet.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

/**
 * 音乐播放的Service层
 * 
 * @author chaochen
 * 
 */
public class PlayMusicService extends Service {

  // 为日志工具设置标签
  private static String TAG = "PlayMusicService";
  // 定义音乐播放器变量
  private MediaPlayer mPlayer;

  @Override
  public IBinder onBind(Intent intent) {
    // TODO Auto-generated method stub
    return null;
  }

}
