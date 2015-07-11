package com.yizhixiaomifeng.user;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.admin.bean.Client;
import com.yizhixiaomifeng.config.YzxmfConfig;
import com.yizhixiaomifeng.tools.ActivityCloser;
import com.yizhixiaomifeng.tools.AvosTool;
import com.yizhixiaomifeng.tools.CatchVoiceTool;
import com.yizhixiaomifeng.tools.ClientInfoLoader;
import com.yizhixiaomifeng.tools.ConnectWeb;
import com.yizhixiaomifeng.tools.LocalStorage;
import com.yizhixiaomifeng.tools.ShowVoiceTool;
import com.yizhixiaomifeng.user.SetHead.HeadUper;

import android.R.bool;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class CheckOut extends Activity
{
	private Button commit_check_out_button;
	private TextView check_out_time;
	private String timeInfo;
	private TextView check_out_name;
	private Button get_position_button;
	private ProgressBar show_getting_position_proProgressBar;
	private TextView check_out_position;
	private TextView check_out_position_tip;
	private TextView check_out_client;
	private Button catch_voice_Button;
	private Button show_voice_Button;
	private Button delete_voice_Button;
	private ImageView show_catching_voice_imageview;
	private LocationClient mLocationClient=null;
    private LatLng mylLatLng=null;  //我当前的位置
    private LatLng goalLatLng=null;	//我的目标位置
	private String positionStatus="fail";
    
	private Client client;
	
    private double nowDistance = 0;  //当前位置距离目标地点的距离
    private double maxDistance = YzxmfConfig.maxDistance;  //管理员设置的到目标地点的最大偏差值
    
    
    private CatchVoiceTool cvt = null;
    ShowVoiceTool svt=null;
    private boolean hadCatchVoice = false;
    private boolean isCatchingVoice=false;
    private boolean isPlayingVoice=false;
    
    
    private Handler handler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		if(msg.what==0x111){  //0x111用来修改当前的地址信息,如果刚获取到信息，说明定位已完成
    			get_position_button.setText("定位");
    			get_position_button.setEnabled(true);
    			show_getting_position_proProgressBar.setVisibility(View.INVISIBLE);
    			if(msg.obj!=null)
    				check_out_position.setText(msg.obj.toString());
    		}
    		if(msg.what==0x112){  //显示正在定位的进度条
    			get_position_button.setText("正在定位中...");
    			get_position_button.setEnabled(false);
    			show_getting_position_proProgressBar.setVisibility(View.VISIBLE);
    		}
    		if(msg.what==0x113){	//显示用户的与目的地的距离提示
    			if(nowDistance>maxDistance){
    				DecimalFormat df=new DecimalFormat(".##");
    				double d=nowDistance-maxDistance;
    				String st=df.format(d);
    				check_out_position_tip.setTextColor(Color.RED);
        			check_out_position_tip.setText("没有在目标范围签退，相差"+st+" m ,加油...");
    			}else {
    				positionStatus="ok";//如果符合条件，就把位置的状态改为ok
    				check_out_position_tip.setTextColor(Color.GREEN);
        			check_out_position_tip.setText("在目标范围内签退...");
				}
    			
    		}
    		if(msg.what==0x114){//更新时间
    			check_out_time.setText(msg.obj.toString());
    		}
    		if(msg.what==0x115){  //用来显示基本信息
    			LocalStorage ls = new LocalStorage(CheckOut.this);
    			check_out_name.setText(ls.getString("name", "****"));
    			check_out_client.setText(client.getName());
    		}
    		
    		if(msg.what==0x116){  //控制录制音频
    			if(isCatchingVoice){
    				show_voice_Button.setBackgroundResource(R.drawable.rectangle_bg_6);
        			show_voice_Button.setEnabled(false);
        			delete_voice_Button.setBackgroundResource(R.drawable.rectangle_bg_6);
        			delete_voice_Button.setEnabled(false);
    			}else {
    				show_voice_Button.setBackgroundResource(R.drawable.rectangle_bg_2);
        			show_voice_Button.setEnabled(true);
        			delete_voice_Button.setBackgroundResource(R.drawable.rectangle_bg_2);
        			delete_voice_Button.setEnabled(true);
				}
    			
    		}
    		if(msg.what==0x117){ //控制播放音频
    			if(isPlayingVoice){
    				show_voice_Button.setText("停止播放");
    				catch_voice_Button.setBackgroundResource(R.drawable.rectangle_bg_6);
        			catch_voice_Button.setEnabled(false);
        			delete_voice_Button.setBackgroundResource(R.drawable.rectangle_bg_6);
        			delete_voice_Button.setEnabled(false);
    			}else {
    				show_voice_Button.setText("试听录音");
    				catch_voice_Button.setBackgroundResource(R.drawable.rectangle_bg_2);
        			catch_voice_Button.setEnabled(true);
        			delete_voice_Button.setBackgroundResource(R.drawable.rectangle_bg_2);
        			delete_voice_Button.setEnabled(true);
				}
    			
    		}
    		if(msg.what==0x118){
    			isCatchingVoice=false;
    			catch_voice_Button.setBackgroundResource(R.drawable.rectangle_bg_2);
    			catch_voice_Button.setEnabled(true);
    			show_voice_Button.setBackgroundResource(R.drawable.rectangle_bg_6);
    			show_voice_Button.setEnabled(false);
    		}
    	};
    };
    
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_out);
        
        ActivityCloser.activities.add(this);
        
        client=(Client) getIntent().getSerializableExtra("client");
        goalLatLng=new LatLng(client.getLatitude(), client.getLongitude());
        
        /**
         * 初始化组件
         */
        commit_check_out_button=(Button)findViewById(R.id.commit_check_out_button);
        check_out_time=(TextView)findViewById(R.id.check_out_time);
        check_out_name=(TextView)findViewById(R.id.check_out_name);
        get_position_button=(Button)findViewById(R.id.get_position_button);
        show_getting_position_proProgressBar=(ProgressBar)findViewById(R.id.show_getting_position_progressbar);
        check_out_position=(TextView)findViewById(R.id.check_out_position);
        check_out_position_tip=(TextView)findViewById(R.id.check_out_position_tip);
        check_out_client=(TextView)findViewById(R.id.check_out_client);
//        check_in_leave_word=(EditText)findViewById(R.id.check_in_leave_word);
        catch_voice_Button=(Button)findViewById(R.id.catch_voice_button);
        show_voice_Button=(Button)findViewById(R.id.show_voice_button);
        delete_voice_Button=(Button)findViewById(R.id.delete_voice_button);
        show_catching_voice_imageview=(ImageView)findViewById(R.id.show_catching_voice_imageview);
        
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
				Log.e("loca", location.getLatitude()+":"+location.getLongitude());
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

				getDistanceToGoal(); //定位完后立即获取与目标的距离
			}
		});
		
		get_position_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getMyLocation();
			}
		});
		
		
		
		/**
		 * 加载所有客户的信息
		 */
//		loadAllClientInfo();
        
        
        
        
        
        
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
        
        //
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
        
        /**
         * 录制音频
         */
        cvt = new CatchVoiceTool();
        
        
        catch_voice_Button.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				
				show_catching_voice_imageview.setVisibility(View.VISIBLE);
				Animation showCatchingVoiceAnimation = AnimationUtils.loadAnimation(CheckOut.this, R.anim.show_catching_voice_anim);
				show_catching_voice_imageview.setAnimation(showCatchingVoiceAnimation);
				
				cvt.startCatchVoice();
				hadCatchVoice=true;
				isCatchingVoice=true;
				/**
				 * 发送正在录制音频信号
				 */
				Message msg = new Message();
				msg.what=0x116;
				handler.sendMessage(msg);
				return false;
			}
		});
        catch_voice_Button.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v,MotionEvent event){ 
            	if(event.getAction() == MotionEvent.ACTION_UP) { //松开手后就不录制
            		show_catching_voice_imageview.setVisibility(View.INVISIBLE);
            		Animation showUnCatchingVoiceAnimation = AnimationUtils.loadAnimation(CheckOut.this, R.anim.unshow_catching_voice_anim);
    				show_catching_voice_imageview.setAnimation(showUnCatchingVoiceAnimation);
            		
                    isCatchingVoice=false;
                    cvt.stopCatchVoice(); //不再录制
                    Message msg = new Message();
    				msg.what=0x116;
    				handler.sendMessage(msg);
                }
				return false;
            }

        });
        show_voice_Button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isPlayingVoice){
					if(hadCatchVoice){
						svt = new ShowVoiceTool(CheckOut.this); 
						svt.play();
						isPlayingVoice=true;
						Message msg = new Message();
						msg.what=0x117;
						handler.sendMessage(msg);
					}
				}else {
					svt.stop();
					isPlayingVoice=false;
					Message msg = new Message();
					msg.what=0x117;
					handler.sendMessage(msg);
				}
				
			}
		});
        delete_voice_Button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean isdelete=cvt.deleteVoice();
				if(isdelete){
					Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_LONG).show();
				}else {
					Toast.makeText(getApplicationContext(), "删除失败", Toast.LENGTH_LONG).show();
				}
				Message msg = new Message();
				msg.what=0x118;
				handler.sendMessage(msg);
			}
		});
        
        
        
        
        //为提交按钮绑定事件
        commit_check_out_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/**
				 * 把签到现场保存到云
				 */
				
				if(YzxmfConfig.isConnect(CheckOut.this)){
					Calendar c = Calendar.getInstance();
					int year = c.get(Calendar.YEAR);
					int month = c.get(Calendar.MONTH)+1;
					int date = c.get(Calendar.DATE);
					String time = ""+year+"-"+month+"-"+date;
					//保存签到现场到云端
					new CheckOutHelper().execute(time);
				}
			}
		});
        
        ImageView check_out_back= (ImageView)findViewById(R.id.check_out_back);
        check_out_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CheckOut.this,MainActivity.class);
				startActivity(intent);
				CheckOut.this.finish();
			}
		});
        
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
	class CheckOutHelper extends AsyncTask<String, Integer, String>{
		@Override
		protected String doInBackground(String... params) {
			/**
			 * 让滚动条显示
			 */
			Message msg = new Message();
			msg.what=0x111;
			handler.sendMessage(msg);
			double lat=mylLatLng.latitude;
			double lon=mylLatLng.longitude;
			LocalStorage ls = new LocalStorage(CheckOut.this);
			String username = ls.getString("username", "");
			String type =ls.getString("type", "");
			String date = params[0];
			/**
			 * 保存相应数据到LeanCloud
			 */
			AvosTool avosTool = new AvosTool();
			avosTool.saveCheckOutVoice(username, type, date);
			String voiceUrl=avosTool.getCheckOutVoiceUrl(username, type, date);
			/**
			 * 保存数据到数据库
			 */
			String result = new ConnectWeb().checkOut(username,client.getId(),lat, lon, voiceUrl,nowDistance );
			
			return result;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			
			if(result.equals("ok")){
				/**
				 * 让滚动条隐藏
				 */
				Message msg = new Message();
				msg.what=0x112;
				handler.sendMessage(msg);
				Toast.makeText(CheckOut.this, "签退成功...", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(CheckOut.this,MainActivity.class);
				startActivity(intent);
				super.onPostExecute(result);
			}else {
				Toast.makeText(CheckOut.this, "签退失败...", Toast.LENGTH_LONG).show();
			}
			commit_check_out_button.setText("签退");
			commit_check_out_button.setEnabled(true);
			
		}

		@Override
		protected void onPreExecute() {
			commit_check_out_button.setText("签退中...");
			commit_check_out_button.setEnabled(false);
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
	}
}
