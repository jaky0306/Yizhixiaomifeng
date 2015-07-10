package com.yizhixiaomifeng.tools;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.a.a.a.c;
import com.yizhixiaomifeng.admin.bean.Client;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class ClientInfoLoader extends AsyncTask<String, Integer, String>{
	private Context context;
	private Spinner spinner;
	private LinearLayout show_loadinfo_tip;
	private List<Client> all_Clients=null;
	public ClientInfoLoader(Context context,Spinner spinner,LinearLayout linearLayout, List<Client> all_Clients){
		this.context=context;
		this.spinner=spinner;
		this.show_loadinfo_tip=linearLayout;
		this.all_Clients=all_Clients;
	}
	@Override
	protected String doInBackground(String... params) {
		
		/**
		 * 去后台获取数据
		 */
		String phone = params[0];
		String result = new ConnectWeb().getAllClientDataByPhone(phone);
		return result;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(String result) {
		Log.e("aaaaaaaaaaaaa", "result");
		if(result!=null){
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
					all_Clients.add(client);
				}
				List<String> client_names = new ArrayList<String>();
				client_names.add("请选择客户");
				for(Client client : all_Clients){
					client_names.add(client.getName());
				}
				if(spinner!=null){
					//适配器
			        ArrayAdapter<String> arr_adapter= new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, client_names);
			        //设置样式
			        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			        //加载适配器
			        spinner.setAdapter(arr_adapter);
				}
				
		        
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else {
			List<String> client_names = new ArrayList<String>();
			client_names.add("没有获取到数据...");
			if(spinner!=null){
				ArrayAdapter<String> arr_adapter= new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, client_names);
		        //设置样式
		        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		        //加载适配器
		        spinner.setAdapter(arr_adapter);
			}
	        
		}
		show_loadinfo_tip.setVisibility(View.GONE);
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		show_loadinfo_tip.setVisibility(View.VISIBLE);
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}
	
}