package com.yizhixiaomifeng.admin;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.avos.avoscloud.LogUtil.log;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.admin.bean.Client;
import com.yizhixiaomifeng.domain.WorkerEntity;
import com.yizhixiaomifeng.tools.ClientInfoLoader;
import com.yizhixiaomifeng.tools.ClientInfoSaver;
import com.yizhixiaomifeng.tools.ConnectWeb;
import com.yizhixiaomifeng.tools.LocalStorage;
import com.yizhixiaomifeng.tools.UserInfoLoader;
import com.yizhixiaomifeng.user.CheckIn;
import com.yizhixiaomifeng.user.Login;
import com.yizhixiaomifeng.user.MainActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ArrangeStaffForClientActivity extends Activity{
	private Button commit_arrange_button;
	private Button select_staff_for_client_button;
	private TextView arrange_client_name;
	private TextView arrange_client_content;
	private TextView arrange_client_address;
	private List<WorkerEntity>all_workers=new ArrayList<WorkerEntity>();
	private String[] staffItems;
	private boolean[] staffItemStatus;
	private ListView showAllStaffListview;
	private List<String>staffPhoneforClient=new ArrayList<String>();
	private TextView show_be_arranged_staff_textview;
	private boolean firstTimeClickSelectStaff=true;
	private Client client;
	private Handler handler  = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==0x111){
				Client client = (Client) msg.obj;
				arrange_client_name.setText(client.getName());
				arrange_client_content.setText(client.getProjectName());
				arrange_client_address.setText(client.getAddress());
			}
			if(msg.what==0x112){
				show_be_arranged_staff_textview.setText(msg.obj.toString());
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.arrange_client);
		client= (Client) getIntent().getSerializableExtra("client");
		commit_arrange_button=(Button)findViewById(R.id.commit_arrange_button);
		select_staff_for_client_button=(Button)findViewById(R.id.select_staff_for_client_button);
		arrange_client_name=(TextView)findViewById(R.id.arrange_client_name);
		arrange_client_content=(TextView)findViewById(R.id.arrange_client_content);
		arrange_client_address=(TextView)findViewById(R.id.arrange_client_address);
		show_be_arranged_staff_textview=(TextView)findViewById(R.id.show_be_arranged_staff_textview);
		/**
		 * 初始化数据
		 */
		Message msg = new Message();
		msg.what=0x111;
		msg.obj=client;
		handler.sendMessage(msg);
		//到后台加载员工信息
		new UserInfoLoader(ArrangeStaffForClientActivity.this, select_staff_for_client_button, all_workers).execute("");
		
		
		
		select_staff_for_client_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				if(firstTimeClickSelectStaff)
				{
					int index=0;
					staffItems=new String[all_workers.size()];
					staffItemStatus=new boolean[all_workers.size()];
					for(WorkerEntity we:all_workers){
						staffItems[index]=we.getName()+"("+we.getCellPhone()+")";
						staffItemStatus[index]=false;
						index++;
					}
				}
				
				AlertDialog dialog = new AlertDialog.Builder(ArrangeStaffForClientActivity.this)
				.setTitle("选择员工")
				.setMultiChoiceItems(staffItems,staffItemStatus,new DialogInterface.OnMultiChoiceClickListener(){
					public void onClick(DialogInterface dialog,int whichButton, boolean isChecked){
						//点击某个区域
					}
				})
				.setPositiveButton("确定",new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog,int whichButton)
					{
						StringBuffer sb = new StringBuffer("安排的员工的员工：");
						staffPhoneforClient.clear();
						for (int i = 0; i < staffItems.length; i++)
						{
							
							if (showAllStaffListview.getCheckedItemPositions().get(i))
							{
//								item格式：黄智杰(123456789);
								String item =(String) showAllStaffListview.getAdapter().getItem(i);
								sb.append(item+"  ");
								String phone =item.substring(item.indexOf("(")+1, item.indexOf(")"));
								staffPhoneforClient.add(phone);
								staffItemStatus[i]=true;//把这个员工标记为已选定
							}else
							{
								showAllStaffListview.getCheckedItemPositions().get(i,false);
							}
							
						}
						Message msg = new Message() ;
						msg.what=0x112;
						msg.obj=sb.toString();
						handler.sendMessage(msg);
						dialog.dismiss();
					}
				}).setNegativeButton("取消", null).create();
				
				firstTimeClickSelectStaff=false;
				showAllStaffListview = dialog.getListView();
				dialog.show();
			}
			
				
		});
		
		commit_arrange_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(staffPhoneforClient.size()==0){
					Toast.makeText(ArrangeStaffForClientActivity.this, "请选择员工...", Toast.LENGTH_LONG).show();
					return ;
				}
				//保存数据到后台
				new ArrangementSaver().execute("");
			}
		});
		
		
		
	}

	class ArrangementSaver extends AsyncTask<String, Integer, String>{
    	@Override
    	protected String doInBackground(String... params) {
    		JSONArray array = new JSONArray();
    		try 
    		{
    			for(int i=0;i<staffPhoneforClient.size();i++){
    				JSONObject jo = new JSONObject();
    				jo.put("clientId", client.getId());
    				jo.put("phone", staffPhoneforClient.get(i));
    				array.put(jo);
    			}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		String result = new ConnectWeb().saveArrangement(array.toString());
    		return result;
    	}

    	@Override
    	protected void onCancelled() {
    		super.onCancelled();
    	}

    	@Override
    	protected void onPostExecute(String result){
    		if(result.equals("ok")){
    			Toast.makeText(ArrangeStaffForClientActivity.this, "安排成功...", Toast.LENGTH_LONG).show();
    		}else
    		{
    			Toast.makeText(ArrangeStaffForClientActivity.this, "安排失败...", Toast.LENGTH_LONG).show();
    		}
    		//安排一次后，把内容初始化
    		firstTimeClickSelectStaff=true;
    		commit_arrange_button.setText("安排");
    		commit_arrange_button.setEnabled(true);
    	}

    	@Override
    	protected void onPreExecute() {
    		commit_arrange_button.setText("正在安排...");
    		commit_arrange_button.setEnabled(false);
    		super.onPreExecute();
    	}

    	@Override
    	protected void onProgressUpdate(Integer... values) {
    		super.onProgressUpdate(values);
    	}
    	
    }
	
}
