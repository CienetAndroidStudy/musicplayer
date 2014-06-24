package com.cienet.musicplayer;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyBaseAdapter extends BaseAdapter {

	private List<Song> songs;
	Context context;

	public MyBaseAdapter() {
		super();
	}

	public MyBaseAdapter(Context context, List<Song> songs) {
		super();
		this.songs = songs;
		this.context = context;
	}

	@Override
	public int getCount() {
		return (songs == null) ? 0 : songs.size();
	}

	@Override
	public Object getItem(int position) {
		return songs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public class ViewHolder {
		TextView textViewItem01;
		TextView textViewItem02;
		ImageView imageView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Song song = (Song) getItem(position);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			Log.d("MyBaseAdapter", "新建convertView,position=" + position);
			convertView = LayoutInflater.from(context).inflate(
					R.layout.music_list_item, null);

			viewHolder = new ViewHolder();
			viewHolder.textViewItem01 = (TextView) convertView
					.findViewById(R.id.name);
			viewHolder.textViewItem02 = (TextView) convertView
					.findViewById(R.id.album);
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.ItemImage);


			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			Log.d("MyBaseAdapter", "旧的convertView,position=" + position);
		}

		viewHolder.textViewItem01.setText(song.name);
		viewHolder.textViewItem02.setText(song.album);
		viewHolder.imageView.setImageResource(song.image);

		// 对ListView中第1个TextView配置OnClick事件
//		viewHolder.textViewItem01.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(context,
//						"[textViewItem01.setOnClickListener]点击了" + song.name,
//						Toast.LENGTH_SHORT).show();
//			}
//		});

		// 对ListView中的每一行信息配置OnClick事件
//		convertView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(context,
//						"[convertView.setOnClickListener]点击了" + song.name,
//						Toast.LENGTH_SHORT).show();
//			}
//
//		});

		// 对ListView中的每一行信息配置OnLongClick事件
//		convertView.setOnLongClickListener(new OnLongClickListener() {
//			@Override
//			public boolean onLongClick(View v) {
//				Toast.makeText(context,
//						"[convertView.setOnLongClickListener]点击了" + song.name,
//						Toast.LENGTH_SHORT).show();
//				return true;
//			}
//		});

		return convertView;
	}

}
