package com.yizhixiaomifeng.hss.admin;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.domain.WorkerEntity;
import com.yizhixiaomifeng.hss.admin.adapter.WorkerAdapter;
import com.yizhixiaomifeng.hss.admin.bean.AdminOperate;
import com.yizhixiaomifeng.hss.admin.bean.OtherOperate;
import com.yizhixiaomifeng.hss.admin.bean.impl.AdminOperateImpl;
import com.yizhixiaomifeng.hss.admin.bean.impl.OtherOperateImpl;
import com.yizhixiaomifeng.hss.util.ThreadManager;

public class SearchWorkerActivity extends Activity{

	public final static int REQUEST_CODE_EDIT_WORKER=11110;
	private ListView workerView=null;
	private WorkerAdapter adapter;
	
	private RelativeLayout noDataView;
	private EditText inputView=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_worker);
		
		initView();
	}
	
	private void initView() {
		inputView=(EditText) this.findViewById(R.id.search_worker_input);
		workerView=(ListView) this.findViewById(R.id.search_worker_workers);
		noDataView=(RelativeLayout) this.findViewById(R.id.search_worker_not_data);
		initAdapter();
		workerView.setAdapter(adapter);
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
				Intent intent=new Intent(SearchWorkerActivity.this, AddOrEditWorker.class);
				intent.putExtra("worker", worker);
				startActivity(intent);
				finish();
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
		if(requestCode==REQUEST_CODE_EDIT_WORKER){
			if(resultCode==RESULT_OK){
				WorkerEntity worker=(WorkerEntity) data.getSerializableExtra("worker");
				WorkerEntity wk=null;
				for(int i=0;i<adapter.getData().size();i++){
					wk=adapter.getData().get(i);
					if(wk.getJobNum()==worker.getJobNum()){
						wk.setName(worker.getName());
						wk.setDutyTypeEntity(worker.getDutyTypeEntity());
						wk.setEntryDate(worker.getEntryDate());
						wk.setBasePay(worker.getBasePay());
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
	
	private String name;
	/**
	 * 查找监听处理
	 * @param v
	 */
	public void search(final View v){
		if(inputView.getText()!=null&&inputView.getText().length()!=0)
			name=inputView.getText().toString();
		else{
			inputView.requestFocus();
			Toast.makeText(getApplicationContext(), "输入的内容不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		v.setClickable(false);
		/**
		 * 执行查找任务
		 */
		ThreadManager.getInstance().clearALL();
		ThreadManager.getInstance().submit(new Runnable() {
			
			boolean rs=false;
			@Override
			public void run() {
				OtherOperate operate=new OtherOperateImpl();
				List<WorkerEntity> data=operate.searchWorkerByName(name);
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
						 * 加载成功则更新视图
						 * 失败则提示失败
						 */
						if(rs){
							if(adapter.getData().size()==0){
								noDataView.setVisibility(View.VISIBLE);
							}else{
								noDataView.setVisibility(View.GONE);
							}
							adapter.notifyDataSetChanged();
						}else{
							Toast.makeText(SearchWorkerActivity.this, "信息加载失败：网络不给力", Toast.LENGTH_SHORT).show();
						}

						v.setClickable(true);
					}
				});
			}
		}, ThreadManager.DEFALSE_REQUEST_POOL);
	}
}
