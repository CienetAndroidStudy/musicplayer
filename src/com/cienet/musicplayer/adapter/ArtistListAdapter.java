package com.cienet.musicplayer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cienet.musicplayer.R;

/**
 * ArtListAdapter
 * 
 * @author alexwan
 * 
 */
public class ArtistListAdapter extends BaseAdapter {
  private Context mContext;
  private String[] artists;
  private int[] mAlbumNums;

  public ArtistListAdapter(Context context, String[] string, int[] albumNums) {
    mContext = context;
    artists = string;
    mAlbumNums = albumNums;
  }

  @Override
  public int getCount() {
    return artists.length;
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
    public TextView albumNum, artist;
    public ImageView artistsItem;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder = null;
    View view = convertView;
    if (convertView == null) {
      Log.i("ArtListAdapter", "ArtListAdapter：" + position);
      viewHolder = new ViewHolder();
      view = LayoutInflater.from(mContext).inflate(R.layout.artist_list_item, null);
      viewHolder.artist = (TextView) view.findViewById(R.id.artist);
      viewHolder.albumNum = (TextView) view.findViewById(R.id.album_num);
      // viewHolder.artistsItem = (ImageView) view.findViewById(R.id.artist_item);
      view.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }
    /*
     * if (artists[position].length() > 15) {//set album name
     * viewHolder.artist.setText(artists[position].substring(0, 12) + "..."); } else {
     * viewHolder.artist.setText(artists[position]); }
     */
    viewHolder.artist.setText(artists[position]);
    viewHolder.artist.setBackgroundColor(0x80000000);
    viewHolder.albumNum.setText(mAlbumNums[position] + "  albums");
    viewHolder.albumNum.setBackgroundColor(0x80000000);

    // viewHolder.artistsItem.setImageResource(R.drawable.default_artwork);
    return view;
  }

}
