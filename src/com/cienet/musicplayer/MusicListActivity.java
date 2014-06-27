package com.cienet.musicplayer;

import java.util.ArrayList;
import java.util.List;

import com.cienet.musicplayer.adapter.SongListAdapter;
import com.cienet.musicplayer.entity.Song;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MusicListActivity extends Activity {

  private List<Song> songs;
  private ListAdapter adapter = null;
  private ListView listView = null;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.music_list);

    // 绑定Layout里面的ListView
    listView = (ListView) findViewById(R.id.music_list_view);

    // 显示ListView
    initListAllSongs();
    showByMyBaseAdapter();
  }

  public void initListAllSongs() {
    songs = new ArrayList<Song>();
    for (int i = 10; i < 30; i++) {
      songs.add(new Song("歌曲" + i, "专辑" + i, R.drawable.ic_action_favorite));
    }
  }

  public void showByMyBaseAdapter() {
    adapter = new SongListAdapter(this, songs);
    listView.setAdapter(adapter);
  }
}
