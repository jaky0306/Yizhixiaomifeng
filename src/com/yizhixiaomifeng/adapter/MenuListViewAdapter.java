package com.yizhixiaomifeng.adapter;

import java.util.List;

import com.yizhixiaomifeng.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuListViewAdapter extends BaseAdapter {

	private ViewHolder holder;
	private List<String> list;
	private Context context;

	public MenuListViewAdapter(Context context, List<String> list) {
		this.list = list;
		this.context = context;
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
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.menuitems, null);
			holder.icon=(ImageView)convertView.findViewById(R.id.menuitemes_icon);
			holder.title = (TextView) convertView.findViewById(R.id.menuitems_title);
			holder.title.setTextSize(22);
			convertView.setTag(holder); //∏¥”√Item
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.icon.setBackgroundResource(R.drawable.right_icon_small);
		holder.title.setText(list.get(position));
		return convertView;
	}

	private static class ViewHolder {
		ImageView icon;
		TextView title;
	}

}
