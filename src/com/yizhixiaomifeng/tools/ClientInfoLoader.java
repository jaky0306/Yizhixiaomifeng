package com.yizhixiaomifeng.tools;

import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class ClientInfoLoader extends AsyncTask<String, Integer, String>{
	private Spinner spinner;
	private LinearLayout show_loadinfo_tip;
	public ClientInfoLoader(Spinner spinner,LinearLayout linearLayout){
		this.spinner=spinner;
		this.show_loadinfo_tip=linearLayout;
	}
	@Override
	protected String doInBackground(String... params) {
		
		/**
		 * 去后台获取数据
		 */
		String result = new ConnectWeb().getUserInfoByPhone(params[0]);
		return result;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(String result) {
		if(result!=null){
			
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