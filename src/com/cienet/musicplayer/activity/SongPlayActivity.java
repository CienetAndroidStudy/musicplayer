package com.cienet.musicplayer.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cienet.musicplayer.R;
import com.cienet.musicplayer.service.MusicPlayService;

/**
 * @author chaochen
 * 
 */
public class SongPlayActivity extends Activity {
  private String TAG = this.getClass().getName();
  private ImageButton shuffleButtom, previousButton, playButton, nextButton, repeatButton;
  private Context context;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.i(TAG, "----------onCreate");
    super.onCreate(savedInstanceState);
    this.setContentView(R.layout.song_play);
    initView();
  }

  private void initView() {
    shuffleButtom = (ImageButton) findViewById(R.id.shuffle);
    previousButton = (ImageButton) findViewById(R.id.lastButton);
    playButton = (ImageButton) findViewById(R.id.playButton);
    nextButton = (ImageButton) findViewById(R.id.nextButton);
    repeatButton = (ImageButton) findViewById(R.id.repeat_one);
    context = this.getApplicationContext();
  }

  @Override
  public void onStart() {
    Log.i(TAG, "----------onStart");
    super.onStart();

    previousButton.setOnClickListener(new ImageButton.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(context, TAG, Toast.LENGTH_LONG).show();
      }
    });
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    // 设置淡入淡出效果
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }

}
