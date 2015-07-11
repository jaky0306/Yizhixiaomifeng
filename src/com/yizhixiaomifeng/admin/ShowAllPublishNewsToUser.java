package com.yizhixiaomifeng.admin;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.location.f;
import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.adapter.NewsViewAdapter;
import com.yizhixiaomifeng.admin.bean.News;
import com.yizhixiaomifeng.opensource.autoListview.AutoListView;
import com.yizhixiaomifeng.opensource.autoListview.AutoListView.OnLoadListener;
import com.yizhixiaomifeng.opensource.autoListview.AutoListView.OnRefreshListener;
import com.yizhixiaomifeng.tools.ActivityCloser;
import com.yizhixiaomifeng.tools.ConnectWeb;
import com.yizhixiaomifeng.user.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 
 * @author Jaky
 *
 */
public class ShowAllPublishNewsToUser extends Activity implements OnRefreshListener,
		OnLoadListener {

	private AutoListView show_all_news_to_user;
	private NewsViewAdapter adapter;
	private List<News> had_publish_list = new ArrayList<News>();
	private int start=0;
	private int status =1; //已发布的消息的转态都为1
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			List<News> result = (List<News>) msg.obj;
			switch (msg.what) {
			case AutoListView.REFRESH:
				show_all_news_to_user.onRefreshComplete();
				had_publish_list.clear();
				had_publish_list.addAll(result);
				break;
			case AutoListView.LOAD:
				show_all_news_to_user.onLoadComplete();
				had_publish_list.addAll(result);
				break;
			}
			show_all_news_to_user.setResultSize(result.size());
			adapter.notifyDataSetChanged();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_all_published_news_to_user);
		ActivityCloser.activities.add(this);
		show_all_news_to_user = (AutoListView) findViewById(R.id.show_all_published_news_to_user_Listview);
		adapter = new NewsViewAdapter(this, had_publish_list);
		show_all_news_to_user.setAdapter(adapter);
		show_all_news_to_user.setOnRefreshListener(this);
		show_all_news_to_user.setOnLoadListener(this);
		initData();
		show_all_news_to_user.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
//				Toast.makeText(getApplicationContext(), ""+position, Toast.LENGTH_LONG).show();
				Intent intent = new Intent(ShowAllPublishNewsToUser.this,ShowNewsActivity.class);
				intent.putExtra("news",had_publish_list.get(position-1));
				startActivity(intent);
				
			}
		});
		
		ImageView show_all_published_news_to_user_back=(ImageView)findViewById(R.id.show_all_published_news_to_user_back);
		show_all_published_news_to_user_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShowAllPublishNewsToUser.this,MainActivity.class);
				startActivity(intent);
				ShowAllPublishNewsToUser.this.finish();
			}
		});
	}

	private void initData() {
		loadData(AutoListView.REFRESH);
	}

	private void loadData(final int what) {
		
		new LoadHadPublishNewsDataHelper(what).execute(start,status);
	}

	@Override
	public void onRefresh() {
		start=0;
		loadData(AutoListView.REFRESH);
	}

	@Override
	public void onLoad() {
		start+=5;
		loadData(AutoListView.LOAD);
	}
	class LoadHadPublishNewsDataHelper extends AsyncTask<Integer, Integer, String>{
		private int what;
		public LoadHadPublishNewsDataHelper(int what){
			this.what=what;
		}
		@Override
		protected String doInBackground(Integer... params) {
			String result = new ConnectWeb().getNewsData(params[0],params[1]);
			return result;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			List<News> data = new ArrayList<News>();
			if(!result.equals("error")){
				try {
					JSONArray jsonArray = new JSONArray(result);
					for(int i=0;i<jsonArray.length();i++){
						JSONObject object = jsonArray.getJSONObject(i);
						long id = object.getLong("id");
						String title = object.getString("title");
						String content = object.getString("content");
						long time = object.getLong("releasedTime");
						News news = new News(id, title, content,time,status);
						data.add(news);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Message msg = new Message();
			msg.what = what;
			msg.obj = data;
			handler.sendMessage(msg);
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
	}
}
