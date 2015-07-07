package com.yizhixiaomifeng.hss.admin;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.domain.DepartmenttEntity;
import com.yizhixiaomifeng.hss.util.ThreadManager;

public class WorkerAdminActivity extends Activity{
	
	private ListView departmentView=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_worker);
		
		departmentView=(ListView) this.findViewById(R.id.admin_worker_departments);
		departmentView.setAdapter(new DepartmentAdapter(this));
		departmentView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View item, int position,
					long id) {
				Toast.makeText(WorkerAdminActivity.this, "��ת�����Ź������", Toast.LENGTH_SHORT).show();
			}
		});
		initContent();
	}
	
	
	private void initContent() {
		ThreadManager.getInstance().clearALL();
		ThreadManager.getInstance().submit(new Runnable() {
			
			@Override
			public void run() {
				
			}
		}, ThreadManager.DEFALSE_REQUEST_POOL);
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back(null);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	/**
	 * �����¼�����
	 * @param v
	 */
	public void back(View v){
		ThreadManager.getInstance().clearALL();
		finish();
	}
	
	/**
	 * ���Ҽ�������
	 * @param v
	 */
	public void search(View v){
		Toast.makeText(getApplicationContext(), "��ת��Ա�����ҽ���", Toast.LENGTH_SHORT).show();
	}
	
	
	protected class DepartmentAdapter extends BaseAdapter{
		
		private Context context=null;
		private List<DepartmenttEntity> data=new ArrayList<DepartmenttEntity>();
		
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
			 * ��ȡitem��ͼ
			 */
			convertView = getView(position, convertView);
			
			/**
			 * ����������һ��item���������
			 */
			if(position!=getData().size()){
				/**
				 * ��ȡ��ͼ����ӵ����
				 */
				holder = getHolder(convertView);
				holder.name.setText(getData().get(position).getName());
				holder.member.setText(getData().get(position).getNumber()+"��");
			}
			return convertView;
		}
		
		private View buttonItem=null;
		private View getView(int position,View convertView){
			/**
			 * ��������һ��item������Ӱ�ť��ͼ
			 */
			if(position==getData().size()){
				if(buttonItem==null){
					convertView = View.inflate(context, R.layout.hss_button, null);
					convertView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							Toast.makeText(context, "��ת����Ӳ��Ž���", Toast.LENGTH_SHORT).show();
						}
					});
				}else{
					convertView=buttonItem;
				}
				Button btn=(Button) convertView.findViewById(R.id.hss_btn);
				btn.setText("��������");
				return convertView;
			}
			/**
			 * �������һ��itemʱ���ز�����ͼ
			 */
			if(convertView==null){
				convertView = View.inflate(context, R.layout.list_item_department, null);
			}
			return convertView;
		}
		
		private ViewHolder getHolder(View convertView){
			ViewHolder holder=(ViewHolder) convertView.getTag();
			if(holder==null){
				holder=new ViewHolder(convertView);
			}
			return holder;
		}
		

		private class ViewHolder{
			public TextView name;
			public TextView member;
			
			public ViewHolder(View convertView){
				name=(TextView) convertView.findViewById(R.id.department_name);
				member=(TextView) convertView.findViewById(R.id.department_member);
			}
		}
		
	}
}
