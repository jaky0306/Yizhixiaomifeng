package com.yizhixiaomifeng.hss.admin.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.domain.WorkerEntity;
import com.yizhixiaomifeng.hss.util.TimeChangeUtil;

public class WorkerAdapter  extends BaseAdapter{
	
	private Context context=null;
	
	private OnClickListener editListener;
	private OnClickListener deleteListener;
	
	private List<WorkerEntity> data=new ArrayList<WorkerEntity>();
	
	public WorkerAdapter(Context context){
		this.context=context;
	}

	public List<WorkerEntity> getData() {
		return data;
	}

	public void setData(List<WorkerEntity> data) {
		this.data = data;
	}
	
	@Override
	public int getCount() {
		return getData().size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private ViewHolder holder=null;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/**
		 * 获取item视图
		 */
		if(convertView==null){
			convertView = View.inflate(context, R.layout.list_item_worker, null);
		}
		/**
		 * 获取视图内容拥有者
		 */
		holder = getHolder(convertView);
		holder.name.setText(getData().get(position).getName());
		holder.duty.setText("职位："+getData().get(position).getDutyTypeEntity().getName());
		holder.jobNumber.setText("工号："+getData().get(position).getJobNum());
		holder.entryTime.setText("入职时间："+TimeChangeUtil.getTimeString(getData().get(position).getEntryDate()));
		/**
		 * 设置位置标识，方便监听器获取该位置对应的worker对象
		 */
		holder.editButton.setTag(position);
		holder.deleteButton.setTag(position);
		/**
		 * 设置监听器，处理事件响应
		 */
		holder.editButton.setOnClickListener(editListener);
		holder.deleteButton.setOnClickListener(deleteListener);
		return convertView;
	}
	
	private ViewHolder getHolder(View convertView){
		ViewHolder holder=(ViewHolder) convertView.getTag();
		if(holder==null){
			holder=new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		return holder;
	}
	

	public OnClickListener getEditListener() {
		return editListener;
	}

	public void setEditListener(OnClickListener editListener) {
		this.editListener = editListener;
	}


	public OnClickListener getDeleteListener() {
		return deleteListener;
	}

	public void setDeleteListener(OnClickListener deleteListener) {
		this.deleteListener = deleteListener;
	}


	private class ViewHolder{
		public TextView name;
		public TextView duty;
		public TextView jobNumber;
		public TextView entryTime;
		public ImageView editButton;
		public ImageView deleteButton;
		
		public ViewHolder(View convertView){
			name=(TextView) convertView.findViewById(R.id.worker_name);
			duty=(TextView) convertView.findViewById(R.id.worker_duty);
			jobNumber=(TextView) convertView.findViewById(R.id.worker_jobnum);
			entryTime=(TextView) convertView.findViewById(R.id.worker_entrytime);
			editButton=(ImageView) convertView.findViewById(R.id.worker_edit);
			deleteButton=(ImageView) convertView.findViewById(R.id.worker_delete);
		}
	}
	
}
