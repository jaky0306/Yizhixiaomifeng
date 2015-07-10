package com.yizhixiaomifeng.user;
import org.json.JSONException;
import org.json.JSONObject;

import com.avos.avoscloud.LogUtil.log;
import com.baidu.platform.comapi.map.j;
import com.yizhixiaomifeng.R;
import com.yizhixiaomifeng.admin.AdminMainActivity;
import com.yizhixiaomifeng.config.YzxmfConfig;
import com.yizhixiaomifeng.tools.ActivityCloser;
import com.yizhixiaomifeng.tools.ConnectWeb;
import com.yizhixiaomifeng.tools.LocalStorage;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity
{
    Handler handler;
    private ImageView backImageView;
    private RadioGroup login_type;
    private TextView textView;
    private Button Login;
    private EditText username;
    private EditText password;
    private String user;
    private String pass;
    private String type;  //��¼��¼����
    private String result;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        ActivityCloser.activities.add(this);
        
        backImageView=(ImageView)findViewById(R.id.back);
        backImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Login.this,MainActivity.class);
				startActivity(intent);
			}
		});
        
        
        /**
         * �����¼�¼�
         */
        
        textView=(TextView)findViewById(R.id.register);
        Login=(Button)findViewById(R.id.loginIn);
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        
        login_type=(RadioGroup)findViewById(R.id.login_type);
        Login.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                user=username.getText().toString();
                pass=password.getText().toString();
                for(int i=0;i<login_type.getChildCount();i++)
				{
					RadioButton rb = (RadioButton)login_type.getChildAt(i);
					if(rb.isChecked())
					{
						type = rb.getText().toString().trim();
						break;
					}
				}
                if(YzxmfConfig.isConnect(Login.this)==false)
                {
                    Toast.makeText(Login.this, "��������ʧ�ܣ���ȷ����������...",Toast.LENGTH_LONG).show();
                    return ;
                }
                if (user.equals("")||pass.equals("")||user==null||pass==null) {
                    Toast.makeText(Login.this, "��������Ϣ",Toast.LENGTH_LONG).show();
                    
                }
                else {
                	new DataLoader().execute(user,pass,type);
                }
                
            }
        });
        
      /**
       *   Ϊע�����ְ�����
       */
        
        SpannableString sp = new SpannableString(textView.getText());
        final Intent intent = new Intent();
        intent.setClass(Login.this, Register.class);
        sp.setSpan(
                new IntentSpan(
                    new OnClickListener()
                    {  
                            public void onClick(View view)
                            {  
                                startActivity(intent);  
                            }  
    
                    }
                ),
                6,
                8,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
       );
       textView.setText(sp);  
       textView.setMovementMethod(LinkMovementMethod.getInstance());  
         
    }
    public class IntentSpan extends ClickableSpan {  
        
        
        private final OnClickListener listener;  

        public IntentSpan(View.OnClickListener listener) {  
            this.listener = listener;  
        }  

        @Override  
        public void onClick(View view) {  
            listener.onClick(view);  
        }  
    }  

    /**
     * �������ʺ�̨��ȡ����
     * @author Jaky
     *
     */
    class DataLoader extends AsyncTask<String, Integer, String>{
    	@Override
    	protected String doInBackground(String... params) {
    		result=new ConnectWeb().checkUser(params[0], params[1], params[2]);
    		//Log.e("result", "aa"+result);
    		return result;
    	}

    	@Override
    	protected void onCancelled() {
    		super.onCancelled();
    	}

    	@Override
    	protected void onPostExecute(String result){
    		//Log.e("result", "aa"+result);
    		if(!result.trim().equals("error")){
    			try {
    				if(type.equals("Ա��")){
    					String userInfo = result.trim();
    					log.e("userInfo",""+userInfo);
    					//����json��ʽ������
    					JSONObject jsonObject = new JSONObject(userInfo);
    					String name=jsonObject.getString("name");
    					String duty = jsonObject.getString("duty");
    					String department = jsonObject.getString("department");
    					/**
    	    			 * ��¼�ɹ�����û�������Ϣ���浽SharedPreferences
    	    			 */
    	    			LocalStorage ls = new LocalStorage(Login.this);
    	    			ls.putString("username", user);
    	    			ls.putString("type", "staff");
    	    			ls.putString("name", name);
    	    			ls.putString("duty", duty);
    	    			ls.putString("department", department);
    	    			ls.commitEditor();
    	    			//��¼�ɹ������Activity��ת
    	    			Intent intent=new Intent();
    	                intent.setClass(Login.this, MainActivity.class);     
    	                startActivity(intent);
    	                Login.this.finish();
    				}else if(type.equals("����Ա"))
    				{
    					String adminInfo = result.trim();
    					Log.e("admininfo", adminInfo);
    					//����json��ʽ������
    					JSONObject jsonObject = new JSONObject(adminInfo);
    					//int id = jsonObject.getInt("id");
    					String username = jsonObject.getString("username");
    					String name=jsonObject.getString("realname");
    					/**
    	    			 * ��¼�ɹ�����û�������Ϣ���浽SharedPreferences
    	    			 */
    	    			LocalStorage ls = new LocalStorage(Login.this);
    	    			ls.putString("username",user); //�Ե�¼ʱ����û���Ϊ��
    	    			ls.putString("type", "admin");
    	    			ls.putString("name", name);
    	    			ls.commitEditor();
    					//��¼�ɹ������Activity��ת
    	    			Intent intent=new Intent();
    	                intent.setClass(Login.this, AdminMainActivity.class);     
    	                startActivity(intent);
    	                Login.this.finish();
    				}
					
    			} catch (Exception e) {
					e.printStackTrace();
				}
    			
    		}else{
    			Toast.makeText(getApplicationContext(), "��¼ʧ��!",Toast.LENGTH_LONG).show();
                Login.setText("��¼");
                Login.setEnabled(true);
    		}
    		super.onPostExecute(result);
    	}

    	@Override
    	protected void onPreExecute() {
    		Login.setText("��¼��...");
            Login.setEnabled(false);
    		super.onPreExecute();
    	}

    	@Override
    	protected void onProgressUpdate(Integer... values) {
    		super.onProgressUpdate(values);
    	}
    	
    }

}
