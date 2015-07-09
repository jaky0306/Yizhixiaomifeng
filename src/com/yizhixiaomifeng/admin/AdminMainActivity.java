package com.yizhixiaomifeng.admin;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.utils.h;
import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.config.ParameterConfig;
import com.yizhixiaomifeng.tools.ActivityCloser;
import com.yizhixiaomifeng.tools.HeadLoader;
import com.yizhixiaomifeng.tools.LocalStorage;
import com.yizhixiaomifeng.user.SettingCenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AdminMainActivity extends Activity{
	private RelativeLayout manage_attendance;
	private RelativeLayout manage_news;
	private RelativeLayout manage_staff;
	private RelativeLayout manage_client;
	private RelativeLayout admin_setting;
	private LinearLayout show_loadinfo_tip;
	private ImageView admin_main_head_img;
	private TextView admin_main_name;
	private String user ;
	private String type;
	private int exit=0;  //	用来记录click 的次数
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==0x111){
				LocalStorage ls = new LocalStorage(AdminMainActivity.this);
				admin_main_name.setText(ls.getString("name", "****"));
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());//初始化百度地图
		setContentView(R.layout.activity_admin_main);
		
		ActivityCloser.activities.add(this);
		
		manage_attendance=(RelativeLayout)findViewById(R.id.admin_main_manage_attendance);
		manage_news = (RelativeLayout)findViewById(R.id.admin_main_manage_news);
		manage_staff=(RelativeLayout)findViewById(R.id.admin_main_manage_staff);
		manage_client=(RelativeLayout)findViewById(R.id.admin_main_manage_client);
		admin_setting=(RelativeLayout)findViewById(R.id.admin_main_setting);
		manage_attendance.setOnClickListener(listener);
		manage_news.setOnClickListener(listener);
		manage_staff.setOnClickListener(listener);
		manage_client.setOnClickListener(listener);
		admin_setting.setOnClickListener(listener);
		
		LocalStorage ls = new LocalStorage(AdminMainActivity.this);
		user=ls.getString("username", "");
		type=ls.getString("type", "");
		
		initAdminInfo();
		
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.admin_main_manage_attendance:
				Intent intent1 = new Intent(AdminMainActivity.this,ManageNewsActivity.class);
				startActivity(intent1);
				break;
			case R.id.admin_main_manage_news:
				Intent intent2 = new Intent(AdminMainActivity.this,ManageNewsActivity.class);
				startActivity(intent2);
				break;	
			case R.id.admin_main_manage_staff:
				Intent intent3 = new Intent(AdminMainActivity.this,ManageNewsActivity.class);
				startActivity(intent3);
				break;
			case R.id.admin_main_manage_client:
				Intent intent4 = new Intent(AdminMainActivity.this,ManageClientActivity.class);
				startActivity(intent4);
				break;
			case R.id.admin_main_setting:
				Intent intent5 = new Intent(AdminMainActivity.this,SettingCenter.class);
				startActivity(intent5);
				break;
			default:
				break;
			}
		}
	};
	
	
	public void initAdminInfo(){
		/**
		 * 初始化头像
		 */
		show_loadinfo_tip=(LinearLayout)findViewById(R.id.show_loadinfo_tip_LinearLayout);
		admin_main_head_img = (ImageView)findViewById(R.id.admin_main_head_img);
		ImageView [] heads=new ImageView[]{admin_main_head_img};
		
		if(ParameterConfig.headChange||ParameterConfig.firstUse){ //如果头像改变了,或第一次使用，立刻更新头像
			Log.e("initadmin", ""+user+" "+type);
			new HeadLoader(heads,show_loadinfo_tip).execute(user,type);     //获取最新头像信息
			ParameterConfig.headChange=false; //头像更新完毕了，把标记设为false
		}
		/**
		 * 初始化信息
		 */
		admin_main_name=(TextView)findViewById(R.id.admin_main_name);
		
		Message msg = new Message();
		msg.what=0x111;
		handler.sendMessage(msg);
		
		ParameterConfig.firstUse=false; //用户信息加载完毕了，说明已经使用过了
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            exit++ ;
            if(exit==2)
            {
                exit = 0 ;
                ActivityCloser.exitAllActivities(this);
              
            }
            else
            {
                Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        exit = 0 ;
                    }
                }, 3000);
                return true ;
            }
            
        }
        return super.onKeyDown(keyCode, event);
    }
	@Override
	protected void onResume() {
		super.onResume();
	}
}
