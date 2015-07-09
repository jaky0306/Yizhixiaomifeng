package com.yizhixiaomifeng.hss.admin;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.domain.DepartmenttEntity;
import com.yizhixiaomifeng.domain.WorkerEntity;
import com.yizhixiaomifeng.hss.admin.adapter.WorkerAdapter;
import com.yizhixiaomifeng.hss.admin.bean.AdminOperate;
import com.yizhixiaomifeng.hss.admin.bean.impl.AdminOperateImpl;
import com.yizhixiaomifeng.hss.util.ThreadManager;

public class WorkerAdminDPActivity extends Activity{

	public final static int REQUEST_CODE_EDIT_WORKER=11101;
	public final static int REQUEST_CODE_ADD_WORKER=11102;

	private ListView workerView=null;
	private WorkerAdapter adapter;
	
	private TextView title=null;
	private DepartmenttEntity department=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_worker_dp);
		/**
		 * ��ȡ������Ϣ
		 */
		department=(DepartmenttEntity) this.getIntent().getSerializableExtra("department");
		initView();
		initContent();
	}
	
	/**
	 * ��ʼ��������ͼ
	 */
	private void initView(){
		workerView=(ListView) this.findViewById(R.id.admin_worker_dp_workers);
		title=(TextView) this.findViewById(R.id.admin_worker_dp_title);
		title.setText(department.getName());
		initAdapter();
		workerView.setAdapter(adapter);
	}

	/**
	 * ��ʼ���б�������Ϣ
	 */
	private void initContent() {
		/**
		 * û�������ݻ��棬����ÿ�ζ�Ҫ���¼�����������
		 */
		ThreadManager.getInstance().clearALL();
		ThreadManager.getInstance().submit(new Runnable() {
			
			List<WorkerEntity> data=null;
			@Override
			public void run() {
				AdminOperate loader=new AdminOperateImpl();
				data=loader.loadWokers(department);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						/**
						 * ���سɹ��������ͼ
						 * ʧ������ʾʧ��
						 */
						if(data==null){
							WorkerAdapter adapter=((WorkerAdapter)workerView.getAdapter());
							adapter.setData(data);
							adapter.notifyDataSetChanged();
						}else{
							Toast.makeText(WorkerAdminDPActivity.this, "��Ϣ����ʧ�ܣ����粻����", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		}, ThreadManager.DEFALSE_REQUEST_POOL);
	}
	
	/**
	 * ��ʼ���б�������
	 */
	private void initAdapter(){

		adapter=new WorkerAdapter(getApplicationContext());
		adapter.setEditListener(new OnClickListener() {
			
			public void onClick(View v) {
				int position=(Integer) v.getTag();
				WorkerEntity worker=adapter.getData().get(position);
				/**
				 * ��ת���༭Ա���Ľ��棺
				 */
				Intent intent=new Intent(WorkerAdminDPActivity.this, AddOrEditWorker.class);
				intent.putExtra("worker", worker);
				startActivityForResult(intent, REQUEST_CODE_EDIT_WORKER);
//				Toast.makeText(getApplicationContext(), "��ת���༭Ա���Ľ��棺", Toast.LENGTH_SHORT).show();
			}
		});
		adapter.setDeleteListener(new OnClickListener() {
			
			public void onClick(View v) {
				int position=(Integer) v.getTag();
				WorkerEntity worker=adapter.getData().get(position);

				AdminOperate operate=new AdminOperateImpl();
				/**
				 * ִ��Ա����Ϣɾ������
				 * 
				 * ɾ���ɹ��������ͼ
				 * ʧ������ʾʧ��
				 */
				if(operate.deleteWorker(worker)){
					adapter.getData().remove(position);
					adapter.notifyDataSetChanged();
				}else{
					Toast.makeText(getApplicationContext(), "����ʧ�ܣ����粻����", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==REQUEST_CODE_ADD_WORKER){
			if(resultCode==RESULT_OK){
				adapter.getData().add((WorkerEntity) data.getSerializableExtra("worker"));
				adapter.notifyDataSetChanged();
			}
		}else if(requestCode==REQUEST_CODE_EDIT_WORKER){
			if(resultCode==RESULT_OK){
				WorkerEntity worker=(WorkerEntity) data.getSerializableExtra("worker");
				WorkerEntity wk=null;
				for(int i=0;i<adapter.getData().size();i++){
					wk=adapter.getData().get(i);
					if(wk.getJobNum()==worker.getJobNum()){
						/**
						 * ����Ѿ������ڱ����ţ������Ƴ�����Ա���б�
						 * �����������
						 */
						if(wk.getDepartmenttEntity().getNumber()!=worker.getDepartmenttEntity().getNumber()){
							adapter.getData().remove(i);
						}else{
							wk.setName(worker.getName());
							wk.setDutyTypeEntity(worker.getDutyTypeEntity());
							wk.setEntryDate(worker.getEntryDate());
							wk.setBasePay(worker.getBasePay());
						}
						break;
					}
				}
				adapter.notifyDataSetChanged();
			}
		}
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
	 * ���Ա���¼�
	 * @param v
	 */
	public void addWorker(View v){
		/**
		 * ��ת�����Ա���Ľ���
		 */
		Intent intent=new Intent(WorkerAdminDPActivity.this, AddOrEditWorker.class);
		startActivityForResult(intent, REQUEST_CODE_ADD_WORKER);
//		Toast.makeText(getApplicationContext(), "��ת�����Ա���Ľ���", Toast.LENGTH_SHORT).show();
	}
}
