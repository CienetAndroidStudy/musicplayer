package com.cienet.musicplayer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainViewActivity extends Activity {

  private View view1, view2, view3;
  private ViewPager viewPager;
  private PagerTabStrip pagerTabStrip;
  private List<View> viewList;
  private List<String> titleList;
  private List<Song> songs;
  private ListAdapter musicListAdapter = null;
  private ListView musicListView = null;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    setContentView(R.layout.view_pager);
    initView();

  }

  private void initView() {
    viewPager = (ViewPager) findViewById(R.id.viewpager);
    pagerTabStrip = (PagerTabStrip) findViewById(R.id.pagertab);
    pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.powderblue));

    pagerTabStrip.setTextColor(getResources().getColor(R.color.powderblue));

    pagerTabStrip.setDrawFullUnderline(false);
    pagerTabStrip.setBackgroundColor(getResources().getColor(R.color.black));
    pagerTabStrip.setTextSpacing(50);

    LayoutInflater lf = getLayoutInflater();
    view1 = lf.inflate(R.layout.layout1, null);
    view2 = lf.inflate(R.layout.layout2, null);
    view3 = lf.inflate(R.layout.music_list, null);

    viewList = new ArrayList<View>();
    viewList.add(view1);
    viewList.add(view2);
    viewList.add(view3);

    titleList = new ArrayList<String>();
    titleList.add("TEXT");
    titleList.add("ALBUMS");
    titleList.add("SONGS");

    PagerAdapter pagerAdapter = new PagerAdapter() {

      @Override
      public boolean isViewFromObject(View arg0, Object arg1) {

        return arg0 == arg1;
      }

      @Override
      public int getCount() {

        return viewList.size();
      }

      @Override
      public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));

      }

      @Override
      public int getItemPosition(Object object) {

        return super.getItemPosition(object);
      }

      @Override
      public CharSequence getPageTitle(int position) {

        return titleList.get(position);
      }

      @Override
      public Object instantiateItem(ViewGroup container, int position) {
        musicListView = (ListView) view3.findViewById(R.id.music_list_view);
        // musicListView.setBackgroundResource(R.drawable.background_holo_dark);

        musicListView.setOnItemClickListener(new OnItemClickListener() {

          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(view.getContext(), "你点击了第" + position + "个Item", Toast.LENGTH_LONG)
                .show();

          }

        });

        musicListView.setOnItemLongClickListener(new OnItemLongClickListener() {

          @Override
          public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(view.getContext(), "你长按了第" + position + "个Item", Toast.LENGTH_LONG)
                .show();
            return true;
          }

        });
        showBySongListAdapter();
        ((ViewPager) container).addView(viewList.get(position), 0);
        return viewList.get(position);
      }

    };
    viewPager.setAdapter(pagerAdapter);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.actionbar_top, menu);
    return true;
  }


  public void showBySongListAdapter() {
    musicListAdapter = new SongListAdapter(this, songs);
    ((SongListAdapter) musicListAdapter).initListAllSongs();
    musicListView.setAdapter(musicListAdapter);
  }

  public void initListAllSongs() {
    songs = new ArrayList<Song>();
    for (int i = 10; i < 30; i++) {
      songs.add(new Song("歌曲" + i, "专辑" + i, R.drawable.ic_action_favorite));
    }
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {

      case R.id.action_search:

        Toast.makeText(this, "action_search", Toast.LENGTH_LONG).show();
        break;

      case R.id.action_favorite:

        Toast.makeText(this, "action_favorite", Toast.LENGTH_LONG).show();

        break;

    }

    return true;
  }

}
