package com.yizhixiaomifeng.admin;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.admin.bean.Client;
import com.yizhixiaomifeng.tools.ActivityCloser;
import com.yizhixiaomifeng.tools.ClientInfoSaver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class EditClientActivity extends Activity implements OnGetGeoCoderResultListener{
	private GeoCoder mSearch = null;
	private BaiduMap mBaiduMap = null;
	private MapView mMapView = null;
	private LatLng clientLatLng=null;
	private Button commit_edit_button;
	private Button arrange_staff_button;
	private EditText edit_client_name;
	private EditText edit_client_project;
	private EditText edit_client_address;
	private Button edit_client_search;
	private Handler handler  = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==0x111){
				Client client = (Client) msg.obj;
				edit_client_name.setText(client.getName());
				edit_client_project.setText(client.getProjectName());
				edit_client_address.setText(client.getAddress());
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_client);
		ActivityCloser.activities.add(this);
		final Client client = (Client) getIntent().getSerializableExtra("client");
		commit_edit_button=(Button)findViewById(R.id.commit_edit_button);
		arrange_staff_button=(Button)findViewById(R.id.arrange_staff_button);
		edit_client_name=(EditText)findViewById(R.id.edit_client_name);
		edit_client_project=(EditText)findViewById(R.id.edit_client_project);
		edit_client_address=(EditText)findViewById(R.id.edit_client_address);
		edit_client_search=(Button)findViewById(R.id.edit_client_search);
		/**
		 * 初始化数据
		 */
		Message msg = new Message();
		msg.what=0x111;
		msg.obj=client;
		handler.sendMessage(msg);
		
		//地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		edit_client_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Geo搜索
				mSearch.geocode(new GeoCodeOption()
					.city("")
					.address(edit_client_address.getText().toString()));
			}
		});
		
		arrange_staff_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		arrange_staff_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EditClientActivity.this,ArrangeStaffForClientActivity.class);
				intent.putExtra("client", client);
				startActivity(intent);
			}
		});
		
		commit_edit_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String newName = edit_client_name.getText().toString().trim();
				String newProjectName  = edit_client_project.getText().toString().trim();
				String newAddress = edit_client_address.getText().toString().trim();
				client.setName(newName);
				client.setProjectName(newProjectName);
				client.setAddress(newAddress);
				//保存数据到后台
				new ClientInfoSaver(EditClientActivity.this, commit_edit_button,"update").execute(client);
			}
		});
		
		ImageView edit_client_back=(ImageView)findViewById(R.id.edit_client_back);
		edit_client_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditClientActivity.this.finish();
			}
		});
		
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(EditClientActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
			return;
		}
		mBaiduMap.clear();
		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.address_icon)));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));
		
		clientLatLng = new LatLng(result.getLocation().latitude, result.getLocation().longitude);
		Toast.makeText(EditClientActivity.this, "请确认定位的位置是否正确...", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		
	}
	
	@Override
	protected void onPause() {
		if(mMapView!=null){
			mMapView.onPause();
		}
		
		super.onPause();
	}

	@Override
	protected void onResume() {
		if(mMapView!=null){
			mMapView.onResume();
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if(mMapView!=null){
			mMapView.onDestroy();
		}
		if(mSearch!=null){
			mSearch.destroy();
		}
		
		super.onDestroy();
	}
}
