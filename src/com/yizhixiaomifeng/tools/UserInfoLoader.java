package com.yizhixiaomifeng.tools;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.yizhixiaomifeng.domain.WorkerEntity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class UserInfoLoader extends AsyncTask<String, Integer, String>{
	private Context context;
	private Button button;
	private List<WorkerEntity> all_workers=null;
	public UserInfoLoader(Context context ,View view, List<WorkerEntity> all_workers){
		this.context=context;
		this.button=(Button)view;
		this.all_workers=all_workers;
	}
	@Override
	protected String doInBackground(String... params) {
		
		/**
		 * 去后台获取数据
		 */
		String result = new ConnectWeb().getAllUserInfo();
//		Log.e("bbbbbbbbbbbbb", result);
		return result;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(String result) {
		Log.e("aaaaaaaaaaaaa", result);
		if(result!=null){
			try {
				JSONArray jsonArray = new JSONArray(result);
				for(int i=0;i<jsonArray.length();i++){
					JSONObject object = jsonArray.getJSONObject(i);
					String name =object.getString("name");
					String phone = object.getString("cellPhone");
					WorkerEntity we = new WorkerEntity();
					we.setName(name);
					we.setCellPhone(phone);
					all_workers.add(we);
				}
				
		        
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		button.setText("选择员工");
		button.setEnabled(true);
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		button.setText("正在加载员工信息...");
		button.setEnabled(false);
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}
	
}