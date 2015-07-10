package com.yizhixiaomifeng.admin;

import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.admin.bean.FailAttendance;
import com.yizhixiaomifeng.tools.ActivityCloser;
import com.yizhixiaomifeng.tools.AvosTool;
import com.yizhixiaomifeng.tools.ShowVoiceTool;
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

public class ShowFailAttendanceInfoActivity extends Activity{
	private ImageView show_checkIn_scene_imageView;
	private Button show_checkIn_voice_button;
	private Button show_checkOut_voice_button;
	private boolean isPlayingCheckInVoice=false;
	private boolean isPlayingCheckOutVoice=false;
	private FailAttendance failAttendance;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==0x111){
				if(isPlayingCheckInVoice){
					show_checkIn_voice_button.setText("Í£Ö¹²¥·Å");
					show_checkOut_voice_button.setEnabled(false);
					show_checkOut_voice_button.setBackgroundResource(R.drawable.rectangle_bg_6);
				}else {
					show_checkIn_voice_button.setText("²¥·ÅÇ©µ½Â¼Òô");
					show_checkOut_voice_button.setEnabled(true);
					show_checkOut_voice_button.setBackgroundResource(R.drawable.rectangle_bg_5);
				}
				if(isPlayingCheckOutVoice){
					show_checkOut_voice_button.setText("Í£Ö¹²¥·Å");
					show_checkIn_voice_button.setEnabled(false);
					show_checkIn_voice_button.setBackgroundResource(R.drawable.rectangle_bg_6);
				}else {
					show_checkOut_voice_button.setText("²¥·ÅÇ©ÍËÂ¼Òô");
					show_checkIn_voice_button.setEnabled(true);
					show_checkIn_voice_button.setBackgroundResource(R.drawable.rectangle_bg_5);
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
		
		
		new GetSceneDataByUrl().execute(failAttendance.getCheckInSceneUrl());
		
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
					
					ShowVoiceTool svt = new ShowVoiceTool(ShowFailAttendanceInfoActivity.this); 
					svt.play(failAttendance.getCheckInVoiceUrl());
					isPlayingCheckInVoice=true;
					Message msg = new Message();
					msg.what=0x111;
					handler.sendMessage(msg);
					
				}else{
					
					show_checkIn_scene_imageView.setVisibility(View.INVISIBLE);
            		Animation showUnCatchingVoiceAnimation = AnimationUtils.loadAnimation(ShowFailAttendanceInfoActivity.this, R.anim.unshow_catching_voice_anim);
            		show_checkIn_scene_imageView.setAnimation(showUnCatchingVoiceAnimation);
					
            		isPlayingCheckInVoice=false;
					Message msg = new Message();
					msg.what=0x111;
					handler.sendMessage(msg);
				}
				
				if(!isPlayingCheckOutVoice)
				{
					show_checkIn_scene_imageView.setVisibility(View.INVISIBLE);
            		Animation showUnCatchingVoiceAnimation = AnimationUtils.loadAnimation(ShowFailAttendanceInfoActivity.this, R.anim.unshow_catching_voice_anim);
            		show_checkIn_scene_imageView.setAnimation(showUnCatchingVoiceAnimation);
					ShowVoiceTool svt = new ShowVoiceTool(ShowFailAttendanceInfoActivity.this); 
					svt.play(failAttendance.getCheckOutVoiceUrl());
					isPlayingCheckOutVoice=true;
					Message msg = new Message();
					msg.what=0x111;
					handler.sendMessage(msg);
				}else{
					show_checkIn_scene_imageView.setVisibility(View.INVISIBLE);
            		Animation showUnCatchingVoiceAnimation = AnimationUtils.loadAnimation(ShowFailAttendanceInfoActivity.this, R.anim.unshow_catching_voice_anim);
            		show_checkIn_scene_imageView.setAnimation(showUnCatchingVoiceAnimation);
					isPlayingCheckOutVoice=false;
					Message msg = new Message();
					msg.what=0x111;
					handler.sendMessage(msg);
				}
				
			}
		});
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
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		
	}
}
