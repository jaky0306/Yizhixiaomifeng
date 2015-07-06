package com.yizhixiaomifeng;
import com.yizhixiaomifeng.config.YzxmfConfig;
import com.yizhixiaomifeng.tools.ActivityCloser;
import com.yizhixiaomifeng.tools.ConnectWeb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity
{
    Boolean result;
    Handler handler;
    private ImageView backImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        
        ActivityCloser.activities.add(this);
        
        backImageView=(ImageView)findViewById(R.id.back);
        backImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Register.this,MainActivity.class);
				startActivity(intent);
			}
		});
        final Button register=(Button)findViewById(R.id.ToRegister);
        final EditText username=(EditText)findViewById(R.id.register_username);
        final EditText password=(EditText)findViewById(R.id.register_password);
        final EditText confirmPassword=(EditText)findViewById(R.id.register_confirmPassword);
        register.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                final String user=username.getText().toString();
                final String pass=password.getText().toString();
                final String confirmPass=confirmPassword.getText().toString();
                if(YzxmfConfig.isConnect(Register.this)==false)
                {
                    Toast.makeText(Register.this, "网络连接失败，请确认网络连接...",Toast.LENGTH_LONG).show();
                    return ;
                }
                if(!pass.equals(confirmPass))
                {
                    Toast.makeText(getApplicationContext(), "密码有误...", Toast.LENGTH_LONG).show();
                    return ;
                }
                if (user.equals("")||pass.equals("")||user==null||pass==null||confirmPass.equals("")||confirmPass==null) {
                    Toast.makeText(Register.this, "请输入信息",Toast.LENGTH_LONG).show();
                    
                }
                else {
//                    new Thread(new Runnable() {
//                        
//                        public void run()
//                        {
//                            result=new ConnectWeb().addUser(user, pass);
//                            Message m = handler.obtainMessage();
//                            handler.sendMessage(m); 
//                        }
//                    }).start();
                }
                handler = new Handler(){
                    public void handleMessage(Message msg) {
                                          
                        register.setText("正在注册...");
                        register.setEnabled(false);
                        if(!result){
                            Toast.makeText(getApplicationContext(), "此用户名已存在..",
                                    Toast.LENGTH_LONG).show();
                            
                            register.setText("注册");
                            register.setEnabled(true);
                            
                        }else{
                            Toast.makeText(getApplicationContext(), "注册成功",
                                    Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(Register.this, Login.class);
                            startActivity(intent);
                            Register.this.finish();
                        }
                        
                        super.handleMessage(msg);
                    }
                };
            }
        });
    }
}
