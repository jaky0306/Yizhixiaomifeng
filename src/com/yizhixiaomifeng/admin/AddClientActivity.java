package com.yizhixiaomifeng.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
import com.baidu.platform.comapi.map.e;
import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.admin.bean.Client;
import com.yizhixiaomifeng.tools.ClientInfoSaver;

import android.R.fraction;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class AddClientActivity extends Activity implements OnGetGeoCoderResultListener{
	private GeoCoder mSearch = null;
	private BaiduMap mBaiduMap = null;
	private MapView mMapView = null;
	private Button commit_add_client=null;
	private EditText add_client_name;
	private EditText add_client_project;
	private EditText add_client_address_editText;
	private EditText add_client_start_time;
	private EditText add_client_end_time;
	private Button add_client_search_button;
	private LatLng clientLatLng=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_client);
		
		//地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();

		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		add_client_name=(EditText)findViewById(R.id.add_client_name);
		add_client_project=(EditText)findViewById(R.id.add_client_project);
		add_client_address_editText=(EditText)findViewById(R.id.add_client_address);
		add_client_start_time=(EditText)findViewById(R.id.add_client_start_time);
		add_client_end_time=(EditText)findViewById(R.id.add_client_end_time);
		add_client_search_button=(Button)findViewById(R.id.add_client_search);
		add_client_search_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Geo搜索
				mSearch.geocode(new GeoCodeOption()
					.city("")
					.address(add_client_address_editText.getText().toString()));
			}
		});
		
		final Calendar c = Calendar.getInstance();
        add_client_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(AddClientActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        add_client_start_time.setText(DateFormat.format("yyy-MM-dd", c));
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
        add_client_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(AddClientActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        add_client_end_time.setText(DateFormat.format("yyy-MM-dd", c));
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
		
		commit_add_client=(Button)findViewById(R.id.commit_add_client);
		commit_add_client.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Client client = new Client();
				String name = add_client_name.getText().toString().trim();
				String projectName = add_client_project.getText().toString().trim();
				String address = add_client_address_editText.getText().toString().trim();
				String startTime = add_client_start_time.getText().toString().trim();
				
				String endTime = add_client_end_time.getText().toString().trim();
				double longitude = clientLatLng.longitude;
				double latitude = clientLatLng.latitude;
				client.setName(name);
				client.setProjectName(projectName);
				client.setAddress(address);
				//获取时间戳
				SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd" );
				Date date1=null;
				Date date2=null;
				try {
					date1 = format.parse(startTime);
					date2 = format.parse(endTime);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				client.setStartTime(date1.getTime());
				client.setEndTime(date2.getTime());
				
				client.setLongitude(longitude);
				client.setLatitude(latitude);
				//保存客户信息到数据库
				new ClientInfoSaver(AddClientActivity.this, commit_add_client, "save").execute(client);
				AddClientActivity.this.finish();
			}
		});
		
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(AddClientActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
			return;
		}
		mBaiduMap.clear();
		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.address_icon)));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));
		
		clientLatLng = new LatLng(result.getLocation().latitude, result.getLocation().longitude);
		Toast.makeText(AddClientActivity.this, "请确认定位的位置是否正确...", Toast.LENGTH_LONG).show();
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
