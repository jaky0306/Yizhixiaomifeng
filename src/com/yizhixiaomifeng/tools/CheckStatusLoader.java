package com.yizhixiaomifeng.tools;

import org.json.JSONObject;

import com.yizhixiaomifeng.R.color;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

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
		if(result!="error"){
			try {
				JSONObject jsonObject = new JSONObject(result);
				String checkInStatus = jsonObject.getString("checkInStatus");
				String checkOutStatus = jsonObject.getString("checkOutStatus");
				if(!checkInStatus.equals("ok")){
					checkIn_button.setEnabled(false);
					checkIn_button.setBackgroundResource(context.getResources().getColor(color.gray));
				}else {
					checkIn_button.setEnabled(true);
					checkIn_button.setBackgroundResource(context.getResources().getColor(color.white));
				}
				if(!checkOutStatus.equals("ok")){
					checkOut_button.setEnabled(false);
					checkOut_button.setBackgroundResource(context.getResources().getColor(color.gray));
				}else {
					checkOut_button.setEnabled(true);
					checkOut_button.setBackgroundResource(context.getResources().getColor(color.white));
				}
				show_loadinfo_tip.setVisibility(View.GONE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
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