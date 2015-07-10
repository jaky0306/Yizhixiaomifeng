package com.yizhixiaomifeng.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.tools.ActivityCloser;
import com.yizhixiaomifeng.tools.ConnectWeb;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;


public class SearchAttendanceActivity extends Activity{
	private EditText search_attendance_staff_name;
	private EditText search_attendance_start_time;
	private EditText search_attendance_end_time;
	private Button search_attendance_button;
	String staff_name;
	String start_time;
	String end_time;
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
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			
			
			
			
			
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
