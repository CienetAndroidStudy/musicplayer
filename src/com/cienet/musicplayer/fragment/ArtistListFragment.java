package com.cienet.musicplayer.fragment;

import java.util.HashSet;
import java.util.Iterator;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;
import com.cienet.musicplayer.R;
import com.cienet.musicplayer.adapter.ArtistListAdapter;

/**
 * ArtistListFragment
 * 
 * @author alexwan
 * 
 */
public class ArtistListFragment extends Fragment {
  private String[] artists;
  private GridView mArtistListView;
  private int[] alblumNums;

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  private void setListData() {
    // Get the media music info
    Cursor c =
        this.getActivity()
            .getContentResolver()
            .query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[] {MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME}, null, null,
                MediaStore.Audio.Media.ARTIST);
    // If no media music
    if (c == null || c.getCount() == 0) {
      return;
    }
    // Move to the first music
    c.moveToFirst();
    // Get music numbers
    int num = c.getCount();
    alblumNums = new int[num];
    HashSet<String> set = new HashSet<String>();
    for (int i = 0; i < num; i++) {
      // Get artist name
      String artist = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
      set.add(artist);
      c.moveToNext();
    }
    num = set.size();
    Iterator<String> it = set.iterator();
    artists = new String[num];
    int i = 0;
    while (it.hasNext()) {
      artists[i] = it.next().toString();
      i++;
    }
    // Get the album nums
    for (int y = 0; y < artists.length; y++) {
      HashSet<String> albumSet = new HashSet<String>();
      c.moveToFirst();
      for (int x = 0; x < c.getCount(); x++) {
        if (artists[y].equals(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST)))) {
          String album = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM));
          albumSet.add(album);
        }
        c.moveToNext();
      }
      alblumNums[y] = albumSet.size();
    }
    mArtistListView.setAdapter(new ArtistListAdapter(this.getActivity(), artists, alblumNums));
    mArtistListView.setOnItemClickListener(new AlbumsItemClickListener());
  }

  class AlbumsItemClickListener implements OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
      Toast toast = Toast.makeText(getActivity(), "hahaha", Toast.LENGTH_SHORT);
      toast.show();
      // Intent intent = new Intent();
      // intent.setClass(AlbumActivity.this, SongsOfAlbumActivity.class);
      // intent.putExtra("albums", albums[position]);
      // startActivity(intent);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.artist_list, container, false);
    mArtistListView = (GridView) view.findViewById(R.id.artist_list_item);
    setListData();
    return view;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

}
