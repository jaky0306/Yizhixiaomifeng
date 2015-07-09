package com.yizhixiaomifeng;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.yizhixiaomifeng.SetHead.HeadUper;
import com.yizhixiaomifeng.config.ParameterConfig;
import com.yizhixiaomifeng.config.YzxmfConfig;
import com.yizhixiaomifeng.tools.ActivityCloser;
import com.yizhixiaomifeng.tools.AvosTool;
import com.yizhixiaomifeng.tools.HeadTool;
import com.yizhixiaomifeng.tools.LocalStorage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CheckIn extends Activity
{
	private Button commit_check_in_button;
	private TextView check_in_time;
	private String timeInfo;
	private TextView check_in_name;
	private Button get_position_button;
	private ProgressBar show_getting_position_proProgressBar;
	private TextView check_in_position;
	private TextView check_in_position_tip;
	private Spinner spinner;
	private ImageView check_in_scene;
	private String check_in_scene_name="checkinScene.jpg";
	private EditText check_in_leave_word;
	private List<String>data_list;
	
	private LocationClient mLocationClient=null;
	private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private LatLng mylLatLng=null;  //我当前的位置
    private LatLng goalLatLng=null;	//我的目标位置
	
    private double nowDistance = 0;  //当前位置距离目标地点的距离
    private double maxDistance = 0;  //管理员设置的到目标地点的最大偏差值
    
    
    private Handler handler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		if(msg.what==0x111){  //0x111用来修改当前的地址信息,如果刚获取到信息，说明定位已完成
    			get_position_button.setText("定位");
    			get_position_button.setEnabled(true);
    			show_getting_position_proProgressBar.setVisibility(View.INVISIBLE);
    			check_in_position.setText(msg.obj.toString());
    		}
    		if(msg.what==0x112){  //显示正在定位的进度条
    			get_position_button.setText("正在定位中...");
    			get_position_button.setEnabled(false);
    			show_getting_position_proProgressBar.setVisibility(View.VISIBLE);
    		}
    		if(msg.what==0x113){	//显示用户的
    			if(nowDistance>maxDistance){
    				check_in_position_tip.setTextColor(Color.RED);
        			check_in_position_tip.setText("距离目的地还大概有 "+(nowDistance-maxDistance)+" m ,加油...");
    			}else {
    				check_in_position_tip.setTextColor(Color.GREEN);
        			check_in_position_tip.setText("目标地点就在你周围...");
				}
    			
    		}
    		if(msg.what==0x114){//更新时间
    			check_in_time.setText(msg.obj.toString());
    		}
    		if(msg.what==0x115){  //用来显示基本信息
    			LocalStorage ls = new LocalStorage(CheckIn.this);
    			check_in_name.setText(ls.getString("name", "****"));
    		}
    	};
    };
    
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_in);
        
        ActivityCloser.activities.add(this);
        /**
         * 初始化组件
         */
        commit_check_in_button=(Button)findViewById(R.id.commit_check_in_button);
        check_in_time=(TextView)findViewById(R.id.check_in_time);
        check_in_name=(TextView)findViewById(R.id.check_in_name);
        get_position_button=(Button)findViewById(R.id.get_position_button);
        show_getting_position_proProgressBar=(ProgressBar)findViewById(R.id.show_getting_position_progressbar);
        check_in_position=(TextView)findViewById(R.id.check_in_position);
        check_in_position_tip=(TextView)findViewById(R.id.check_in_position_tip);
        spinner = (Spinner) findViewById(R.id.check_in_customer);
        check_in_scene=(ImageView)findViewById(R.id.check_in_scene);
        check_in_leave_word=(EditText)findViewById(R.id.check_in_leave_word);
        
        
        mLocationClient= new LocationClient(getApplicationContext());
	    LocationClientOption option = new LocationClientOption();
	    option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
	    option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
	    option.setScanSpan(50000);//设置发起定位请求的间隔时间为5000ms
	    option.setIsNeedAddress(true);//返回的定位结果包含地址信息
	    option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
	    mLocationClient.setLocOption(option);
	    //为位置客户端绑定事件
	    mLocationClient.registerLocationListener(new BDLocationListener() {
			
			@Override
			public void onReceiveLocation(BDLocation location) {
				
				if(location==null){
					Message msg = new Message();
					msg.what=0x111; //设置当前位置
					msg.obj="获取位置失败，请重新定位...";
					handler.sendMessage(msg);
					return;
				}
				
				//设置我当前位置的经纬度
				mylLatLng=new LatLng(location.getLatitude(), location.getLongitude());
				StringBuffer sb = new StringBuffer();
				if (location.getLocType() == BDLocation.TypeGpsLocation){
					sb.append(location.getSpeed());
					sb.append(location.getSatelliteNumber());
				} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
					sb.append(location.getAddrStr());
				} 
				/**
				 * 获取完经纬度后，更新组件状态
				 */
				Message msg = new Message();
				msg.what=0x111; //设置当前位置
				msg.obj=sb.toString();
				handler.sendMessage(msg);
			}
		});
	    
		mSearch = GeoCoder.newInstance();
		//为搜索绑定事件
		mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener(){

			@Override
			public void onGetGeoCodeResult(GeoCodeResult result) {
				if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
					Toast.makeText(CheckIn.this, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
					return;
				}
				goalLatLng=new LatLng(result.getLocation().latitude, result.getLocation().longitude);
			}

			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
				if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
					Toast.makeText(CheckIn.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
							.show();
					return;
				}
				Toast.makeText(CheckIn.this, result.getAddress(),
						Toast.LENGTH_LONG).show();
			}
			
		});
		
		check_in_scene.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intentFromCapture = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE); // 相机资源
				if (YzxmfConfig.hasSdcard()) {
					intentFromCapture.putExtra(
							MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(new File(Environment
									.getExternalStorageDirectory(),
									check_in_scene_name)));
				}
				startActivityForResult(intentFromCapture,1);
			}
		});
		
		
//		mSearch.geocode(new GeoCodeOption().city(
//				editCity.getText().toString()).address(
//				editGeoCodeKey.getText().toString()));
		
        
		//根据电话号码获取用户的姓名
		//获取所有客户的数据
		//获取管理员规定的最远距离值
        
        /**
         * 初始化Spinner的数据
         */
        data_list = new ArrayList<String>();
        data_list.add("请选择客户");
        data_list.add("中国移动南方基地");
        data_list.add("bbb");
        data_list.add("ccc");
        data_list.add("ddd");
        
        //适配器
        ArrayAdapter<String> arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
        
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long id) {
				if(position!=0)
					Toast.makeText(getApplicationContext(), data_list.get(position), Toast.LENGTH_LONG).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
        
        
        
        
        /**
         * 初始化基本信息
         */
        Message msg =new Message();
        msg.what=0x115;
        handler.sendMessage(msg);
        
        
        /**
         * 加载完数据后立刻获取当前位置，并定时更新时间
         */
        getMyLocation();
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(
        		new Runnable() {
					
					@Override
					public void run() {
						initCheckinTime();
					}
				}, 
				1, 
				1, 
				TimeUnit.SECONDS);
        
        
        //为提交按钮绑定事件
        commit_check_in_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				
				/**
				 * 把签到现场保存到云
				 */
				String username = new LocalStorage(CheckIn.this).getString("username", "");
				if(YzxmfConfig.isConnect(CheckIn.this)){
					Calendar c = Calendar.getInstance();
					int year = c.get(Calendar.YEAR);
					int month = c.get(Calendar.MONTH)+1;
					int date = c.get(Calendar.DATE);
					String time = ""+year+"-"+month+"-"+date;
					new CheckInHelper().execute(username,time);
				}
			}
		});
        
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) // 处理返回的结果
	{
		
		if(resultCode!=0)
		{
			switch (requestCode) {
			case 1:
				if (YzxmfConfig.hasSdcard()) {
					File tempFile = new File(
							Environment.getExternalStorageDirectory(),
							check_in_scene_name);
					startPhotoZoom(Uri.fromFile(tempFile));
				} else {
					Toast.makeText(CheckIn.this, "未找到存储卡，无法存储照片！",
							Toast.LENGTH_LONG).show();
				}
				break;
			case 2:
				if (data != null) {
					getImageToView(data);
				}
				break;
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);

	}
	
	//图片的截取
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 2);//RESULT_REQUEST_CODE
	}
	
	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			//转成圆形
			//Bitmap roundBitmap = HeadTool.toRoundBitmap(photo);
			@SuppressWarnings("deprecation")
			Drawable drawable = new BitmapDrawable(photo);
			saveScene(photo);
			check_in_scene.setImageDrawable(drawable);

		}
	}
	
	public void saveScene(Bitmap bitmap) {
		
		FileOutputStream fos = null;
		try {
			fos = openFileOutput(check_in_scene_name, 0);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		
	}
	
	public void initCheckinTime(){
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		int week=c.get(Calendar.DAY_OF_WEEK)-1;
		if(week<0) week=0;
		int year = c.get(Calendar.YEAR); 
		int month = c.get(Calendar.MONTH)+1; 
		int date = c.get(Calendar.DATE); 
		int hour = c.get(Calendar.HOUR_OF_DAY); 
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		timeInfo=year+"-"+month+"-"+date+"  "+(hour<10?"0"+hour:""+hour)+":"+(minute<10?"0"+minute:""+minute)+":"+second;
		Message msg = new Message();
		msg.what=0x114;
		msg.obj=timeInfo;
		handler.sendMessage(msg);
	}
	
    
	public void getMyLocation(){
		/**
		 * 准备获取当前位置信息
		 */
		Message msg = new Message();
		msg.what=0x112;
		handler.sendMessage(msg);
		mLocationClient.start();  //开启位置客户端
	}
	
	public void getDistanceToGoal(){
	  //计算p1、p2两点之间的直线距离，单位：米  
		if(mylLatLng!=null&&goalLatLng!=null){
			nowDistance = DistanceUtil.getDistance(mylLatLng, goalLatLng);
			/**
			 * 获取最新距离后，给用户一个提示
			 */
			Message msg = new Message();
			msg.what=0x113;
			handler.sendMessage(msg);
		}else {
			return ;
		}
	    
	}
	
	
	
	
	/**
	 * 提交助手
	 * @author Jaky
	 *
	 */
	class CheckInHelper extends AsyncTask<String, Integer, Void>{
		@Override
		protected Void doInBackground(String... params) {
			/**
			 * 让滚动条显示
			 */
			Message msg = new Message();
			msg.what=0x111;
			handler.sendMessage(msg);
			new AvosTool().saveHead(params[0]);  //把头像保存到LeanCloud
			return null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Void result) {
			/**
			 * 让滚动条隐藏
			 */
			Message msg = new Message();
			msg.what=0x112;
			handler.sendMessage(msg);
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
}
