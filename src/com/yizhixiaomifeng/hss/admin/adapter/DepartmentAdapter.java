package com.yizhixiaomifeng.hss.admin.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.domain.DepartmenttEntity;
import com.yizhixiaomifeng.hss.widget.SwipeFadeOutLayout;
import com.yizhixiaomifeng.hss.widget.SwipeFadeOutLayout.OnSwipeStatusChangeListener;

public class DepartmentAdapter extends BaseAdapter{
	
	private Context context=null;
	private List<DepartmenttEntity> data=new ArrayList<DepartmenttEntity>();
	private OnClickListener onItemClickListener=null;
	private OnClickListener onEditListener=null;
	private OnClickListener onDeleteListner=null;
	
	public DepartmentAdapter(Context context){
		this.context=context;
	}

	public List<DepartmenttEntity> getData() {
		return data;
	}

	public void setData(List<DepartmenttEntity> data) {
		this.data = data;
	}
	
	@Override
	public int getCount() {
		return getData().size()+1;
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
		convertView = getView(position, convertView);
		convertView.setTag(position);
		
		/**
		 * 如果不是最后一个item则填充数据
		 */
		if(position!=getData().size()){
			/**
			 * 获取视图内容拥有者
			 */
			holder = getHolder(convertView);
			holder.name.setText(getData().get(position).getName());
			holder.member.setText(getData().get(position).getNumber()+"人");
			/**
			 * 设置位置标识，方便监听器获取对应数据
			 */
			holder.deleteButton.setTag(position);
			holder.editButton.setTag(position);
			holder.swipeLayout.setTag(position);
		}else{
			
		}
		return convertView;
	}
	
	private View buttonItem=null;
	private View getView(int position,View convertView){
		if(convertView==null){
			convertView = View.inflate(context, R.layout.list_item_department, null);
		}
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
	

	public OnClickListener getOnItemClickListener() {
		return onItemClickListener;
	}

	public void setOnItemClickListener(OnClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}


	public OnClickListener getOnEditListener() {
		return onEditListener;
	}

	public void setOnEditListener(OnClickListener onEditListener) {
		this.onEditListener = onEditListener;
	}


	public OnClickListener getOnDeleteListner() {
		return onDeleteListner;
	}

	public void setOnDeleteListner(OnClickListener onDeleteListner) {
		this.onDeleteListner = onDeleteListner;
	}

	public SwipeFadeOutLayout getOpenSwipeLayout() {
		return openSwipeLayout;
	}

	private class ViewHolder{
		public TextView name;
		public TextView member;
//		public RelativeLayout contentLayout;
		public SwipeFadeOutLayout swipeLayout;
		public ImageView editButton;
		public ImageView deleteButton;
		
		public ViewHolder(View convertView){
			name=(TextView) convertView.findViewById(R.id.department_name);
			member=(TextView) convertView.findViewById(R.id.department_member);
//			contentLayout=(RelativeLayout) convertView.findViewById(R.id.department_go_layout);
			swipeLayout=(SwipeFadeOutLayout) convertView.findViewById(R.id.department_layout);
			editButton=(ImageView) convertView.findViewById(R.id.department_edit);
			deleteButton=(ImageView) convertView.findViewById(R.id.department_delete);
			if(onItemClickListener!=null)
				convertView.setOnClickListener(onItemClickListener);
			if(onEditListener!=null)
				editButton.setOnClickListener(onEditListener);
			if(onDeleteListner!=null)
				deleteButton.setOnClickListener(onDeleteListner);
			if(onStatusChangeListener!=null)
				swipeLayout.setOnSwipeStatusChangeListener(onStatusChangeListener);
		}
	}
	

	/**
	 * 记录上一个处于打开状态的SwipeFadeOutLayout
	 */
	private SwipeFadeOutLayout openSwipeLayout=null;
	private OnSwipeStatusChangeListener onStatusChangeListener=new OnSwipeStatusChangeListener() {
		
		@Override
		public void onSwipe(SwipeFadeOutLayout layout) {
			/**
			 * 上一个打开的SwipeFadeOutLayout不为空，
			 * 且和当前的SwipeFadeOutLayout不是同一个时，
			 * 关闭上一个打开的SwipeFadeOutLayout
			 */
			if(openSwipeLayout!=null&&!openSwipeLayout.equals(layout)){
				openSwipeLayout.close();
			}
		}
		
		@Override
		public void onOpen(SwipeFadeOutLayout layout) {
			/**
			 * 记录下打开的SwipeFadeOutLayout
			 */
			openSwipeLayout=layout;
		}
		
		@Override
		public void onClose(SwipeFadeOutLayout layout) {
			if(openSwipeLayout.equals(layout)){
				openSwipeLayout=null;
			}
		}
	};
	
}