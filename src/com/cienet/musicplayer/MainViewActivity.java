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
    // 设置竖屏（ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE--设定为横屏）
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    setContentView(R.layout.view_pager);

    initView();

  }

  private void initView() {
    viewPager = (ViewPager) findViewById(R.id.viewpager);
    pagerTabStrip = (PagerTabStrip) findViewById(R.id.pagertab);
    pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.gold));
    pagerTabStrip.setDrawFullUnderline(false);
    pagerTabStrip.setBackgroundColor(getResources().getColor(R.color.azure));
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
    titleList.add("text");
    titleList.add("alum");
    titleList.add("songs");


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
            Toast.makeText(view.getContext(), "你长按了" + position + "个Item", Toast.LENGTH_LONG)
                .show();
            // 返回值为true，则不触发setOnItemClickListener事件
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
    // getMenuInflater().inflate(R.menu.activity_view_pager_demo, menu);
    return true;
  }


  public void showBySongListAdapter() {
    musicListAdapter = new SongListAdapter(this, songs);
    ((SongListAdapter) musicListAdapter).initListAllSongs();
    musicListView.setAdapter(musicListAdapter);
  }

}
