package com.yizhixiaomifeng;

import com.yizhixiaomifeng.tools.ActivityCloser;
import com.yizhixiaomifeng.tools.LocalStorage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class SettingCenter extends Activity{
	private Button setHeadButton;
	private Button signInButton;
	private Button signOutButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_center);
		
		ActivityCloser.activities.add(this);
		
		setHeadButton=(Button)findViewById(R.id.setting_center_header);
		signInButton=(Button)findViewById(R.id.setting_center_signin);
		signOutButton=(Button)findViewById(R.id.setting_center_signout);
		setHeadButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingCenter.this,SetHead.class);
				startActivity(intent);
			}
		});
		signInButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!new LocalStorage(SettingCenter.this).getString("username", "").equals("")){
					Toast.makeText(SettingCenter.this, "���Ѿ���¼...", Toast.LENGTH_LONG).show();
					return;
				}
				Intent intent = new Intent(SettingCenter.this,Login.class);
				startActivity(intent);
				SettingCenter.this.finish();
			}
		});
		signOutButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LocalStorage ls = new LocalStorage(SettingCenter.this);
				ls.putString("username", "");
    			ls.putString("type", "");
    			ls.putString("name", "****");
    			ls.putString("duty", "****");
    			ls.putString("department", "****");
				ls.commitEditor();
				Toast.makeText(getApplicationContext(), "���˳���¼...", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(SettingCenter.this,MainActivity.class);
				startActivity(intent);
				SettingCenter.this.finish();
			}
		});
	}
	
}
