package com.yizhixiaomifeng.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.admin.bean.News;
import android.content.Context;
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
public class NewsViewAdapter extends BaseAdapter {

	private ViewHolder holder;
	private List<News> list;
	private Context context;
	public NewsViewAdapter(Context context, List<News> list) {
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
					R.layout.show_news_listview_item, null);
			holder.title = (TextView) convertView.findViewById(R.id.news_title);
			holder.content=(TextView)convertView.findViewById(R.id.news_content);
			holder.time=(TextView)convertView.findViewById(R.id.news_time);
			convertView.setTag(holder); //∏¥”√Item
			
			

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final News news = list.get(position);
		holder.title.setText(news.getTitle());
		holder.content.setText(news.getContent());
		String time= ""+news.getReleasedTime();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");  
		String timeString = sdf.format(new Date(Long.parseLong(time)));  
		holder.time.setText(timeString);
		return convertView;
	}

	
	private static class ViewHolder {
		TextView title;
		TextView content;
		TextView time;
	}

}
