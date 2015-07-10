package com.yizhixiaomifeng.hss.admin;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.domain.BusinessTypeEntity;
import com.yizhixiaomifeng.domain.DepartmenttEntity;
import com.yizhixiaomifeng.hss.admin.adapter.BusinessTypeOfSpinnerAdapter;
import com.yizhixiaomifeng.hss.admin.bean.AdminOperate;
import com.yizhixiaomifeng.hss.admin.bean.OtherOperate;
import com.yizhixiaomifeng.hss.admin.bean.impl.AdminOperateImpl;
import com.yizhixiaomifeng.hss.admin.bean.impl.OtherOperateImpl;
import com.yizhixiaomifeng.hss.util.ThreadManager;

public class AddOrEditDeparmentActivity extends Activity{
	
	private TextView titleView=null;
	private EditText nameView=null;
	private EditText phoneView=null;
	private Spinner businessTypeView=null;
	private BusinessTypeOfSpinnerAdapter adapter=null;
	private boolean isAdd=true;
	DepartmenttEntity department;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_department);
		
		
		initView();
		initContent();
	}
	
	private void initView(){
		titleView=(TextView) this.findViewById(R.id.add_department_title);
		nameView=(EditText) this.findViewById(R.id.add_department_name);
		phoneView=(EditText) this.findViewById(R.id.add_department_phone);
		businessTypeView=(Spinner) this.findViewById(R.id.add_department_business_type);
		adapter=new BusinessTypeOfSpinnerAdapter(this);
		businessTypeView.setAdapter(adapter);
	}
	
	private void initContent() {
		department=(DepartmenttEntity) getIntent().getSerializableExtra("department");
		if(department==null){
			department=new DepartmenttEntity();
			titleView.setText("��������");
			isAdd=true;
		}else{
			titleView.setText("�༭����");
			isAdd=false;
			nameView.setText(department.getName());
			phoneView.setText(department.getPhone());
			adapter.getData().add(department.getBusinessTypeEntity());
			adapter.notifyDataSetChanged();
			businessTypeView.setSelection(1);
		}
		/**
		 * û�������ݻ��棬����ÿ�ζ�Ҫ���¼�����������
		 */
		ThreadManager.getInstance().clearALL();
		ThreadManager.getInstance().submit(new Runnable() {
			
			boolean rs=false;
			@Override
			public void run() {
				OtherOperate operate=new OtherOperateImpl();
				List<BusinessTypeEntity> data=operate.loaderBusinessTypes();
				if(data!=null){
					adapter.getData().addAll(data);
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
							/**
							 * ����Ǳ༭������Ҫ����ǰ�Ĳ��ŵ�ҵ����������Ϊѡ��״̬
							 */
//							if(!isAdd){
//								for(int i=0;i<adapter.getData().size();i++){
//									if(adapter.getData().get(i).getBusinessId()
//											==department.getBusinessTypeEntity().getBusinessId()){
//										businessTypeView.setSelection(i+1);
//										break;
//									}
//								}
//							}
						}else{
							Toast.makeText(AddOrEditDeparmentActivity.this, "ҵ�����ͼ���ʧ�ܣ����粻����", Toast.LENGTH_SHORT).show();
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
		setResult(RESULT_CANCELED);
		finish();
	}
	
	/**
	 * �����������
	 * @param v
	 */
	public void save(final View v){
		if(nameView.getText()!=null&&nameView.getText().length()!=0){
			department.setName(nameView.getText().toString());
		}else{
			Toast.makeText(getApplicationContext(), "�������Ʋ���Ϊ��", Toast.LENGTH_SHORT).show();
			nameView.requestFocus();
			return;
		}
		if(phoneView.getText()!=null&&phoneView.getText().length()!=0){
			department.setPhone(phoneView.getText().toString());
		}
		if(businessTypeView.getSelectedItemPosition()!=0){
			department.setBusinessTypeEntity((BusinessTypeEntity) adapter.getItem(businessTypeView.getSelectedItemPosition()));
		}else{
			Toast.makeText(getApplicationContext(), "��ѡ��ҵ������", Toast.LENGTH_SHORT).show();
			return;
		}

		v.setClickable(false);
		ThreadManager.getInstance().clearALL();
		ThreadManager.getInstance().submit(new Runnable() {
			
			boolean rs=false;
			@Override
			public void run() {
				AdminOperate operate=new AdminOperateImpl();
				if (isAdd)
					rs = operate.addDepartment(department);
				else
					rs = operate.editDepartment(department);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						/**
						 * �����ɹ��򷵻���һ������
						 * ʧ������ʾʧ��
						 */
						if(rs){
							Intent intent=new Intent();
							intent.putExtra("department", department);
							setResult(RESULT_OK, intent);
							finish();
						}else{
							v.setClickable(true);
							Toast.makeText(AddOrEditDeparmentActivity.this, "��Ϣ�ϴ�ʧ�ܣ����粻����", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		}, ThreadManager.DEFALSE_REQUEST_POOL);
	}
}
