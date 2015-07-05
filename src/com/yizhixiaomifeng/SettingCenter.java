package com.yizhixiaomifeng;

import com.yizhixiaomifeng.tools.ActivityCloser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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
	}
	
}
