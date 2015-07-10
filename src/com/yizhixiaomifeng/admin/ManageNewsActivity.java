package com.yizhixiaomifeng.admin;

import java.util.ArrayList;

import com.baidu.location.ad;
import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.tools.ActivityCloser;

import android.R.color;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ManageNewsActivity extends FragmentActivity{
	private ViewPager viewPager;
	private Fragment had_publish_news_Fragment;
	private Fragment un_publish_news_Fragment;
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private ArrayList<String>titles=new ArrayList<String>();
	private ArrayList<Integer>drawables = new ArrayList<Integer>();
	private PagerTabStrip pagerTabStrip;
	private Button add_news_button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_news);
		ActivityCloser.activities.add(this);
		viewPager=(ViewPager)findViewById(R.id.manage_news_viewpager);
		pagerTabStrip=(PagerTabStrip) findViewById(R.id.manage_news_viewpager_tab);
		//设置下划线的颜色
		pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.background_1)); 
		//设置背景的颜色
		pagerTabStrip.setBackgroundColor(getResources().getColor(R.color.background_4));
		
		had_publish_news_Fragment=new ManageHadPublishNewsFragment();
		un_publish_news_Fragment=new ManageUnPublishNewsFragment();
		fragments.add(had_publish_news_Fragment);
		fragments.add(un_publish_news_Fragment);
		titles.add("已发布");
		titles.add("草稿箱");
		drawables.add(R.drawable.delete_icon);
		drawables.add(R.drawable.edit_icon);
		viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
		add_news_button=(Button)findViewById(R.id.add_news_button);
		add_news_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ManageNewsActivity.this,EditNewsActivity.class);
				startActivity(intent);
			}
		});
	}
	
	
	public class MyViewPagerAdapter extends FragmentPagerAdapter{

		public MyViewPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return fragments.get(arg0);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			
			SpannableStringBuilder ssb = new SpannableStringBuilder("  "+titles.get(position)); // space added before text  
			Drawable myDrawable = getResources().getDrawable(drawables.get(position));  
			myDrawable.setBounds(0, 0, myDrawable.getIntrinsicWidth(),myDrawable.getIntrinsicHeight());  
			ImageSpan span = new ImageSpan(myDrawable,ImageSpan.ALIGN_BASELINE);  
			ForegroundColorSpan fcs = new ForegroundColorSpan(R.color.white);// 字体颜色设置为绿色  
			ssb.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置图标  
			ssb.setSpan(fcs, 1, ssb.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置字体颜色  
			ssb.setSpan(new RelativeSizeSpan(1.2f), 1, ssb.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
			return ssb; 
			
		}
		
	}
}
