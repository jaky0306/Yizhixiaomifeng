package com.yizhixiaomifeng.tools;

import com.yizhixiaomifeng.admin.ManageNewsActivity;
import com.yizhixiaomifeng.admin.bean.News;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
/**
 * 用来删除，或更新消息状态
 * @author Jaky
 *
 */
public class NewsManager extends AsyncTask<News, Integer, String>{
	private String type;
	private Context context;
	public NewsManager(Context context,String type){
		this.context=context;
		this.type=type;
	}
	@Override
	protected String doInBackground(News... params) {
		//Toast.makeText(context, "正在操作...", Toast.LENGTH_LONG).show();
		String result=null;
		/**
		 * 去后台保存数据
		 */
		if(type.equals("save")){
			result = new ConnectWeb().saveNews(params[0]);
		}
		if(type.equals("delete")){
			
			result = new ConnectWeb().deleteNews(params[0]);
		}
		if(type.equals("update")){
			
			result=new ConnectWeb().updateNews(params[0]);
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
			Toast.makeText(context, "操作成功...", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(context,ManageNewsActivity.class);
			context.startActivity(intent);
		}else {
			Toast.makeText(context, "操作失败...", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(context,ManageNewsActivity.class);
			context.startActivity(intent);
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