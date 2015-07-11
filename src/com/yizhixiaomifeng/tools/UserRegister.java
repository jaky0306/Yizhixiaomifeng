package com.yizhixiaomifeng.tools;

import com.yizhixiaomifeng.user.Login;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.Toast;

public class UserRegister extends AsyncTask<String, Integer, String>{
	private Context context;
	private Button button;
	public UserRegister(Context context , Button button){
		this.context=context;
		this.button=button;
	}
	@Override
	protected String doInBackground(String... params) {
		long id = Long.valueOf(params[0]);
		String phone = params[1];
		String password=params[2];
		return new ConnectWeb().registerUser(id, phone, password);
	}

	@Override
	protected void onPostExecute(String result) {
		if(!result.equals("error")){
			Toast.makeText(context, "注册成功...", Toast.LENGTH_LONG).show();
		}
		button.setText("注册");
		button.setEnabled(true);
		Intent intent = new Intent(context,Login.class);
		context.startActivity(intent);
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		button.setText("正在注册...");
		button.setEnabled(false);
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	

}
