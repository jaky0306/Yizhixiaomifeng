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
    private LatLng mylLatLng=null;  //�ҵ�ǰ��λ��
    private LatLng goalLatLng=null;	//�ҵ�Ŀ��λ��
	private String positionStatus="fail";
    
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
    			if(msg.obj!=null)
    				check_out_position.setText(msg.obj.toString());
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
    				check_out_position_tip.setTextColor(Color.RED);
        			check_out_position_tip.setText("û����Ŀ�귶Χǩ�ˣ����"+st+" m ,����...");
    			}else {
    				positionStatus="ok";//��������������Ͱ�λ�õ�״̬��Ϊok
    				check_out_position_tip.setTextColor(Color.GREEN);
        			check_out_position_tip.setText("��Ŀ�귶Χ��ǩ��...");
				}
    			
    		}
    		if(msg.what==0x114){//����ʱ��
    			check_out_time.setText(msg.obj.toString());
    		}
    		if(msg.what==0x115){  //������ʾ������Ϣ
    			LocalStorage ls = new LocalStorage(CheckOut.this);
    			check_out_name.setText(ls.getString("name", "****"));
    			check_out_client.setText(client.getName());
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
        setContentView(R.layout.check_out);
        
        ActivityCloser.activities.add(this);
        
        client=(Client) getIntent().getSerializableExtra("client");
        goalLatLng=new LatLng(client.getLatitude(), client.getLongitude());
        
        /**
         * ��ʼ�����
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
		
		get_position_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getMyLocation();
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
				Animation showCatchingVoiceAnimation = AnimationUtils.loadAnimation(CheckOut.this, R.anim.show_catching_voice_anim);
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
            		Animation showUnCatchingVoiceAnimation = AnimationUtils.loadAnimation(CheckOut.this, R.anim.unshow_catching_voice_anim);
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
					Toast.makeText(getApplicationContext(), "ɾ���ɹ�", Toast.LENGTH_LONG).show();
				}else {
					Toast.makeText(getApplicationContext(), "ɾ��ʧ��", Toast.LENGTH_LONG).show();
				}
				Message msg = new Message();
				msg.what=0x118;
				handler.sendMessage(msg);
			}
		});
        
        
        
        
        //Ϊ�ύ��ť���¼�
        commit_check_out_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/**
				 * ��ǩ���ֳ����浽��
				 */
				
				if(YzxmfConfig.isConnect(CheckOut.this)){
					Calendar c = Calendar.getInstance();
					int year = c.get(Calendar.YEAR);
					int month = c.get(Calendar.MONTH)+1;
					int date = c.get(Calendar.DATE);
					String time = ""+year+"-"+month+"-"+date;
					//����ǩ���ֳ����ƶ�
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
	class CheckOutHelper extends AsyncTask<String, Integer, String>{
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
			LocalStorage ls = new LocalStorage(CheckOut.this);
			String username = ls.getString("username", "");
			String type =ls.getString("type", "");
			String date = params[0];
			/**
			 * ������Ӧ���ݵ�LeanCloud
			 */
			AvosTool avosTool = new AvosTool();
			avosTool.saveCheckOutVoice(username, type, date);
			String voiceUrl=avosTool.getCheckOutVoiceUrl(username, type, date);
			/**
			 * �������ݵ����ݿ�
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
				 * �ù���������
				 */
				Message msg = new Message();
				msg.what=0x112;
				handler.sendMessage(msg);
				Toast.makeText(CheckOut.this, "ǩ�˳ɹ�...", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(CheckOut.this,MainActivity.class);
				startActivity(intent);
				super.onPostExecute(result);
			}else {
				Toast.makeText(CheckOut.this, "ǩ��ʧ��...", Toast.LENGTH_LONG).show();
			}
			commit_check_out_button.setText("ǩ��");
			commit_check_out_button.setEnabled(true);
			
		}

		@Override
		protected void onPreExecute() {
			commit_check_out_button.setText("ǩ����...");
			commit_check_out_button.setEnabled(false);
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
	}
}
