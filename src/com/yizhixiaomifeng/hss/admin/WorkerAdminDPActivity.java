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
		 * 获取部门信息
		 */
		department=(DepartmenttEntity) this.getIntent().getSerializableExtra("department");
		initView();
		initContent();
	}
	
	/**
	 * 初始化界面视图
	 */
	private void initView(){
		workerView=(ListView) this.findViewById(R.id.admin_worker_dp_workers);
		title=(TextView) this.findViewById(R.id.admin_worker_dp_title);
		title.setText(department.getName());
		initAdapter();
		workerView.setAdapter(adapter);
	}

	/**
	 * 初始化列表内容信息
	 */
	private void initContent() {
		/**
		 * 没有做数据缓存，所以每次都要重新加载网络数据
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
						 * 加载成功则更新视图
						 * 失败则提示失败
						 */
						if(data==null){
							WorkerAdapter adapter=((WorkerAdapter)workerView.getAdapter());
							adapter.setData(data);
							adapter.notifyDataSetChanged();
						}else{
							Toast.makeText(WorkerAdminDPActivity.this, "信息加载失败：网络不给力", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		}, ThreadManager.DEFALSE_REQUEST_POOL);
	}
	
	/**
	 * 初始化列表适配器
	 */
	private void initAdapter(){

		adapter=new WorkerAdapter(getApplicationContext());
		adapter.setEditListener(new OnClickListener() {
			
			public void onClick(View v) {
				int position=(Integer) v.getTag();
				WorkerEntity worker=adapter.getData().get(position);
				/**
				 * 跳转到编辑员工的界面：
				 */
				Intent intent=new Intent(WorkerAdminDPActivity.this, AddOrEditWorker.class);
				intent.putExtra("worker", worker);
				startActivityForResult(intent, REQUEST_CODE_EDIT_WORKER);
//				Toast.makeText(getApplicationContext(), "跳转到编辑员工的界面：", Toast.LENGTH_SHORT).show();
			}
		});
		adapter.setDeleteListener(new OnClickListener() {
			
			public void onClick(View v) {
				int position=(Integer) v.getTag();
				WorkerEntity worker=adapter.getData().get(position);

				AdminOperate operate=new AdminOperateImpl();
				/**
				 * 执行员工信息删除操作
				 * 
				 * 删除成功则更新视图
				 * 失败则提示失败
				 */
				if(operate.deleteWorker(worker)){
					adapter.getData().remove(position);
					adapter.notifyDataSetChanged();
				}else{
					Toast.makeText(getApplicationContext(), "操作失败：网络不给力", Toast.LENGTH_SHORT).show();
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
						 * 如果已经不处于本部门，则将其移除部门员工列表
						 * 否则更新数据
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
	 * 返回事件处理
	 * @param v
	 */
	public void back(View v){
		ThreadManager.getInstance().clearALL();
		finish();
	}

	/**
	 * 添加员工事件
	 * @param v
	 */
	public void addWorker(View v){
		/**
		 * 跳转到添加员工的界面
		 */
		Intent intent=new Intent(WorkerAdminDPActivity.this, AddOrEditWorker.class);
		startActivityForResult(intent, REQUEST_CODE_ADD_WORKER);
//		Toast.makeText(getApplicationContext(), "跳转到添加员工的界面", Toast.LENGTH_SHORT).show();
	}
}
