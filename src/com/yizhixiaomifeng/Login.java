package com.yizhixiaomifeng;
import com.avos.avoscloud.LogUtil.log;
import com.yizhixiaomifeng.tools.ActivityCloser;
import com.yizhixiaomifeng.tools.ConnectWeb;
import com.yizhixiaomifeng.tools.LocalStorage;

import android.app.Activity;
import android.app.backup.SharedPreferencesBackupHelper;
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
    private String type;  //记录登录类型
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
         * 处理登录事件
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
                if(new ConnectWeb().isConnect(Login.this)==false)
                {
                    Toast.makeText(Login.this, "网络连接失败，请确认网络连接...",Toast.LENGTH_LONG).show();
                    return ;
                }
                if (user.equals("")||pass.equals("")||user==null||pass==null) {
                    Toast.makeText(Login.this, "请输入信息",Toast.LENGTH_LONG).show();
                    
                }
                else {
                	
                	
//                    new Thread(new Runnable() {                       
//                        public void run()
//                        {
//                            result=new ConnectWeb().checkUser(user, pass,lt);
//                            Log.e("aaaaaaaaaaaa", "aa"+result);
////                            Message m = handler.obtainMessage();
////                            handler.sendMessage(m); 
//                        }
//                    }).start();
                	//把用户名，密码， 登录类型发给后台
                	new DataLoader().execute(user,pass,type);
                }
                
//                handler = new Handler(){
//                    public void handleMessage(Message msg) {
//                        // 提示登录中                        
//                        Login.setText("登录中...");
//                        Login.setEnabled(false);
//                    
//                        if(result){
//                            
//                            Intent intent=new Intent();
//                            intent.putExtra("username", user);
//                            intent.putExtra("password", pass);
//                            intent.setClass(Login.this, MainActivity.class);     
//                            startActivity(intent);
//                            
//                            //Intent intent=new Intent(Login.this, MainUI.class);  //登录成功就跳转
//                            
//                           // startActivity(intent);
//                            Login.this.finish();
//                            
//                        }else{
//                            Toast.makeText(getApplicationContext(), "登录信息有误!",Toast.LENGTH_LONG).show();
//                            Login.setText("登录");
//                            Login.setEnabled(true);
//                            
//                        }
//                        
//                        super.handleMessage(msg);
//                    }
//                };
                
            }
        });
        
      /**
       *   为注册文字绑定链接
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
     * 用来访问后台获取数据
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
    	protected void onPostExecute(String result) {
    		//Log.e("result", "aa"+result);
    		if(result.trim().equals("ok")){
    			
    			/**
    			 * 登录成功后把登录信息保存到SharedPreferences
    			 */
    			LocalStorage ls = new LocalStorage(Login.this);
    			ls.putString("username", user);
    			ls.putString("type", type);
    			ls.commitEditor();
    			//登录成功后进行Activity跳转
    			Intent intent=new Intent();
                intent.setClass(Login.this, MainActivity.class);     
                startActivity(intent);
                Login.this.finish();
    		}else{
    			Toast.makeText(getApplicationContext(), "登录信息有误!"+result,Toast.LENGTH_LONG).show();
                Login.setText("登录");
                Login.setEnabled(true);
    		}
    		super.onPostExecute(result);
    	}

    	@Override
    	protected void onPreExecute() {
    		Login.setText("登录中...");
            Login.setEnabled(false);
    		super.onPreExecute();
    	}

    	@Override
    	protected void onProgressUpdate(Integer... values) {
    		super.onProgressUpdate(values);
    	}
    	
    }

}
