package com.cienet.musicplayer.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cienet.musicplayer.R;
import com.cienet.musicplayer.entity.Song;

/**
 * 歌曲列表适配器
 * 
 * @author chaochen
 * 
 */
public class SongListAdapter extends BaseAdapter {

  private List<Song> songs;
  private Context context;

  public SongListAdapter() {
    super();
  }

  public SongListAdapter(Context context, List<Song> songs) {
    super();
    this.songs = songs;
    this.context = context;
  }

  @Override
  public int getCount() {
    return (songs == null) ? 0 : songs.size();
  }

  /**
   * getItem方法不是在Baseadapter类中被调用的，而是在Adapterview中被调用的。
   * 它也不会被自动调用，它是用来在我们设置setOnItemClickListener、setOnItemLongClickListener、
   * setOnItemSelectedListener的点击选择处理事件中方便地调用来获取当前行数据的。
   */
  @Override
  public Object getItem(int position) {
    return songs.get(position);
  }

  /**
   * 它返回的是该postion对应item的id,adapterview也有类似方法：
   * 某些方法（如onclicklistener的onclick方法）有id这个参数，而这个id参数就是取决于getItemId()这个返回值的。
   */
  @Override
  public long getItemId(int position) {
    return position;
  }

  public class ViewHolder {
    TextView songName;
    TextView songAblum;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    final Song song = (Song) getItem(position);
    ViewHolder viewHolder = null;
    if (convertView == null) {
      Log.i("MyBaseAdapter", "新建convertView,position=" + position);
      convertView = LayoutInflater.from(context).inflate(R.layout.music_list_item, null);

      viewHolder = new ViewHolder();
      viewHolder.songName = (TextView) convertView.findViewById(R.id.song_name);
      viewHolder.songAblum = (TextView) convertView.findViewById(R.id.song_album);


      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
      Log.i("MyBaseAdapter", "旧的convertView,position=" + position);
    }

    viewHolder.songName.setText(song.getName());
    viewHolder.songAblum.setText(song.getAlbum());

    // 对ListView中第1个TextView配置OnClick事件
    // viewHolder.textViewItem01.setOnClickListener(new OnClickListener() {
    // @Override
    // public void onClick(View v) {
    // Toast.makeText(context,
    // "[textViewItem01.setOnClickListener]点击了" + song.name,
    // Toast.LENGTH_SHORT).show();
    // }
    // });

    // 对ListView中的每一行信息配置OnClick事件
    // convertView.setOnClickListener(new OnClickListener() {
    //
    // @Override
    // public void onClick(View v) {
    // Toast.makeText(context,
    // "[convertView.setOnClickListener]点击了" + song.name,
    // Toast.LENGTH_SHORT).show();
    // }
    //
    // });

    // 对ListView中的每一行信息配置OnLongClick事件
    // convertView.setOnLongClickListener(new OnLongClickListener() {
    // @Override
    // public boolean onLongClick(View v) {
    // Toast.makeText(context,
    // "[convertView.setOnLongClickListener]点击了" + song.name,
    // Toast.LENGTH_SHORT).show();
    // return true;
    // }
    // });

    return convertView;
  }

  public void initListAllSongs() {
    songs = new ArrayList<Song>();
    for (int i = 10; i < 30; i++) {
      songs.add(new Song("歌曲" + i, "专辑" + i));
    }
  }

}
