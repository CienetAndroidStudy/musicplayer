package com.cienet.musicplayer.adapter;

import java.util.HashMap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cienet.musicplayer.R;

public class AlbumListAdapter extends BaseAdapter {
  private Context context;
  private String[] albums;
  private HashMap<String, String> map;

  public AlbumListAdapter(Context context, String[] albums, HashMap<String, String> map) {
    this.context = context;
    this.albums = albums;
    this.map = map;
  }

  @Override
  public int getCount() {
    return albums.length;
  }

  @Override
  public Object getItem(int position) {
    return position;
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  class ViewHolder {
    public TextView albumName, albumSinger;
    public ImageView albumImage;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder = null;
    View view = convertView;
    if (convertView == null) {
      Log.i("ArtListAdapter", "ArtListAdapter：" + position);
      viewHolder = new ViewHolder();
      view = LayoutInflater.from(context).inflate(R.layout.album_list_item, null);
      viewHolder.albumSinger = (TextView) view.findViewById(R.id.singer);
      viewHolder.albumName = (TextView) view.findViewById(R.id.album);
      // viewHolder.albumImage = (ImageView) view.findViewById(R.id.artist_item);
      view.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }
    viewHolder.albumSinger.setText(map.get(albums[position]));
    viewHolder.albumSinger.setBackgroundColor(0x80000000);
    viewHolder.albumName.setText(albums[position]);
    viewHolder.albumName.setBackgroundColor(0x80000000);

    // viewHolder.artistsItem.setImageResource(R.drawable.default_artwork);
    return view;
  }

}
