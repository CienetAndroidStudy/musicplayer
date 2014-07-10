package com.cienet.musicplayer.app;

import android.app.Application;

import com.cienet.musicplayer.controller.MusicInfoController;

public class MusicPlayerApp extends Application{
  private MusicInfoController mMusicInfoController = null;
  
  public void onCreate() {
    super.onCreate();
    mMusicInfoController = MusicInfoController.getInstance(this);
  }

  public MusicInfoController getMusicInfoController() {
    return mMusicInfoController;
  }
}
