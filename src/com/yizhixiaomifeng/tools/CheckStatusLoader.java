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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class CheckStatusLoader extends AsyncTask<String, Integer, String>{
	private Context context;
	private LinearLayout show_loadinfo_tip;
	private Button checkIn_button;
	private Button checkOut_button;
	public CheckStatusLoader(Context context,LinearLayout linearLayout,View checkIn,View checkOut){
		this.context=context;
		this.show_loadinfo_tip=linearLayout;
		this.checkIn_button=(Button)checkIn;
		this.checkOut_button=(Button)checkOut;
	}
	@Override
	protected String doInBackground(String... params) {
		
		/**
		 * 去后台获取数据
		 */
		String user = params[0];
		String clientId=params[1];
		String result = new ConnectWeb().getCheckStatusByUserAndClient(user, clientId);
		return result;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(String result) {
		

		
		
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