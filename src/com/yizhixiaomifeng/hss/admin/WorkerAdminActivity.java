package com.yizhixiaomifeng.hss.admin;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.domain.DepartmenttEntity;
import com.yizhixiaomifeng.hss.admin.adapter.DepartmentAdapter;
import com.yizhixiaomifeng.hss.admin.bean.AdminOperate;
import com.yizhixiaomifeng.hss.admin.bean.impl.AdminOperateImpl;
import com.yizhixiaomifeng.hss.util.ThreadManager;
import com.yizhixiaomifeng.hss.widget.SwipeFadeOutLayout;
import com.yizhixiaomifeng.hss.widget.SwipeFadeOutLayout.SwipeStatus;

public class WorkerAdminActivity extends Activity{
	
	public final static int REQUEST_CODE_ADD_DEPARMENT=10001;
	public final static int REQUEST_CODE_EDIT_DEPARMENT=10002;
	public final static int REQUEST_CODE_ADMIN_DEPARMENT_WORKER=10003;
	
	private ListView departmentView=null;
	private DepartmentAdapter adapter=null;
	private AdminOperate adminOperate=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_worker);
		
		adminOperate=new AdminOperateImpl();
		departmentView=(ListView) this.findViewById(R.id.admin_worker_departments);
		adapter=new DepartmentAdapter(getApplicationContext());
		initListener();
		departmentView.setAdapter(adapter);
		initContent();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==REQUEST_CODE_ADD_DEPARMENT){
			if(resultCode==RESULT_OK){
				adapter.getData().add((DepartmenttEntity) data.getSerializableExtra("department"));
				adapter.notifyDataSetChanged();
			}
		}else if(requestCode==REQUEST_CODE_EDIT_DEPARMENT||requestCode==REQUEST_CODE_ADMIN_DEPARMENT_WORKER){
			if(resultCode==RESULT_OK){
				DepartmenttEntity department=(DepartmenttEntity) data.getSerializableExtra("department");
				for(int i=0;i<adapter.getData().size();i++){
					if(adapter.getData().get(i).getNumber()==department.getNumber()){
						adapter.getData().get(i).setBusinessTypeEntity(department.getBusinessTypeEntity());
						adapter.getData().get(i).setName(department.getName());
						adapter.getData().get(i).setPhone(department.getPhone());
						adapter.getData().get(i).setWorkNumber(department.getWorkNumber());
						break;
					}
				}
				adapter.notifyDataSetChanged();
			}
		}
	}
	
	private void initListener(){
		adapter.setOnDeleteListner(new OnClickListener() {
			int position;
			@Override
			public void onClick(View v) {
				position=(Integer) v.getTag();
				ThreadManager.getInstance().clearALL();
				ThreadManager.getInstance().submit(new Runnable() {
					boolean rs;
					@Override
					public void run() {

						rs = adminOperate.deleteDepartment(adapter.getData().get(position));
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								if(rs){
									adapter.getData().remove(position);
									//�ر����д򿪵�swipeView
									adapter.getOpenSwipeLayout().close();
									adapter.notifyDataSetChanged();
								}else{
									Toast.makeText(WorkerAdminActivity.this, "����ʧ�ܣ����粻����", Toast.LENGTH_SHORT).show();
								}
							}
						});
					}
				}, ThreadManager.DEFALSE_REQUEST_POOL);
			}
		});
		adapter.setOnEditListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/**
				 * ��ת�����ű༭����
				 */
				int position=(Integer) v.getTag();
				DepartmenttEntity department=adapter.getData().get(position);
				Intent intent=new Intent(WorkerAdminActivity.this,AddOrEditDeparmentActivity.class);
				intent.putExtra("department", department);
				startActivityForResult(intent,REQUEST_CODE_EDIT_DEPARMENT);
			}
		});
		adapter.setOnItemClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SwipeFadeOutLayout swipeView=(SwipeFadeOutLayout) v;
				if(swipeView.getStatus()==SwipeStatus.ClOSE){
					/**
					 * ��ת��������Ա�б����
					 */
					int position=(Integer) v.getTag();
					DepartmenttEntity department=adapter.getData().get(position);
					Intent intent=new Intent(WorkerAdminActivity.this,WorkerAdminDPActivity.class);
					intent.putExtra("department", department);
					startActivityForResult(intent, REQUEST_CODE_ADMIN_DEPARMENT_WORKER);
				}
			}
		});
	}
	
	private void initContent() {
		
		/**
		 * û�������ݻ��棬����ÿ�ζ�Ҫ���¼�����������
		 */
		ThreadManager.getInstance().clearALL();
		ThreadManager.getInstance().submit(new Runnable() {
			
			boolean rs=false;
			@Override
			public void run() {
				List<DepartmenttEntity> data=adminOperate.loadDepartments();
				if(data!=null){
					adapter.setData(data);
					rs=true;
				}else{
					rs=false;
				}
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						/**
						 * ���سɹ��������ͼ
						 * ʧ������ʾʧ��
						 */
						if(rs){
							adapter.notifyDataSetChanged();
						}else{
							Toast.makeText(WorkerAdminActivity.this, "��Ϣ����ʧ�ܣ����粻����", Toast.LENGTH_SHORT).show();
						}
					}
				});
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
		Intent intent=new Intent(WorkerAdminActivity.this,SearchWorkerActivity.class);
		startActivity(intent);
	}
	
	public void addDepartment(View view){
		Intent intent=new Intent(WorkerAdminActivity.this, AddOrEditDeparmentActivity.class);
		startActivityForResult(intent,REQUEST_CODE_ADD_DEPARMENT);
	}

}
