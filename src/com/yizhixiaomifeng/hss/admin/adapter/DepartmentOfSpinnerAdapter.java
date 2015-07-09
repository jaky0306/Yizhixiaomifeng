package com.yizhixiaomifeng.hss.admin.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yizhixiaomifeng.domain.DepartmenttEntity;

public class DepartmentOfSpinnerAdapter extends BaseAdapter{
	
	private Context context;

	private List<DepartmenttEntity> data=new ArrayList<DepartmenttEntity>();
	
	public DepartmentOfSpinnerAdapter(Context context){
		this.context=context;
	}
	
	@Override
	public int getCount() {
		return getData().size()+1;
	}

	@Override
	public Object getItem(int position) {
		return getData().get(position-1);
	}

	@Override
	public long getItemId(int position) {
		return getData().get(position-1).getNumber();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup spinner) {
		convertView=getView(convertView);
		if(position==0)
			((TextView)convertView).setText("��ѡ����");
		else
			((TextView)convertView).setText(getData().get(position-1).getName());
		return convertView;
	}
	
	private View getView(View convertView){
		if(convertView==null){
			convertView=new TextView(context);
			convertView.setPadding(5, 5, 5, 5);
			((TextView)convertView).setGravity(Gravity.CENTER_VERTICAL);
			((TextView)convertView).setTextSize(18);
		}
		return convertView;
	}

	public List<DepartmenttEntity> getData() {
		return data;
	}

	public void setData(List<DepartmenttEntity> data) {
		this.data = data;
	}

}

