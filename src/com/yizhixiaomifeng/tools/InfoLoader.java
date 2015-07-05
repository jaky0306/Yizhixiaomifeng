package com.yizhixiaomifeng.tools;

import java.util.List;

import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InfoLoader extends AsyncTask<String, Integer, String>{
	private List<TextView> infoTextViews;
	private LinearLayout show_loadinfo_tip;
	public InfoLoader(List<TextView> textViews,LinearLayout linearLayout){
		this.infoTextViews=textViews;
		this.show_loadinfo_tip=linearLayout;
	}
	@Override
	protected String doInBackground(String... params) {
		show_loadinfo_tip.setVisibility(View.VISIBLE);
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
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}
	
}