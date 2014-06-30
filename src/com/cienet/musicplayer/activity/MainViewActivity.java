package com.cienet.musicplayer.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cienet.musicplayer.R;
import com.cienet.musicplayer.fragment.SongListFragment;

/**
 * 主界面
 * 
 * @author chaochen
 * 
 */
public class MainViewActivity extends FragmentActivity {

  private ViewPager viewPager;
  private PagerTabStrip pagerTabStrip;
  private List<String> titleList;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // 设置竖屏
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    setContentView(R.layout.view_pager);
    initView();

  }

  private void initView() {
    viewPager = (ViewPager) findViewById(R.id.viewpager);
    viewPager.setAdapter(new PlayerPageAdapter(getSupportFragmentManager()));

    pagerTabStrip = (PagerTabStrip) findViewById(R.id.pagertab);
    pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.powderblue));

    pagerTabStrip.setTextColor(getResources().getColor(R.color.powderblue));

    pagerTabStrip.setDrawFullUnderline(false);
    pagerTabStrip.setBackgroundColor(getResources().getColor(R.color.black));
    pagerTabStrip.setTextSpacing(50);

    titleList = new ArrayList<String>();
    titleList.add("TEXT");
    titleList.add("ALBUMS");
    titleList.add("SONGS");


    RelativeLayout bottomlayout = (RelativeLayout) findViewById(R.id.bottom_bar);
    bottomlayout.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        Toast.makeText(v.getContext(), "你点击了底部菜单栏", Toast.LENGTH_LONG).show();

      }

    });

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.actionbar_top, menu);
    return true;
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

  class PlayerPageAdapter extends FragmentPagerAdapter {

    private ArrayList<android.support.v4.app.Fragment> list =
        new ArrayList<android.support.v4.app.Fragment>();

    public PlayerPageAdapter(android.support.v4.app.FragmentManager fm) {
      super(fm);

      list.add(new SongListFragment());
      list.add(new SongListFragment());
      list.add(new SongListFragment());
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
      return list.get(position);
    }

    @Override
    public int getCount() {
      return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return titleList.get(position);
    }

  }

}
