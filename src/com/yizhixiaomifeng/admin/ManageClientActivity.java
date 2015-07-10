package com.yizhixiaomifeng.admin;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.adapter.ShowClientListViewAdapter;
import com.yizhixiaomifeng.admin.bean.Client;
import com.yizhixiaomifeng.admin.bean.News;
import com.yizhixiaomifeng.config.ParameterConfig;
import com.yizhixiaomifeng.opensource.autoListview.AutoListView;
import com.yizhixiaomifeng.opensource.autoListview.AutoListView.OnLoadListener;
import com.yizhixiaomifeng.opensource.autoListview.AutoListView.OnRefreshListener;
import com.yizhixiaomifeng.tools.ActivityCloser;
import com.yizhixiaomifeng.tools.ConnectWeb;
import com.yizhixiaomifeng.tools.NewsManager;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * @author Jaky
 *
 */
public class ManageClientActivity extends Activity implements OnRefreshListener,
		OnLoadListener {
	private Button add_client_button;
	private AutoListView show_all_client;
	private ShowClientListViewAdapter adapter;
	private List<Client> all_clients_list = new ArrayList<Client>();
	private int start=0;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			List<Client> result = (List<Client>) msg.obj;
			switch (msg.what) {
			case AutoListView.REFRESH:
				show_all_client.onRefreshComplete();
				all_clients_list.clear();
				all_clients_list.addAll(result);
				break;
			case AutoListView.LOAD:
				show_all_client.onLoadComplete();
				all_clients_list.addAll(result);
				break;
			}
			show_all_client.setResultSize(result.size());
			adapter.notifyDataSetChanged();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_client);
		ActivityCloser.activities.add(this);
		
		add_client_button=(Button)findViewById(R.id.add_client_button);
		
		show_all_client = (AutoListView) findViewById(R.id.show_client_listview);
		adapter = new ShowClientListViewAdapter(this, all_clients_list);
		show_all_client.setAdapter(adapter);
		show_all_client.setOnRefreshListener(this);
		show_all_client.setOnLoadListener(this);
		initData();
		show_all_client.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
				Toast.makeText(getApplicationContext(), ""+position, Toast.LENGTH_LONG).show();
				
			}
		});
		
		add_client_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ManageClientActivity.this,AddClientActivity.class);
				startActivity(intent);
			}
		});
	}

	
	private void initData() {
		loadData(AutoListView.REFRESH);
	}

	private void loadData(final int what) {
//		List<Client> data = new ArrayList<Client>();
//		Client client = new Client(1, "123", "123", "123", 12, 12, 12, 12);
//		data.add(client);
//		Message msg = new Message();
//		msg.what = what;
//		msg.obj = data;
//		handler.sendMessage(msg);
		
		//从服务器获取数据
		new LoadClientDataHelper(what).execute(start);

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

	class LoadClientDataHelper extends AsyncTask<Integer, Integer, String>{
		private int what;
		public LoadClientDataHelper(int what){
			this.what=what;
		}
		@Override
		protected String doInBackground(Integer... params) {
			String result = new ConnectWeb().getClientData(params[0]);
			return result;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			
			if(result!=null){
				List<Client> data = new ArrayList<Client>();
				try {
					JSONArray jsonArray = new JSONArray(result);
					for(int i=0;i<jsonArray.length();i++){
						JSONObject object = jsonArray.getJSONObject(i);
						long id = object.getLong("id");
						String name = object.getString("name");
						String projectName = object.getString("projectName");
						String address = object.getString("address");
						double longitude = object.getDouble("longitude");
						double latitude = object.getDouble("latitude");
						long startTime = object.getLong("startTime");
						long endTime = object.getLong("endTime");
						Client client = new Client(id, name, projectName, address, longitude, latitude, startTime, endTime);
						data.add(client);
					}
					Message msg = new Message();
					msg.what = what;
					msg.obj = data;
					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				//[{"address":"中国移动南方基地","endTime":1,"id":1,"laitude":1,"longitude":1,"name":"华软软件学院","projectName":"APP开发","startTime":1},{"address":"广州诺特科技有限公司","endTime":1,"id":2,"laitude":1,"longitude":1,"name":"华软软件学院","projectName":"企业外勤人员考勤管理系统","startTime":1}]
				
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
