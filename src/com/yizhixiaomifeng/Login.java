package com.yizhixiaomifeng;
import com.yizhixiaomifeng.tools.ActivityCloser;
import com.yizhixiaomifeng.tools.ConnectWeb;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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
    private String lt;  //��¼��¼����
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
                final String user=username.getText().toString();
                final String pass=password.getText().toString();
                for(int i=0;i<login_type.getChildCount();i++)
				{
					RadioButton rb = (RadioButton)login_type.getChildAt(i);
					if(rb.isChecked())
					{
						lt = rb.getText().toString().trim();
						break;
					}
				}
                if(new ConnectWeb().isConnect(Login.this)==false)
                {
                    Toast.makeText(Login.this, "��������ʧ�ܣ���ȷ����������...",Toast.LENGTH_LONG).show();
                    return ;
                }
                if (user.equals("")||pass.equals("")||user==null||pass==null) {
                    Toast.makeText(Login.this, "��������Ϣ",Toast.LENGTH_LONG).show();
                    
                }
                else {
                	
                	
//                    new Thread(new Runnable() {
//                        
//                        public void run()
//                        {
//                            result=new ConnectWeb().checkUser(user, pass);
//                            Message m = handler.obtainMessage();
//                            handler.sendMessage(m); 
//                        }
//                    }).start();
                	new DataLoader().execute(user,pass,lt);
                }
                
//                handler = new Handler(){
//                    public void handleMessage(Message msg) {
//                        // ��ʾ��¼��                        
//                        Login.setText("��¼��...");
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
//                            //Intent intent=new Intent(Login.this, MainUI.class);  //��¼�ɹ�����ת
//                            
//                           // startActivity(intent);
//                            Login.this.finish();
//                            
//                        }else{
//                            Toast.makeText(getApplicationContext(), "��¼��Ϣ����!",Toast.LENGTH_LONG).show();
//                            Login.setText("��¼");
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
    		
    		return result;
    	}

    	@Override
    	protected void onCancelled() {
    		super.onCancelled();
    	}

    	@Override
    	protected void onPostExecute(String result) {
    		
    		if(result.trim().equals("ok")){
    			Intent intent=new Intent();
                intent.putExtra("username", username.getText().toString().trim());
                intent.setClass(Login.this, MainActivity.class);     
                startActivity(intent);
                Login.this.finish();
    		}else{
    			Toast.makeText(getApplicationContext(), "��¼��Ϣ����!",Toast.LENGTH_LONG).show();
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
