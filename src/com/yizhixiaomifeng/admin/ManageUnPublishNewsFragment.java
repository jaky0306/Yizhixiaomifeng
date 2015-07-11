package com.yizhixiaomifeng.admin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.adapter.NewsViewAdapter;
import com.yizhixiaomifeng.admin.bean.News;
import com.yizhixiaomifeng.opensource.autoListview.AutoListView;
import com.yizhixiaomifeng.opensource.autoListview.AutoListView.OnLoadListener;
import com.yizhixiaomifeng.opensource.autoListview.AutoListView.OnRefreshListener;
import com.yizhixiaomifeng.tools.ConnectWeb;
import com.yizhixiaomifeng.tools.NewsManager;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ManageUnPublishNewsFragment extends Fragment implements OnRefreshListener,OnLoadListener{
	private View main;
	private AutoListView show_all_un_publish_news;
	private NewsViewAdapter adapter;
	private List<News> un_publish_list = new ArrayList<News>();
	private int start=0;
	private int status =0; //草稿箱的消息的转态都为0
	private Activity manageUnPublishActivity;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			List<News> result = (List<News>) msg.obj;
			switch (msg.what) {
			case AutoListView.REFRESH:
				show_all_un_publish_news.onRefreshComplete();
				un_publish_list.clear();
				un_publish_list.addAll(result);
				break;
			case AutoListView.LOAD:
				show_all_un_publish_news.onLoadComplete();
				un_publish_list.addAll(result);
				break;
			}
			show_all_un_publish_news.setResultSize(result.size());
			adapter.notifyDataSetChanged();
		};
	};
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        manageUnPublishActivity = getActivity();    //获取当前正在是用的Activity
        LayoutInflater inflater = manageUnPublishActivity.getLayoutInflater();
        main=inflater.inflate(R.layout.manage_un_publish_news, (ViewGroup)getActivity().findViewById(R.id.manage_news_viewpager), false);
        show_all_un_publish_news=(AutoListView)main.findViewById(R.id.show_un_publish_news_listview);
        adapter = new NewsViewAdapter(manageUnPublishActivity.getBaseContext(), un_publish_list);
        show_all_un_publish_news.setAdapter(adapter);
        show_all_un_publish_news.setOnRefreshListener(this);
        show_all_un_publish_news.setOnLoadListener(this);
		loadData(AutoListView.REFRESH);
		show_all_un_publish_news.setOnCreateContextMenuListener(new OnCreateContextMenuListener()
        {
            
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo)
            {
            	menu.setHeaderIcon(R.drawable.key_icon);
                menu.setHeaderTitle("操作");
                menu.add(0, 1, 0, "编辑");
                menu.add(0, 2, 0, "删除");
            }
        });
    }
	
	// 长按菜单响应函数 
    public boolean onContextItemSelected(MenuItem item)
    { 
    	AdapterView.AdapterContextMenuInfo menuInfo = 
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
    	News news=null;
        switch (item.getItemId()) { 
        case 1: //编辑操作
        	news = un_publish_list.get(menuInfo.position-1);	 //获取当前点击的news
        	Intent intent0 = new Intent(manageUnPublishActivity.getBaseContext(),EditNewsActivity.class);
        	intent0.putExtra("news", news);
        	startActivity(intent0);
//        	Log.e("aaaaaaaaaaaa", "aaaaaaaaaaaaaaaa");
            break; 
        case 2: //删除操作
        	//让后台删除数据
//        	Log.e("aaaaaaaaaaaa", "bbbbbbbbb");
        	news = un_publish_list.get(menuInfo.position-1);	 //获取当前点击的news
        	new NewsManager(manageUnPublishActivity.getBaseContext(), "delete").execute(news);
        	break; 
        } 

        return super.onContextItemSelected(item); 

    } 
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
		ViewGroup p = (ViewGroup) main.getParent(); 
        if (p != null) { 
            p.removeAllViewsInLayout(); 
        } 
		
		return main;
    }
	private void loadData(final int what) {
//		List<News> data = new ArrayList<News>();
//		News news = new News(1,"油价下跌","abdlanflnadflndl",Calendar.getInstance().getTimeInMillis(),0);
//		data.add(news);
//		Message msg = new Message();
//		msg.what = what;
//		msg.obj = data;
//		handler.sendMessage(msg);
//		
		//从服务器获取数据
		new LoadUnPublishNewsDataHelper(what).execute(start,status);

	}
	
	@Override
	public void onLoad() {
		start+=5;
		loadData(AutoListView.LOAD);
	}
	@Override
	public void onRefresh() {
		start=0;
		loadData(AutoListView.REFRESH);
	}
	@Override
    public void onStop()
    {
        super.onStop();
    }
	
	class LoadUnPublishNewsDataHelper extends AsyncTask<Integer, Integer, String>{
		private int what;
		public LoadUnPublishNewsDataHelper(int what){
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
					Message msg = new Message();
					msg.what = what;
					msg.obj = data;
					handler.sendMessage(msg);
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
