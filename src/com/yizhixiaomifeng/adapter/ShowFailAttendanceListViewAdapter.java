package com.yizhixiaomifeng.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.admin.bean.FailAttendance;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * @author Jaky
 * 
 */
public class ShowFailAttendanceListViewAdapter extends BaseAdapter {

	private Viewholder holder;
	private List<FailAttendance> list=new ArrayList<FailAttendance>();
	private Context context;
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	public ShowFailAttendanceListViewAdapter(Context context, List<FailAttendance> list) {
		this.list = list;
		this.context = context;
		Log.e("bbbbbbbbbbbbbbbb", "bbb"+list.size());
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			holder = new Viewholder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.show_attendance_listview_item, null);
			holder.attendance_item_name = (TextView) convertView.findViewById(R.id.attendance_item_name);
			holder.attendance_item_checkin_info=(TextView)convertView.findViewById(R.id.attendance_item_checkin_info);
			holder.attendance_item_checkout_info=(TextView)convertView.findViewById(R.id.attendance_item_checkout_info);
			convertView.setTag(holder); //¸´ÓÃItem
		} else {
			holder = (Viewholder) convertView.getTag();
		}
		if(list.size()>0){
			FailAttendance attendance = list.get(position);
			holder.attendance_item_name.setText(""+attendance.getName());
			StringBuffer sb1  =new StringBuffer();
			sb1.append(sdf.format(new Date(attendance.getCheckInTime()))+"  ");
			sb1.append(attendance.getCheckInStatus()+"  ");
			sb1.append(attendance.getCheckInResult());
			holder.attendance_item_checkin_info.setText(""+sb1.toString());
			StringBuffer sb2  =new StringBuffer();
			sb2.append(sdf.format(new Date(attendance.getCheckOutTime()))+"  ");
			sb2.append(attendance.getCheckOutStatus()+"  ");
			sb2.append(attendance.getCheckOutResult());
			holder.attendance_item_checkout_info.setText(""+sb2.toString());
		}
		return convertView;
	}

	
	private static class Viewholder {
		TextView attendance_item_name;
		TextView attendance_item_checkin_info;
		TextView attendance_item_checkout_info;
	}

}
