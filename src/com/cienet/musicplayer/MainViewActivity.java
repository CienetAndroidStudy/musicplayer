package com.cienet.musicplayer;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainViewActivity extends Activity {

	private View view1, view2, view3;
	private ViewPager viewPager;
	private PagerTabStrip pagerTabStrip;
	private List<View> viewList;
	private List<String> titleList;
	List<Song> songs ;  
	ListAdapter adapter = null;  
	ListView listView = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_pager);

        initView();

	}

	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		pagerTabStrip = (PagerTabStrip) findViewById(R.id.pagertab);
		pagerTabStrip.setTabIndicatorColor(getResources()
				.getColor(R.color.gold));
		pagerTabStrip.setDrawFullUnderline(false);
		pagerTabStrip
				.setBackgroundColor(getResources().getColor(R.color.azure));
		pagerTabStrip.setTextSpacing(50);

		view1 = findViewById(R.layout.layout1);
		view2 = findViewById(R.layout.layout2);
		view3 = findViewById(R.layout .music_list);

		LayoutInflater lf = getLayoutInflater();
		view1 = lf.inflate(R.layout.layout1, null);
		view2 = lf.inflate(R.layout.layout2, null);
		view3 = lf.inflate(R.layout.music_list, null);
		
		//绑定Layout里面的ListView
        listView = (ListView) (lf.inflate(R.layout.music_list, null)).findViewById(R.id.ListView01);
        //listView = (ListView) findViewById(R.id.ListView01);
        
        //显示ListView  
        initListAllSongs();  
        showByMyBaseAdapter();

		viewList = new ArrayList<View>();
		viewList.add(view1);
		viewList.add(view2);
		viewList.add(listView);

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
			public void destroyItem(ViewGroup container, int position,
					Object object) {
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
	            try { 
	                if(viewList.get(position).getParent()==null)
	                    ((ViewPager) container).addView(viewList.get(position), 0);  
	                else{
	                    //  很难理解新添加进来的view会自动绑定一个父类，由于一个儿子view不能与两个父类相关，所以得解绑
	                    //不这样做否则会产生 viewpager java.lang.IllegalStateException: The specified child already has a parent. You must call removeView() on the child's parent first.
	                    // 还有一种方法是viewPager.setOffscreenPageLimit(3); 这种方法不用判断parent 是不是已经存在，但多余的listview不能被destroy
	                    ((ViewGroup)viewList.get(position).getParent()).removeView(viewList.get(position));
	                    ((ViewPager) container).addView(viewList.get(position), 0); 
	                }
	            } catch (Exception e) {  
	                // TODO Auto-generated catch block  
	                e.printStackTrace();  
	            }  
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
	
	public void initListAllSongs(){
        songs = new ArrayList<Song>();
        for(int i=10;i<30;i++){
            songs.add(new Song("歌曲"+i,"专辑"+i,R.drawable.ic_action_favorite));
        }
    }
    
    public void showByMyBaseAdapter(){
        adapter = new SongListAdapter(this, songs);  
        listView.setAdapter(adapter);
    }

}
