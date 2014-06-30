package com.cienet.musicplayer.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.cienet.musicplayer.R;
import com.cienet.musicplayer.adapter.SongListAdapter;
import com.cienet.musicplayer.entity.Song;

public class SongListFragment extends Fragment {
  private String TAG = SongListFragment.class.getName();
  private List<Song> songs;
  private SongListAdapter songListAdapter;
  private ListView musicListView = null;

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    Log.i(TAG, "----------onAttach");
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.i(TAG, "--------onCreate");

    songs = new ArrayList<Song>();
    for (int i = 10; i < 30; i++) {
      songs.add(new Song("歌曲" + i, "专辑" + i));
    }
    songListAdapter = new SongListAdapter(getActivity(), songs);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.music_list, container, false);
    musicListView = (ListView) view.findViewById(R.id.music_list_view);

    musicListView.setOnItemClickListener(new OnItemClickListener() {

      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(view.getContext(), "你点击了第" + position + "个Item", Toast.LENGTH_LONG).show();

      }

    });

    musicListView.setOnItemLongClickListener(new OnItemLongClickListener() {

      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(view.getContext(), "你长按了第" + position + "个Item", Toast.LENGTH_LONG).show();
        // 如果为true,则触发了setOnItemLongClickListener事件后不触发setOnItemClickListener事件
        return true;
      }

    });

    musicListView.setAdapter(songListAdapter);

    Log.i(TAG, "--------onCreateView");
    return view;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    Log.i(TAG, "--------onActivityCreated");

  }



}
