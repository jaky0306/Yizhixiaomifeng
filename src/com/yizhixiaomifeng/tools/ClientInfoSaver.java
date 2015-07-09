package com.yizhixiaomifeng.tools;

import com.yizhixiaomifeng.admin.ManageClientActivity;
import com.yizhixiaomifeng.admin.bean.Client;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ClientInfoSaver extends AsyncTask<Client, Integer, String>{
	private Context context;
	private Button button;
	private String type;
	public ClientInfoSaver(Context context,View view,String type){
		this.context=context;
		this.button=(Button) view;
		this.type=type;
	}
	@Override
	protected String doInBackground(Client... params) {
		/**
		 * 去后台保存数据
		 */
		String result=null;
		if(type.equals("save")){
			result = new ConnectWeb().saveClient(params[0]);
		}
		if(type.equals("update")){
			result = new ConnectWeb().updateClient(params[0]);
		}
		
		return result;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(String result) {
		if(!result.equals("error")){
			button.setText("保存");
			button.setEnabled(true);
			Toast.makeText(context, "操作成功...", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(context,ManageClientActivity.class);
			context.startActivity(intent);
		}else {
			button.setText("保存");
			button.setEnabled(true);
			Toast.makeText(context, "操作失败...", Toast.LENGTH_LONG).show();
		}
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		button.setText("正在操作...");
		button.setEnabled(false);
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}
	
}