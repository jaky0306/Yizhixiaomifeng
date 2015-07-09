package com.yizhixiaomifeng.admin;

import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.admin.bean.News;
import com.yizhixiaomifeng.config.ParameterConfig;
import com.yizhixiaomifeng.tools.ActivityCloser;
import com.yizhixiaomifeng.tools.NewsManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditNewsActivity extends Activity{
	private EditText edit_news_title;
	private EditText edit_news_content;
	private Button save_edit_news_button;
	private Button publish_edit_news_button;
	private News news=null;
	private boolean isSave=true;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==0x111){
				edit_news_title.setText(news.getTitle());
				edit_news_content.setText(news.getContent());
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_news);
		ActivityCloser.activities.add(this);
		news = (News) getIntent().getSerializableExtra("news");
		edit_news_title=(EditText)findViewById(R.id.edit_news_title);
		edit_news_content=(EditText)findViewById(R.id.edit_news_content);
		save_edit_news_button=(Button)findViewById(R.id.save_edit_news_button);
		publish_edit_news_button=(Button)findViewById(R.id.public_edit_news_button);
		if(news!=null){
			isSave=false;  //news不为空，说明这是在新增消息
			Message msg = new Message();
			msg.what=0x111;
			msg.obj=news;
			handler.sendMessage(msg);
		}else {
			isSave=true;
			news=new News();
		}
		save_edit_news_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/**
				 * 确定保存后 ，就保存消息到后台的草稿箱里
				 */
//				news.setStatus(ParameterConfig.un_publish_news_status);
				news.setTitle(edit_news_title.getText().toString().trim());
				news.setContent(edit_news_content.getText().toString().trim());
				if(isSave){
					new NewsManager(EditNewsActivity.this, "save").execute(news);
				}else{
					new NewsManager(EditNewsActivity.this, "update").execute(news);
				}
				
				EditNewsActivity.this.finish();
			}
		});
		publish_edit_news_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog alert= new AlertDialog.Builder(EditNewsActivity.this).create();
				alert.setIcon(R.drawable.publish_icon);
				alert.setTitle("确定发布?");
				alert.setButton(DialogInterface.BUTTON_NEGATIVE, "放弃",new AlertDialog.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
				alert.setButton(DialogInterface.BUTTON_POSITIVE,"确定",new AlertDialog.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(EditNewsActivity.this, "正在操作...", Toast.LENGTH_LONG).show();
						/**
						 * 确定发布后 ，就保存消息到后台的已发布消息里
						 */
						news.setTitle(edit_news_title.getText().toString().trim());
						news.setContent(edit_news_content.getText().toString().trim());
						news.setStatus(ParameterConfig.had_publish_news_status);
						if(isSave){
							new NewsManager(EditNewsActivity.this, "save").execute(news);
						}else {
							new NewsManager(EditNewsActivity.this, "update").execute(news);
						}
						EditNewsActivity.this.finish();
					}
					
				});
				alert.show();
			}
		});
	}
}
