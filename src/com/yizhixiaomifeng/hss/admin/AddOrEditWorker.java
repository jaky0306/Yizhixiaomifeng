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
			titleView.setText("����Ա��");
			isAdd=true;
		}else{
			calendar.setTimeInMillis(worker.getEntryDate());
			titleView.setText("�༭Ա��");
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
				 * �ڸı䲿�ŵ�ѡ���ͬʱ��Ҳ�ı�ְλ���б�
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
						 * �����ǰ�Ǵ��ڱ༭�û��Ĳ�������ѡ��Ĳ�����Ա��ԭ�ȵ����ڵĲ���
						 * ��Ҫ��Ա��ԭ��ְλ����Ϊѡ��״̬
						 * ����Ĭ��ѡ�е�һ��
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
		 * û�������ݻ��棬����ÿ�ζ�Ҫ���¼�����������
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
						 * ���سɹ��������ͼ
						 * ʧ������ʾʧ��
						 */
						if(rs){
							departmentAdapter.notifyDataSetChanged();
							/**
							 * ����Ǳ༭������Ҫ��ԭ��Ա�������Ĳ�������Ϊѡ��״̬
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
							Toast.makeText(AddOrEditWorker.this, "�����б����ʧ�ܣ����粻����", Toast.LENGTH_SHORT).show();
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
	 * �����¼�����
	 * @param v
	 */
	public void back(View v){
		ThreadManager.getInstance().clearALL();
		setResult(RESULT_CANCELED);
		finish();
	}
	
	/**
	 * ������Ӳ����¼�
	 * @param v
	 */
	public void addDepartment(View v){
		/**
		 * ��Ӳ���
		 */
		Intent intent=new Intent(this, AddOrEditDeparmentActivity.class);
		startActivityForResult(intent, REQUEST_CODE_ADD_DEPARMENT);
	}
	
	/**
	 * �����������
	 * @param v
	 */
	public void save(final View v){

		
		if(nameView.getText()!=null&&nameView.getText().length()!=0){
			worker.setName(nameView.getText().toString());
		}else{
			Toast.makeText(getApplicationContext(), "���Ʋ���Ϊ��", Toast.LENGTH_SHORT).show();
			nameView.requestFocus();
			return;
		}
		if(departmentView.getSelectedItemPosition()!=0){
			worker.setDepartmenttEntity((DepartmenttEntity) departmentAdapter.getItem(departmentView.getSelectedItemPosition()));
		}else{
			Toast.makeText(getApplicationContext(), "��ѡ����", Toast.LENGTH_SHORT).show();
			return;
		}
		if(dutyView.getSelectedItemPosition()!=0){
			worker.setDutyTypeEntity((DutyTypeEntity) dutyAdapter.getItem(dutyView.getSelectedItemPosition()));
		}else{
			Toast.makeText(getApplicationContext(), "��ѡ��ְλ����", Toast.LENGTH_SHORT).show();
			return;
		}
		if(entryTimeView.getText()!=null&&entryTimeView.getText().length()!=0){
			worker.setEntryDate(TimeChangeUtil.getTime(entryTimeView.getText().toString()));
		}else{
			Toast.makeText(getApplicationContext(), "��ѡ����ְʱ��", Toast.LENGTH_SHORT).show();
			return;
		}
		if(basePayView.getText()!=null&&basePayView.getText().length()!=0){
			worker.setBasePay(Integer.valueOf(basePayView.getText().toString()));
		}else{
			Toast.makeText(getApplicationContext(), "�������ʲ���Ϊ��", Toast.LENGTH_SHORT).show();
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
						 * �����ɹ��򷵻���һ������
						 * ʧ������ʾʧ��
						 */
						if(rs){
							Intent intent=new Intent();
							intent.putExtra("worker", worker);
							setResult(RESULT_OK, intent);
							finish();
						}else{
							v.setClickable(true);
							Toast.makeText(AddOrEditWorker.this, "��Ϣ�ϴ�ʧ�ܣ����粻����", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		}, ThreadManager.DEFALSE_REQUEST_POOL);
	}
}
