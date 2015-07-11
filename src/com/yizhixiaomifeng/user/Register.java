package com.yizhixiaomifeng.user;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.admin.bean.Client;
import com.yizhixiaomifeng.config.YzxmfConfig;
import com.yizhixiaomifeng.domain.WorkerEntity;
import com.yizhixiaomifeng.tools.ActivityCloser;
import com.yizhixiaomifeng.tools.CheckStatusLoader;
import com.yizhixiaomifeng.tools.ConnectWeb;
import com.yizhixiaomifeng.tools.UserRegister;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Register extends Activity
{
    private ImageView backImageView;
    private Spinner spinner;
    private EditText register_username;
    private EditText register_password;
    private EditText register_confirmPassword;
    private Button commit_registet_button;
    private WorkerEntity workerEntity;
    private String phone;
    private String password;
    private List<WorkerEntity>all_un_register_user=new ArrayList<WorkerEntity>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        
        ActivityCloser.activities.add(this);
        
        
        backImageView=(ImageView)findViewById(R.id.register_back);
        backImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Register.this,MainActivity.class);
				startActivity(intent);
			}
		});
        spinner = (Spinner)findViewById(R.id.register_userid);
        register_username=(EditText)findViewById(R.id.register_username);
        register_password=(EditText)findViewById(R.id.register_password);
        register_confirmPassword=(EditText)findViewById(R.id.register_confirmPassword);
        commit_registet_button=(Button)findViewById(R.id.commit_registet_button);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long id) {
				if(position!=0)
				{
					workerEntity = all_un_register_user.get(position-1);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
        //��ȡ����δע���Ա����Ϣ
        new GetAllUnRegisterUser().execute("");
        
        commit_registet_button.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
            	phone=register_username.getText().toString();
                password=register_password.getText().toString();
                final String confirmPass=register_confirmPassword.getText().toString();
                if(YzxmfConfig.isConnect(Register.this)==false)
                {
                    Toast.makeText(Register.this, "��������ʧ�ܣ���ȷ����������...",Toast.LENGTH_LONG).show();
                    return ;
                }
                if(!password.equals(confirmPass))
                {
                    Toast.makeText(getApplicationContext(), "���벻һ��...", Toast.LENGTH_LONG).show();
                    return ;
                }
                if (phone.equals("")||phone.equals("")||password==null||password==null) {
                    Toast.makeText(Register.this, "��������Ϣ",Toast.LENGTH_LONG).show();
                    
                }
                if(workerEntity==null){
            		Toast.makeText(Register.this, "��ѡ������",Toast.LENGTH_LONG).show();
            		return;
            	}
                else {
                	new UserRegister(Register.this,commit_registet_button).execute(""+workerEntity.getJobNum(),phone,password);
                }
                
            }
        });
        ImageView register_back= (ImageView)findViewById(R.id.register_back);
        register_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Register.this.finish();
			}
		});
    }
    
    class GetAllUnRegisterUser extends AsyncTask<String, Integer, String>{
    	public GetAllUnRegisterUser(){
    	}
    	@Override
    	protected String doInBackground(String... params) {
    		
    		/**
    		 * ȥ��̨��ȡ����
    		 */
    		String result = new ConnectWeb().getAllUserInfo();
    		return result;
    	}

    	@Override
    	protected void onCancelled() {
    		super.onCancelled();
    	}

    	@Override
    	protected void onPostExecute(String result) {
    		if(result!=null)
    		{
    			try {
    				JSONArray jsonArray = new JSONArray(result);
    				for(int i=0;i<jsonArray.length();i++){
    					JSONObject object = jsonArray.getJSONObject(i);
    					long id = object.getLong("id");
    					String name = object.getString("name");
    					WorkerEntity we =new WorkerEntity();
    					we.setJobNum(id);
    					we.setName(name);
    					all_un_register_user.add(we);
    				}
    				List<String> unregister_names = new ArrayList<String>();
    				unregister_names.add("��ѡ��ͻ�");
    				for(WorkerEntity we : all_un_register_user){
    					unregister_names.add(we.getName()+"("+we.getJobNum()+")");
    				}
    				if(spinner!=null){
    					//������
    			        ArrayAdapter<String> arr_adapter= new ArrayAdapter<String>(Register.this, android.R.layout.simple_spinner_item, unregister_names);
    			        //������ʽ
    			        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    			        //����������
    			        spinner.setAdapter(arr_adapter);
    				}
    				
    		        
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
	    		super.onPostExecute(result);
    		}
    	}

    	@Override
    	protected void onPreExecute() {
    		super.onPreExecute();
    	}

    	@Override
    	protected void onProgressUpdate(Integer... values) {
    		super.onProgressUpdate(values);
    	}
    	
    }
    
}
