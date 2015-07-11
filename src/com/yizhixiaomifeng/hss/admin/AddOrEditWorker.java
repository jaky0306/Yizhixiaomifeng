package com.yizhixiaomifeng.hss.admin;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.LogUtil.log;
import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.domain.DepartmenttEntity;
import com.yizhixiaomifeng.domain.DutyTypeEntity;
import com.yizhixiaomifeng.domain.WorkerEntity;
import com.yizhixiaomifeng.hss.admin.adapter.DepartmentOfSpinnerAdapter;
import com.yizhixiaomifeng.hss.admin.adapter.DutyTypeOfSpinnerAdapter;
import com.yizhixiaomifeng.hss.admin.bean.AdminOperate;
import com.yizhixiaomifeng.hss.admin.bean.OtherOperate;
import com.yizhixiaomifeng.hss.admin.bean.impl.AdminOperateImpl;
import com.yizhixiaomifeng.hss.admin.bean.impl.OtherOperateImpl;
import com.yizhixiaomifeng.hss.util.ThreadManager;
import com.yizhixiaomifeng.hss.util.TimeChangeUtil;

public class AddOrEditWorker extends Activity{

	public final static int REQUEST_CODE_ADD_DEPARMENT=11001;
	
	private TextView titleView=null;
	private EditText nameView;
	private EditText entryTimeView;
	private EditText basePayView;
	private Spinner departmentView;
	private Spinner dutyView;
	
	private DepartmentOfSpinnerAdapter departmentAdapter;
	private DutyTypeOfSpinnerAdapter dutyAdapter;
	
	private WorkerEntity worker=null;
	private boolean isAdd=false;
	private Calendar calendar = Calendar.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_worker);
		

		initView();
		initContent();
	}

	private void initView() {
		
		titleView=(TextView) this.findViewById(R.id.add_worker_title);
		nameView=(EditText) this.findViewById(R.id.add_worker_name);
		entryTimeView=(EditText) this.findViewById(R.id.add_worker_entrytime);
		basePayView=(EditText) this.findViewById(R.id.add_worker_basepay);
		
		entryTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(AddOrEditWorker.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    	calendar.set(year, monthOfYear, dayOfMonth);
                        entryTimeView.setText(DateFormat.format("yyy-MM-dd", calendar));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
		
		departmentView=(Spinner) this.findViewById(R.id.add_worker_department);
		dutyView=(Spinner) this.findViewById(R.id.add_worker_duty_type);
		
		departmentAdapter=new DepartmentOfSpinnerAdapter(this);
		dutyAdapter=new DutyTypeOfSpinnerAdapter(this);
		
		departmentView.setAdapter(departmentAdapter);
		dutyView.setAdapter(dutyAdapter);
	}

	private void initContent() {

		worker=(WorkerEntity) getIntent().getSerializableExtra("worker");
		if(worker==null){
			worker=new WorkerEntity();
			worker.setDepartmenttEntity((DepartmenttEntity) getIntent().getSerializableExtra("department"));
			titleView.setText("新增员工");
			isAdd=true;
		}else{
			calendar.setTimeInMillis(worker.getEntryDate());
			titleView.setText("编辑员工");
			isAdd=false;
			nameView.setText(worker.getName());
			entryTimeView.setText(TimeChangeUtil.getTimeString(worker.getEntryDate()));
			basePayView.setText(worker.getBasePay()+"");
			
			dutyAdapter.getData().add(worker.getDutyTypeEntity());
			dutyAdapter.notifyDataSetChanged();
			dutyView.setSelection(1);
		}

		departmentAdapter.getData().add(worker.getDepartmenttEntity());
		departmentAdapter.notifyDataSetChanged();
		departmentView.setSelection(1);
		
		departmentView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> spinner, View v,
					int position, long id) {
				/**
				 * 在改变部门的选择的同时，也改变职位的列表
				 */
				if(position==0){
					dutyAdapter.getData().clear();
					dutyAdapter.notifyDataSetChanged();
				}else{
//					Log.e("aaaaaaaaaaa", "---====="+position);
//					Log.e("bbbbbbbbb", "==---===="+departmentAdapter.getData().get(position-1));
//					log.e("","======="+departmentAdapter.getData().get(position-1).getBusinessTypeEntity());
//					log.e("","======="+departmentAdapter.getData().get(position-1).getBusinessTypeEntity().getDutyTypeEntities().size());
					dutyAdapter.setData(departmentAdapter.getData().get(position-1).getBusinessTypeEntity().getDutyTypeEntities());
					dutyAdapter.notifyDataSetChanged();

					if(!isAdd&&departmentAdapter.getData().get(position-1).getNumber()==worker.getDepartmenttEntity().getNumber()){
						/**
						 * 如果当前是处于编辑用户的操做，且选择的部门是员工原先的所在的部门
						 * 则要把员工原本职位设置为选中状态
						 * 否则默认选中第一项
						 */
						for(int i=0;i<dutyAdapter.getData().size();i++){
							if(dutyAdapter.getData().get(i).getDutyId()
									==worker.getDutyTypeEntity().getDutyId()){
								dutyView.setSelection(i+1);
								break;
							}
						}
					}else
						dutyView.setSelection(0);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		/**
		 * 没有做数据缓存，所以每次都要重新加载网络数据
		 */
		ThreadManager.getInstance().clearALL();
		ThreadManager.getInstance().submit(new Runnable() {
			
			boolean rs=false;
			@Override
			public void run() {
				OtherOperate operate=new OtherOperateImpl();
				List<DepartmenttEntity> data=operate.loadDepartmentsAndDutyTypes();
				if(data!=null){
					departmentAdapter.getData().clear();
					departmentAdapter.setData(data);
					dutyAdapter.getData().clear();
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
							departmentAdapter.notifyDataSetChanged();
							/**
							 * 如果是编辑，则需要将原本员工所处的部门设置为选中状态
							 */
							if(!isAdd){
								for(int i=0;i<departmentAdapter.getData().size();i++){
									if(departmentAdapter.getData().get(i).getNumber()
											==worker.getDepartmenttEntity().getNumber()){
										departmentView.setSelection(i+1);
										break;
									}
								}
							}
						}else{
							Toast.makeText(AddOrEditWorker.this, "部门列表加载失败：网络不给力", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		}, ThreadManager.DEFALSE_REQUEST_POOL);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==REQUEST_CODE_ADD_DEPARMENT){
			if(resultCode==RESULT_OK){
				departmentAdapter.getData().add((DepartmenttEntity) data.getSerializableExtra("department"));
				departmentAdapter.notifyDataSetChanged();
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
		setResult(RESULT_CANCELED);
		finish();
	}
	
	/**
	 * 监听添加部门事件
	 * @param v
	 */
	public void addDepartment(View v){
		/**
		 * 添加部门
		 */
		Intent intent=new Intent(this, AddOrEditDeparmentActivity.class);
		startActivityForResult(intent, REQUEST_CODE_ADD_DEPARMENT);
	}
	
	/**
	 * 监听保存操作
	 * @param v
	 */
	public void save(final View v){

		
		if(nameView.getText()!=null&&nameView.getText().length()!=0){
			worker.setName(nameView.getText().toString());
		}else{
			Toast.makeText(getApplicationContext(), "名称不能为空", Toast.LENGTH_SHORT).show();
			nameView.requestFocus();
			return;
		}
		if(departmentView.getSelectedItemPosition()!=0){
			worker.setDepartmenttEntity((DepartmenttEntity) departmentAdapter.getItem(departmentView.getSelectedItemPosition()));
		}else{
			Toast.makeText(getApplicationContext(), "请选择部门", Toast.LENGTH_SHORT).show();
			return;
		}
		if(dutyView.getSelectedItemPosition()!=0){
			worker.setDutyTypeEntity((DutyTypeEntity) dutyAdapter.getItem(dutyView.getSelectedItemPosition()));
		}else{
			Toast.makeText(getApplicationContext(), "请选择职位类型", Toast.LENGTH_SHORT).show();
			return;
		}
		if(entryTimeView.getText()!=null&&entryTimeView.getText().length()!=0){
			worker.setEntryDate(TimeChangeUtil.getTime(entryTimeView.getText().toString()));
		}else{
			Toast.makeText(getApplicationContext(), "请选择入职时间", Toast.LENGTH_SHORT).show();
			return;
		}
		if(basePayView.getText()!=null&&basePayView.getText().length()!=0){
			worker.setBasePay(Integer.valueOf(basePayView.getText().toString()));
		}else{
			Toast.makeText(getApplicationContext(), "基本工资不能为空", Toast.LENGTH_SHORT).show();
			basePayView.requestFocus();
			return;
		}
		v.setClickable(false);
		ThreadManager.getInstance().clearALL();
		ThreadManager.getInstance().submit(new Runnable() {
			
			boolean rs=false;
			@Override
			public void run() {
				AdminOperate operate=new AdminOperateImpl();
				if(isAdd)
					rs=operate.addWorker(worker);
				else
					rs=operate.editWorker(worker);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						/**
						 * 操作成功则返回上一个界面
						 * 失败则提示失败
						 */
						if(rs){
							Intent intent=new Intent();
							intent.putExtra("worker", worker);
							setResult(RESULT_OK, intent);
							finish();
						}else{
							v.setClickable(true);
							Toast.makeText(AddOrEditWorker.this, "信息上传失败：网络不给力", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		}, ThreadManager.DEFALSE_REQUEST_POOL);
	}
}
