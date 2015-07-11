package com.yizhixiaomifeng.user;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.baidu.a.a.a.c;
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
import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.admin.bean.Client;
import com.yizhixiaomifeng.config.ParameterConfig;
import com.yizhixiaomifeng.config.YzxmfConfig;
import com.yizhixiaomifeng.tools.ActivityCloser;
import com.yizhixiaomifeng.tools.AvosTool;
import com.yizhixiaomifeng.tools.CatchVoiceTool;
import com.yizhixiaomifeng.tools.ClientInfoLoader;
import com.yizhixiaomifeng.tools.ConnectWeb;
import com.yizhixiaomifeng.tools.LocalStorage;
import com.yizhixiaomifeng.tools.ShowVoiceTool;
import com.yizhixiaomifeng.user.SetHead.HeadUper;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
	private TextView check_in_client;
	private ImageView check_in_scene;
	private EditText check_in_leave_word;
	
	
	private Button catch_voice_Button;
	private Button show_voice_Button;
	private Button delete_voice_Button;
	private ImageView show_catching_voice_imageview;
	
	private LocationClient mLocationClient=null;
	private GeoCoder mSearch = null; // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
    private LatLng mylLatLng=null;  //�ҵ�ǰ��λ��
    private LatLng goalLatLng=null;	//�ҵ�Ŀ��λ��
    
	private Client client;
	
    private double nowDistance = 0;  //��ǰλ�þ���Ŀ��ص�ľ���
    private double maxDistance = YzxmfConfig.maxDistance;  //����Ա���õĵ�Ŀ��ص�����ƫ��ֵ
    
    
    private CatchVoiceTool cvt = null;
    ShowVoiceTool svt=null;
    private boolean hadCatchVoice = false;
    private boolean isCatchingVoice=false;
    private boolean isPlayingVoice=false;
    
    
    private Handler handler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		if(msg.what==0x111){  //0x111�����޸ĵ�ǰ�ĵ�ַ��Ϣ,����ջ�ȡ����Ϣ��˵����λ�����
    			get_position_button.setText("��λ");
    			get_position_button.setEnabled(true);
    			show_getting_position_proProgressBar.setVisibility(View.INVISIBLE);
    			if(msg.obj!=null){
    				check_in_position.setText(msg.obj.toString());
    			}
    		}
    		if(msg.what==0x112){  //��ʾ���ڶ�λ�Ľ�����
    			get_position_button.setText("���ڶ�λ��...");
    			get_position_button.setEnabled(false);
    			show_getting_position_proProgressBar.setVisibility(View.VISIBLE);
    		}
    		if(msg.what==0x113){	//��ʾ�û�����Ŀ�ĵصľ�����ʾ
    			if(nowDistance>maxDistance){
    				DecimalFormat df=new DecimalFormat(".##");
    				double d=nowDistance-maxDistance;
    				String st=df.format(d);
    				check_in_position_tip.setTextColor(Color.RED);
        			check_in_position_tip.setText("����Ŀ�ĵػ������ "+st+" m ,����");
    			}else {
    				check_in_position_tip.setTextColor(Color.GREEN);
        			check_in_position_tip.setText("Ŀ��ص��������Χ...");
				}
    			
    		}
    		if(msg.what==0x114){//����ʱ��
    			check_in_time.setText(msg.obj.toString());
    		}
    		if(msg.what==0x115){  //������ʾ������Ϣ
    			LocalStorage ls = new LocalStorage(CheckIn.this);
    			check_in_name.setText(ls.getString("name", "****"));
    			check_in_client.setText(client.getName());
    		}
    		
    		if(msg.what==0x116){  //����¼����Ƶ
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
    		if(msg.what==0x117){ //���Ʋ�����Ƶ
    			if(isPlayingVoice){
    				show_voice_Button.setText("ֹͣ����");
    				catch_voice_Button.setBackgroundResource(R.drawable.rectangle_bg_6);
        			catch_voice_Button.setEnabled(false);
        			delete_voice_Button.setBackgroundResource(R.drawable.rectangle_bg_6);
        			delete_voice_Button.setEnabled(false);
    			}else {
    				show_voice_Button.setText("����¼��");
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
        setContentView(R.layout.check_in);
        
        ActivityCloser.activities.add(this);
        
        client=(Client) getIntent().getSerializableExtra("client");
        goalLatLng=new LatLng(client.getLatitude(), client.getLongitude());
        Log.e("goallatlng", goalLatLng.latitude+" : "+goalLatLng.longitude);
        /**
         * ��ʼ�����
         */
        commit_check_in_button=(Button)findViewById(R.id.commit_check_in_button);
        check_in_time=(TextView)findViewById(R.id.check_in_time);
        check_in_name=(TextView)findViewById(R.id.check_in_name);
        get_position_button=(Button)findViewById(R.id.get_position_button);
        show_getting_position_proProgressBar=(ProgressBar)findViewById(R.id.show_getting_position_progressbar);
        check_in_position=(TextView)findViewById(R.id.check_in_position);
        check_in_position_tip=(TextView)findViewById(R.id.check_in_position_tip);
        check_in_client=(TextView)findViewById(R.id.check_in_client);
        check_in_scene=(ImageView)findViewById(R.id.check_in_scene);
//        check_in_leave_word=(EditText)findViewById(R.id.check_in_leave_word);
        catch_voice_Button=(Button)findViewById(R.id.catch_voice_button);
        show_voice_Button=(Button)findViewById(R.id.show_voice_button);
        delete_voice_Button=(Button)findViewById(R.id.delete_voice_button);
        show_catching_voice_imageview=(ImageView)findViewById(R.id.show_catching_voice_imageview);
        
        mLocationClient= new LocationClient(getApplicationContext());
	    LocationClientOption option = new LocationClientOption();
	    option.setLocationMode(LocationMode.Hight_Accuracy);//���ö�λģʽ
	    option.setCoorType("bd09ll");//���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
	    option.setScanSpan(50000);//���÷���λ����ļ��ʱ��Ϊ5000ms
	    option.setIsNeedAddress(true);//���صĶ�λ���������ַ��Ϣ
	    option.setNeedDeviceDirect(true);//���صĶ�λ��������ֻ���ͷ�ķ���
	    mLocationClient.setLocOption(option);
	    //Ϊλ�ÿͻ��˰��¼�
	    mLocationClient.registerLocationListener(new BDLocationListener() {
			
			@Override
			public void onReceiveLocation(BDLocation location) {
				
				if(location==null){
					Message msg = new Message();
					msg.what=0x111; //���õ�ǰλ��
					msg.obj="��ȡλ��ʧ�ܣ������¶�λ...";
					handler.sendMessage(msg);
					return;
				}
				
				//�����ҵ�ǰλ�õľ�γ��
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
				 * ��ȡ�꾭γ�Ⱥ󣬸������״̬
				 */
				Message msg = new Message();
				msg.what=0x111; //���õ�ǰλ��
				msg.obj=sb.toString();
				handler.sendMessage(msg);
				getDistanceToGoal(); //��λ���������ȡ��Ŀ��ľ���
			}
		});
	    
		mSearch = GeoCoder.newInstance();
		//Ϊ�������¼�
		mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener(){

			@Override
			public void onGetGeoCodeResult(GeoCodeResult result) {
				if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
					Toast.makeText(CheckIn.this, "��Ǹ��δ���ҵ����", Toast.LENGTH_LONG).show();
					return;
				}
				goalLatLng=new LatLng(result.getLocation().latitude, result.getLocation().longitude);
				getDistanceToGoal();
			}

			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
				if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
					Toast.makeText(CheckIn.this, "��Ǹ��δ���ҵ����", Toast.LENGTH_LONG)
							.show();
					return;
				}
				Toast.makeText(CheckIn.this, result.getAddress(),
						Toast.LENGTH_LONG).show();
			}
			
		});
		
		get_position_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getMyLocation();
			}
		});
		
		check_in_scene.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intentFromCapture = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE); // �����Դ
				if (YzxmfConfig.hasSdcard()) {
					intentFromCapture.putExtra(
							MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(new File(Environment
									.getExternalStorageDirectory(),
									YzxmfConfig.checkinScenename)));
				}
				startActivityForResult(intentFromCapture,1);
			}
		});
		
		/**
		 * �������пͻ�����Ϣ
		 */
//		loadAllClientInfo();
        
        
        
        
        
        
        /**
         * ��ʼ��������Ϣ
         */
        Message msg =new Message();
        msg.what=0x115;
        handler.sendMessage(msg);
        
        
        /**
         * ���������ݺ����̻�ȡ��ǰλ�ã�����ʱ����ʱ��
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
         * ¼����Ƶ
         */
        cvt = new CatchVoiceTool();
        
        
        catch_voice_Button.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				
				show_catching_voice_imageview.setVisibility(View.VISIBLE);
				Animation showCatchingVoiceAnimation = AnimationUtils.loadAnimation(CheckIn.this, R.anim.show_catching_voice_anim);
				show_catching_voice_imageview.setAnimation(showCatchingVoiceAnimation);
				
				cvt.startCatchVoice();
				hadCatchVoice=true;
				isCatchingVoice=true;
				/**
				 * ��������¼����Ƶ�ź�
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
            	if(event.getAction() == MotionEvent.ACTION_UP) { //�ɿ��ֺ�Ͳ�¼��
            		show_catching_voice_imageview.setVisibility(View.INVISIBLE);
            		Animation showUnCatchingVoiceAnimation = AnimationUtils.loadAnimation(CheckIn.this, R.anim.unshow_catching_voice_anim);
    				show_catching_voice_imageview.setAnimation(showUnCatchingVoiceAnimation);
            		
                    isCatchingVoice=false;
                    cvt.stopCatchVoice(); //����¼��
                    Message msg = new Message();
    				msg.what=0x116;
    				handler.sendMessage(msg);
                }
				return false;
            }

        });
        delete_voice_Button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean isdelete=cvt.deleteVoice();
				if(isdelete){
					Toast.makeText(getApplicationContext(), "ɾ���ɹ�", Toast.LENGTH_LONG).show();
				}else {
					Toast.makeText(getApplicationContext(), "ɾ��ʧ��", Toast.LENGTH_LONG).show();
				}
				Message msg = new Message();
				msg.what=0x118;
				handler.sendMessage(msg);
			}
		});
        show_voice_Button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isPlayingVoice){
					if(hadCatchVoice){
						svt = new ShowVoiceTool(CheckIn.this); 
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
        
        
        
        
        
        //Ϊ�ύ��ť���¼�
        commit_check_in_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/**
				 * ��ǩ���ֳ����浽��
				 */
//				LocalStorage ls = new LocalStorage(CheckIn.this);
//				String username = ls.getString("username", "");
//				String type =ls.getString("type", "");
//				Calendar c = Calendar.getInstance();
//				int year = c.get(Calendar.YEAR);
//				int month = c.get(Calendar.MONTH)+1;
//				int date = c.get(Calendar.DATE);
//				String time = ""+year+"-"+month+"-"+date;
//				/**
//				 * ������Ӧ���ݵ�LeanCloud
//				 */
//				AvosTool avosTool = new AvosTool();
//				avosTool.saveCheckInScene(username,type,time);  //���ֳ�ͼƬ���浽LeanCloud
//				avosTool.saveCheckInVoice(username, type, time);
//				String sceneUrl=avosTool.getCheckInSceneUrl(username, type, time);
//				String voiceUrl=avosTool.getCheckInVoiceUrl(username, type, time);
//				
//				Log.e("sceneUrl", "aa"+sceneUrl);
//				Log.e("voiceUrl", "aa"+voiceUrl);
				
				
				
				if(YzxmfConfig.isConnect(CheckIn.this)){
					Calendar c = Calendar.getInstance();
					int year = c.get(Calendar.YEAR);
					int month = c.get(Calendar.MONTH)+1;
					int date = c.get(Calendar.DATE);
					String time = ""+year+"-"+month+"-"+date;
					//����ǩ���ֳ����ƶ�
					new CheckInHelper().execute(time);
				}
			}
		});
        
        
        ImageView check_in_back =(ImageView)findViewById(R.id.check_in_back);
        check_in_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CheckIn.this.finish();
			}
		});
    }
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) // �����صĽ��
	{
		
		if(resultCode!=0)
		{
			switch (requestCode) {
			case 1:
				if (YzxmfConfig.hasSdcard()) {
					File tempFile = new File(
							Environment.getExternalStorageDirectory(),
							YzxmfConfig.checkinScenename);
					startPhotoZoom(Uri.fromFile(tempFile));
				} else {
					Toast.makeText(CheckIn.this, "δ�ҵ��洢�����޷��洢��Ƭ��",
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
	
	//ͼƬ�Ľ�ȡ
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// ���òü�
		intent.putExtra("crop", "true");
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY �ǲü�ͼƬ���
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 2);//RESULT_REQUEST_CODE
	}
	
	/**
	 * ����ü�֮���ͼƬ����
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			//ת��Բ��
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
			fos = openFileOutput(YzxmfConfig.checkinScenename, 0);
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
		Calendar c = Calendar.getInstance();//���Զ�ÿ��ʱ���򵥶��޸�
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
		 * ׼����ȡ��ǰλ����Ϣ
		 */
		Message msg = new Message();
		msg.what=0x112;
		handler.sendMessage(msg);
		mLocationClient.start();  //����λ�ÿͻ���
		
	}
	
	public void getDistanceToGoal(){
	  //����p1��p2����֮���ֱ�߾��룬��λ����  
		if(mylLatLng!=null&&goalLatLng!=null){
			
			nowDistance = DistanceUtil.getDistance(mylLatLng, goalLatLng);
			/**
			 * ��ȡ���¾���󣬸��û�һ����ʾ
			 */
			Message msg = new Message();
			msg.what=0x113;
			handler.sendMessage(msg);
		}else {
			return ;
		}
	    
	}
	
	
	
	
	
	/**
	 * �ύ����
	 * @author Jaky
	 *
	 */
	class CheckInHelper extends AsyncTask<String, Integer, String>{
		@Override
		protected String doInBackground(String... params) {
			/**
			 * �ù�������ʾ
			 */
			Message msg = new Message();
			msg.what=0x111;
			handler.sendMessage(msg);
			double lat=mylLatLng.latitude;
			double lon=mylLatLng.longitude;
			LocalStorage ls = new LocalStorage(CheckIn.this);
			String username = ls.getString("username", "");
			String type =ls.getString("type", "");
			String date = params[0];
			
			/**
			 * ������Ӧ���ݵ�LeanCloud
			 */
			AvosTool avosTool = new AvosTool();
			avosTool.saveCheckInScene(username,type,date);  //���ֳ�ͼƬ���浽LeanCloud
			avosTool.saveCheckInVoice(username, type, date);
			String sceneUrl=avosTool.getCheckInSceneUrl(username, type, date);
			String voiceUrl=avosTool.getCheckInVoiceUrl(username, type, date);
			Log.e("sceneUrl", "aa"+sceneUrl);
			Log.e("voiceUrl", "aa"+voiceUrl);
			/**
			 * �������ݵ����ݿ�
			 */
			String result = new ConnectWeb().checkIn(username,client.getId(), lat, lon, sceneUrl, voiceUrl, nowDistance);
			
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
				 * �ù���������
				 */
				Message msg = new Message();
				msg.what=0x112;
				handler.sendMessage(msg);
				Toast.makeText(CheckIn.this, "ǩ���ɹ�...", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(CheckIn.this,MainActivity.class);
				startActivity(intent);
				super.onPostExecute(result);
			}else {
				Toast.makeText(CheckIn.this, "ǩ��ʧ��...", Toast.LENGTH_LONG).show();
			}
			commit_check_in_button.setText("ǩ��");
			commit_check_in_button.setEnabled(true);
			
		}

		@Override
		protected void onPreExecute() {
			commit_check_in_button.setText("ǩ����...");
			commit_check_in_button.setEnabled(false);
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
	}
}
