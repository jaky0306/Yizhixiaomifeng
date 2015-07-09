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
	private GeoCoder mSearch = null; // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
    private LatLng mylLatLng=null;  //�ҵ�ǰ��λ��
    private LatLng goalLatLng=null;	//�ҵ�Ŀ��λ��
	
    private double nowDistance = 0;  //��ǰλ�þ���Ŀ��ص�ľ���
    private double maxDistance = 0;  //����Ա���õĵ�Ŀ��ص�����ƫ��ֵ
    
    
    private Handler handler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		if(msg.what==0x111){  //0x111�����޸ĵ�ǰ�ĵ�ַ��Ϣ,����ջ�ȡ����Ϣ��˵����λ�����
    			get_position_button.setText("��λ");
    			get_position_button.setEnabled(true);
    			show_getting_position_proProgressBar.setVisibility(View.INVISIBLE);
    			check_in_position.setText(msg.obj.toString());
    		}
    		if(msg.what==0x112){  //��ʾ���ڶ�λ�Ľ�����
    			get_position_button.setText("���ڶ�λ��...");
    			get_position_button.setEnabled(false);
    			show_getting_position_proProgressBar.setVisibility(View.VISIBLE);
    		}
    		if(msg.what==0x113){	//��ʾ�û���
    			if(nowDistance>maxDistance){
    				check_in_position_tip.setTextColor(Color.RED);
        			check_in_position_tip.setText("����Ŀ�ĵػ������ "+(nowDistance-maxDistance)+" m ,����...");
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
         * ��ʼ�����
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
									check_in_scene_name)));
				}
				startActivityForResult(intentFromCapture,1);
			}
		});
		
		
//		mSearch.geocode(new GeoCodeOption().city(
//				editCity.getText().toString()).address(
//				editGeoCodeKey.getText().toString()));
		
        
		//���ݵ绰�����ȡ�û�������
		//��ȡ���пͻ�������
		//��ȡ����Ա�涨����Զ����ֵ
        
        /**
         * ��ʼ��Spinner������
         */
        data_list = new ArrayList<String>();
        data_list.add("��ѡ��ͻ�");
        data_list.add("�й��ƶ��Ϸ�����");
        data_list.add("bbb");
        data_list.add("ccc");
        data_list.add("ddd");
        
        //������
        ArrayAdapter<String> arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //������ʽ
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //����������
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
         * ��ʼ��������Ϣ
         */
        Message msg =new Message();
        msg.what=0x115;
        handler.sendMessage(msg);
        
        
        /**
         * ���������ݺ����̻�ȡ��ǰλ�ã�����ʱ����ʱ��
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
        
        
        //Ϊ�ύ��ť���¼�
        commit_check_in_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				
				/**
				 * ��ǩ���ֳ����浽��
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) // �����صĽ��
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
	class CheckInHelper extends AsyncTask<String, Integer, Void>{
		@Override
		protected Void doInBackground(String... params) {
			/**
			 * �ù�������ʾ
			 */
			Message msg = new Message();
			msg.what=0x111;
			handler.sendMessage(msg);
			new AvosTool().saveHead(params[0]);  //��ͷ�񱣴浽LeanCloud
			return null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Void result) {
			/**
			 * �ù���������
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
