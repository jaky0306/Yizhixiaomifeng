package com.yizhixiaomifeng.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.admin.bean.FailAttendance;
import com.yizhixiaomifeng.config.CheckInAndOutStauts;
import com.yizhixiaomifeng.tools.ActivityCloser;
import com.yizhixiaomifeng.tools.ConnectWeb;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class SearchAttendanceActivity extends Activity{
	private EditText search_attendance_staff_name;
	private EditText search_attendance_start_time;
	private EditText search_attendance_end_time;
	private Button search_attendance_button;
	String staff_name;
	String start_time;
	String end_time;
	
	private int totalnum=0;		//考勤人数
	private int belatenum=0;	//迟到人数
	private int leaveeariernum=0;	//早退人数
	private int absencenum=0;
	
	private TextView show_search_attendance_result;
	
	private Calendar c = Calendar.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_attendance);
		ActivityCloser.activities.add(this);
		search_attendance_staff_name=(EditText)findViewById(R.id.search_attendance_staff_name);
		search_attendance_start_time=(EditText)findViewById(R.id.search_attendance_start_time);
		search_attendance_end_time=(EditText)findViewById(R.id.search_attendance_end_time);
		search_attendance_button=(Button)findViewById(R.id.search_attendance_button);
		show_search_attendance_result=(TextView)findViewById(R.id.show_search_attendance_result);
		search_attendance_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				staff_name=search_attendance_staff_name.getText().toString();
				start_time=search_attendance_start_time.getText().toString();
				end_time=search_attendance_end_time.getText().toString();
				new GetAttendanceData().execute("");
			}
		});
		search_attendance_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(SearchAttendanceActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        search_attendance_start_time.setText(DateFormat.format("yyy-MM-dd", c));
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
		search_attendance_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(SearchAttendanceActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        search_attendance_end_time.setText(DateFormat.format("yyy-MM-dd", c));
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
		
		ImageView search_attendance_back = (ImageView)findViewById(R.id.search_attendance_back);
		search_attendance_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SearchAttendanceActivity.this,ManageAttendanceActivity.class);
				startActivity(intent);
				SearchAttendanceActivity.this.finish();
			}
		});
	}
	class GetAttendanceData extends AsyncTask<String,Integer,String>{

		@Override
		protected String doInBackground(String... params) {
			SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd" );
			String result = null;
			Date start_date;
			Date end_date;
			long st=0;
			long et=0;
			try {
				start_date = format.parse(start_time);
				end_date= format.parse(end_time);
				st = start_date.getTime();
				et=end_date.getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if(staff_name==null||staff_name.length()==0)
			{
				result = new ConnectWeb().getAttendanceDataByDate(st, et);
			}else
			{
				result=new ConnectWeb().getAttendanceDataByDateAndName(staff_name, st, et);
			}
			Log.e("result", ""+result);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			StringBuffer sb = new StringBuffer();
			if(result!=null){
				try {
					JSONArray jsonArray = new JSONArray(result);
					Log.e("size", ""+jsonArray.length());
					for(int i=0;i<jsonArray.length();i++){
						JSONObject object = jsonArray.getJSONObject(i);
						totalnum=object.getInt("RecordAllNum");
						long checkInTime = object.getLong("checkInTime");
						String checkInStatus = object.getString("checkInStatus");
						String checkInResult = object.getString("checkInResult");
						long checkOutTime = object.getLong("checkOutTime");
						String checkOutStatus = object.getString("checkOutStatus");
						String checkOutResult=object.getString("checkOutResult");
						String checkInSceneUrl=object.getString("checkInSceneURL");
						String checkInVoiceUrl=object.getString("checkInVoice");
						String checkOutVoiceUrl=object.getString("checkOutVoiceUrl");
						
						JSONObject mission= object.getJSONObject("missionAllotId");
						JSONObject jobnum=mission.getJSONObject("jobNum");
						String phone = jobnum.getString("phone");
						String name = jobnum.getString("name");
						
						FailAttendance fa = new FailAttendance(phone, name, checkInTime, checkInStatus, checkInResult, checkOutTime, checkOutStatus, checkOutResult, checkInSceneUrl, checkInVoiceUrl, checkOutVoiceUrl);
						//迟到
						if(fa.getCheckInResult().equals(CheckInAndOutStauts.checkIn_late)){
							belatenum++;
						}
						//早退
						if(fa.getCheckOutResult().equals(CheckInAndOutStauts.checkOut_earier)){
							leaveeariernum++;
						}
						if(fa.getCheckInResult().equals(CheckInAndOutStauts.checkIn_nocheck)||fa.getCheckOutResult().equals(CheckInAndOutStauts.checkOut_nocheck)){
							absencenum++;
						}
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						String checkintime = sdf.format(checkInTime);
						String checkouttime = sdf.format(checkInTime);
						
						sb.append(name+"\n   "+checkintime+"  "+checkInStatus+"  "+checkInResult+"  "+checkouttime+"  "+checkOutStatus+"  "+checkOutResult+"\n");
					}
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
			}
			
			
			if(sb.length()==0){
				sb.append("没有数据...");
			}
			show_search_attendance_result.setText(sb.toString());
			
			
			search_attendance_button.setText("查询");
			search_attendance_button.setEnabled(true);
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			search_attendance_button.setText("正在查询...");
			search_attendance_button.setEnabled(false);
			super.onPreExecute();
		}
		
	}
}
