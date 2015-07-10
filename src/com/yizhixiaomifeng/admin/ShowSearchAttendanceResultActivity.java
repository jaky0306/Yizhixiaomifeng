package com.yizhixiaomifeng.admin;

import com.yizhixiaomifeng.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ShowSearchAttendanceResultActivity extends Activity{
	private TextView search_attendance_time ;
	private TextView search_attendance_textview2;
	private TextView search_attendance_textview3;
	private TextView search_attendance_textview4;
	private TextView search_attendance_textview5;
	private TextView search_attendance_textview6;
	private TextView search_attendance_textview7;
	private TextView show_search_attendance_textview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_attendance_aftersearch);
		search_attendance_time =(TextView)findViewById(R.id.search_attendance_textview1);
		search_attendance_textview2 =(TextView)findViewById(R.id.search_attendance_textview2);
		search_attendance_textview3 =(TextView)findViewById(R.id.search_attendance_textview3);
		search_attendance_textview4 =(TextView)findViewById(R.id.search_attendance_textview4);
		search_attendance_textview5 =(TextView)findViewById(R.id.search_attendance_textview5);
		search_attendance_textview6 =(TextView)findViewById(R.id.search_attendance_textview6);
		search_attendance_textview7 =(TextView)findViewById(R.id.search_attendance_textview7);
		show_search_attendance_textview =(TextView)findViewById(R.id.show_search_attendance_textview);
	}
}
