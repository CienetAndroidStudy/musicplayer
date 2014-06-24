package com.cienet.musicplayer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MusicListActivity extends Activity {
	
    List<Song> songs ;  
    ListAdapter adapter = null;  
    ListView listView = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_list);
        
        //绑定Layout里面的ListView
        listView = (ListView) findViewById(R.id.ListView01);
        
        //显示ListView  
        initListAllSongs();  
        showByMyBaseAdapter();
    }
	
	public void initListAllSongs(){
		songs = new ArrayList<Song>();
		for(int i=10;i<30;i++){
			songs.add(new Song("歌曲"+i,"专辑"+i,R.drawable.ic_action_favorite));
		}
	}
	
	public void showByMyBaseAdapter(){
		adapter = new MyBaseAdapter(this, songs);  
        listView.setAdapter(adapter);
	}
}