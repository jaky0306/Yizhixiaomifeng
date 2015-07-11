package com.yizhixiaomifeng.admin;

import java.sql.Date;
import java.text.SimpleDateFormat;

import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.admin.bean.News;
import com.yizhixiaomifeng.tools.ActivityCloser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowNewsActivity extends Activity{
	TextView show_news_time ;
	TextView show_news_title;
	TextView show_news_content;
	private ImageView show_news_back;
	News news;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			show_news_time.setText(sdf.format(new java.util.Date(news.getReleasedTime())));
			show_news_title.setText(news.getTitle());
			show_news_content.setText(news.getContent());
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_news);
		ActivityCloser.activities.add(this);
		show_news_time=(TextView)findViewById(R.id.show_news_time);
		show_news_title=(TextView)findViewById(R.id.show_news_title);
		show_news_content=(TextView)findViewById(R.id.show_news_content);
		news=(News) getIntent().getSerializableExtra("news");
		if(news!=null){
			Message message = new Message();
			handler.sendMessage(message);
		}
		show_news_back=(ImageView)findViewById(R.id.show_news_back);
		show_news_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShowNewsActivity.this,ShowAllPublishNewsToUser.class);
				startActivity(intent);
				ShowNewsActivity.this.finish();
			}
		});
	}
}
