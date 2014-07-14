package com.cienet.musicplayer.fragment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import com.cienet.musicplayer.adapter.AlbumListAdapter;
import com.cienet.musicplayer.util.ArtworkUtils;

public class AlbumListFragment extends Fragment {
  private String[] albums;
  private GridView albumListView;
  private Context context;

  @Override
  public void onAttach(Activity activity) {
    context = this.getActivity();
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
                    MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.ALBUM_ID}, null, null, MediaStore.Audio.Media.ALBUM);
    // If no media music
    if (c == null || c.getCount() == 0) {
      return;
    }
    // Move to the first music
    c.moveToFirst();
    // Get music numbers
    int num = c.getCount();
    HashSet<String> alblumSet = new HashSet<String>();
    HashMap<String, String> singerMap = new HashMap<String, String>();
    HashMap<String, Bitmap> imageMap = new HashMap<String, Bitmap>();
    for (int i = 0; i < num; i++) {
      // Get album name
      String album = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM));
      String singer = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
      long songId = c.getLong(c.getColumnIndex(MediaStore.Audio.Media._ID));
      long albumId = c.getLong(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
      String name = c.getString((c.getColumnIndex(MediaStore.Audio.Media.TITLE)));
      alblumSet.add(album);
      singerMap.put(album, singer);
      if (!imageMap.containsKey(album)) {
        Bitmap image = ArtworkUtils.getArtwork(context, name, songId, albumId, true);
        imageMap.put(album, image);
      }
      c.moveToNext();
    }

    num = alblumSet.size();
    Iterator<String> it = alblumSet.iterator();
    albums = new String[num];
    int i = 0;
    while (it.hasNext()) {
      albums[i] = it.next().toString();
      i++;
    }
    c.moveToFirst();
    albumListView.setAdapter(new AlbumListAdapter(context, albums, singerMap, imageMap));
    albumListView.setOnItemClickListener(new AlbumsItemClickListener());
  }

  class AlbumsItemClickListener implements OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
      Toast toast = Toast.makeText(getActivity(), this.getClass().getName(), Toast.LENGTH_SHORT);
      toast.show();
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.album_list, container, false);
    albumListView = (GridView) view.findViewById(R.id.album_list_item);
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
