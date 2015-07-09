package com.yizhixiaomifeng.admin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.adapter.NewsViewAdapter;
import com.yizhixiaomifeng.admin.bean.Client;
import com.yizhixiaomifeng.admin.bean.News;
import com.yizhixiaomifeng.config.ParameterConfig;
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
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ManageHadPublishNews extends Fragment implements OnRefreshListener,OnLoadListener{
	private View main;
	private AutoListView show_all_had_publish_news;
	private NewsViewAdapter adapter;
	private List<News> had_publish_list = new ArrayList<News>();
	private int start=0;
	private int status =1; //�ѷ�������Ϣ��ת̬��Ϊ1
	private Activity manageHadPublishNewsActivity;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			List<News> result = (List<News>) msg.obj;
			switch (msg.what) {
			case AutoListView.REFRESH:
				show_all_had_publish_news.onRefreshComplete();
				had_publish_list.clear();
				had_publish_list.addAll(result);
				break;
			case AutoListView.LOAD:
				show_all_had_publish_news.onLoadComplete();
				had_publish_list.addAll(result);
				break;
			}
			show_all_had_publish_news.setResultSize(result.size());
			adapter.notifyDataSetChanged();
		};
	};
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        manageHadPublishNewsActivity = getActivity();    //��ȡ��ǰ�������õ�Activity
        LayoutInflater inflater = manageHadPublishNewsActivity.getLayoutInflater();
        main=inflater.inflate(R.layout.manage_had_publish_news, (ViewGroup)getActivity().findViewById(R.id.manage_news_viewpager), false);
        show_all_had_publish_news=(AutoListView)main.findViewById(R.id.show_had_publish_news_listview);
        adapter = new NewsViewAdapter(manageHadPublishNewsActivity.getBaseContext(), had_publish_list);
        show_all_had_publish_news.setAdapter(adapter);
        show_all_had_publish_news.setOnRefreshListener(this);
        show_all_had_publish_news.setOnLoadListener(this);
		loadData(AutoListView.REFRESH);
		show_all_had_publish_news.setOnCreateContextMenuListener(new OnCreateContextMenuListener()
        {
            
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo)
            {
            	menu.setHeaderIcon(R.drawable.key_icon);
                menu.setHeaderTitle("����");
                menu.add(0, 0, 0, "��������");
            }
        });
		
    }
	// �����˵���Ӧ���� 
    public boolean onContextItemSelected(MenuItem item)
    { 
    	AdapterView.AdapterContextMenuInfo menuInfo = 
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
    	News news=null;
        switch (item.getItemId()) { 
        case 0: //������������
        	//���̸�����Ϣת̬
        	news = had_publish_list.get(menuInfo.position-1);	 //��ȡ��ǰ�����news
        	news.setStatus(ParameterConfig.un_publish_news_status);
        	new NewsManager(manageHadPublishNewsActivity, "update").execute(news);
//        	Toast.makeText(manageUnPublishActivity.getBaseContext(), "�༭"+news.getReleasedTime(), Toast.LENGTH_LONG).show();
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
//		News news = new News(1,"�ͼ��µ�","abdlanflnadflndl",Calendar.getInstance().getTimeInMillis(),1);
//		data.add(news);
//		Message msg = new Message();
//		msg.what = what;
//		msg.obj = data;
//		handler.sendMessage(msg);
		
		//�ӷ�������ȡ����
		new LoadHadPublishNewsDataHelper(what).execute(start,status);

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
			
			if(result!=null){
				List<News> data = new ArrayList<News>();
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
					// TODO: handle exception
					e.printStackTrace();
				}
				//[{"address":"�й��ƶ��Ϸ�����","endTime":1,"id":1,"laitude":1,"longitude":1,"name":"�������ѧԺ","projectName":"APP����","startTime":1},{"address":"����ŵ�ؿƼ����޹�˾","endTime":1,"id":2,"laitude":1,"longitude":1,"name":"�������ѧԺ","projectName":"��ҵ������Ա���ڹ���ϵͳ","startTime":1}]
				
			}
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
