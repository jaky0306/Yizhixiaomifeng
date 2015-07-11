package com.yizhixiaomifeng.admin;

import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.admin.bean.FailAttendance;
import com.yizhixiaomifeng.tools.ActivityCloser;
import com.yizhixiaomifeng.tools.AvosTool;
import com.yizhixiaomifeng.tools.ShowVoiceTool;
import com.yizhixiaomifeng.user.CheckOut;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ShowFailAttendanceInfoActivity extends Activity{
	private ImageView show_checkIn_scene_imageView;
	private Button show_checkIn_voice_button;
	private Button show_checkOut_voice_button;
	private boolean isPlayingCheckInVoice=false;
	private boolean isPlayingCheckOutVoice=false;
	private FailAttendance failAttendance;
	private LinearLayout show_loadinfo_tip_LinearLayout;
	ImageView show_playing_voice_imageview ;
	ShowVoiceTool checkin_svt;
	ShowVoiceTool checkout_svt;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==0x111){
				if(isPlayingCheckInVoice){
					show_checkIn_voice_button.setText("Í£Ö¹²¥·Å");
				}else {
					show_checkIn_voice_button.setText("²¥·ÅÇ©µ½Â¼Òô");
				}
				
			}
			if(msg.what==0x112){
				if(isPlayingCheckOutVoice){
					show_checkOut_voice_button.setText("Í£Ö¹²¥·Å");
				}else {
					show_checkOut_voice_button.setText("²¥·ÅÇ©ÍËÂ¼Òô");
				}
			}
			
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_fail_attendance);
		ActivityCloser.activities.add(this);
		failAttendance=(FailAttendance) getIntent().getSerializableExtra("failattendance");
		show_loadinfo_tip_LinearLayout=(LinearLayout)findViewById(R.id.show_loadattendanceinfo_tip_LinearLayout);
		
		show_playing_voice_imageview=(ImageView)findViewById(R.id.show_playing_voice_imageview);
		show_checkIn_scene_imageView=(ImageView)findViewById(R.id.showfailattendance_check_in_scene);
		show_checkIn_voice_button=(Button)findViewById(R.id.showfailattendance_checkin_voice_button);
		show_checkOut_voice_button=(Button)findViewById(R.id.showfailattendance_checkout_voice_button);
		show_checkIn_voice_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isPlayingCheckInVoice)
				{
					show_checkIn_scene_imageView.setVisibility(View.VISIBLE);
					Animation showCatchingVoiceAnimation = AnimationUtils.loadAnimation(ShowFailAttendanceInfoActivity.this, R.anim.show_catching_voice_anim);
					show_checkIn_scene_imageView.setAnimation(showCatchingVoiceAnimation);
					checkin_svt = new ShowVoiceTool(ShowFailAttendanceInfoActivity.this); 
					checkin_svt.play(failAttendance.getCheckInVoiceUrl());
					isPlayingCheckInVoice=true;
					Message msg = new Message();
					msg.what=0x111;
					handler.sendMessage(msg);
					
				}else{
					show_playing_voice_imageview.setVisibility(View.INVISIBLE);
            		Animation showUnCatchingVoiceAnimation = AnimationUtils.loadAnimation(ShowFailAttendanceInfoActivity.this, R.anim.unshow_catching_voice_anim);
            		show_playing_voice_imageview.setAnimation(showUnCatchingVoiceAnimation);
            		checkin_svt.stop();
            		isPlayingCheckInVoice=false;
					Message msg = new Message();
					msg.what=0x111;
					handler.sendMessage(msg);
				}
			}
		});
		show_checkOut_voice_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isPlayingCheckOutVoice)
				{
					show_playing_voice_imageview.setVisibility(View.INVISIBLE);
            		Animation showUnCatchingVoiceAnimation = AnimationUtils.loadAnimation(ShowFailAttendanceInfoActivity.this, R.anim.unshow_catching_voice_anim);
            		show_playing_voice_imageview.setAnimation(showUnCatchingVoiceAnimation);
            		
            		checkout_svt = new ShowVoiceTool(ShowFailAttendanceInfoActivity.this); 
            		checkout_svt.play(failAttendance.getCheckOutVoiceUrl());
					isPlayingCheckOutVoice=true;
					Message msg = new Message();
					msg.what=0x112;
					handler.sendMessage(msg);
					
				}else{
					show_playing_voice_imageview.setVisibility(View.INVISIBLE);
            		Animation showUnCatchingVoiceAnimation = AnimationUtils.loadAnimation(ShowFailAttendanceInfoActivity.this, R.anim.unshow_catching_voice_anim);
            		show_playing_voice_imageview.setAnimation(showUnCatchingVoiceAnimation);
					
            		checkout_svt.stop();
            		isPlayingCheckOutVoice=false;
					Message msg = new Message();
					msg.what=0x112;
					handler.sendMessage(msg);
				}
			}
		});
		
		
		//
		new GetSceneDataByUrl().execute(failAttendance.getCheckInSceneUrl());
		
	}
	
	class GetSceneDataByUrl extends AsyncTask<String, Integer, byte[]>{

		@Override
		protected byte[] doInBackground(String... params) {
			return new AvosTool().getImgData(params[0]);
		}

		@Override
		protected void onPostExecute(byte[] result) {
			if(result!=null){
				Bitmap b = BitmapFactory.decodeByteArray(result, 0, result.length);
				show_checkIn_scene_imageView.setImageBitmap(b);
			}
			show_loadinfo_tip_LinearLayout.setVisibility(View.INVISIBLE);
			show_checkIn_voice_button.setBackgroundResource(R.drawable.rectangle_bg_5);
			show_checkIn_voice_button.setEnabled(true);
			show_checkOut_voice_button.setBackgroundResource(R.drawable.rectangle_bg_5);
			show_checkOut_voice_button.setEnabled(true);
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			show_loadinfo_tip_LinearLayout.setVisibility(View.VISIBLE);
			show_checkIn_voice_button.setBackgroundResource(R.drawable.rectangle_bg_6);
			show_checkIn_voice_button.setEnabled(false);
			show_checkOut_voice_button.setBackgroundResource(R.drawable.rectangle_bg_6);
			show_checkOut_voice_button.setEnabled(false);
			super.onPreExecute();
		}
		
		
	}
}
