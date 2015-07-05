package com.yizhixiaomifeng.tools;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import com.yizhixiaomifeng.config.URLConfig;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectWeb {
	/**
	 * �û���¼�ж�
	 * @param userid
	 * @param password
	 * @return �ɹ����� "ok"  ʧ�ܷ��� "error"
	 */
    public String checkUser(String phone,String password,String type)
    {
    	try
        {
    		String target = URLConfig.checkUser;
    		//ͨ��httpClient�����ʺ�̨
            HttpClient httpClient = new DefaultHttpClient();
            //����ʱ
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000); 
            //��ȡ��ʱ
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
            HttpPost httpRequest = new HttpPost(target);        //����������POST�ķ�ʽ����
            List<NameValuePair>params=new ArrayList<NameValuePair>(); //���ô������
           // params.add(new BasicNameValuePair("method", "checkUser"));  //����spring�е�checkUser����
            params.add(new BasicNameValuePair("phone",phone));      //USERID
            params.add(new BasicNameValuePair("password",password));	//password
            params.add(new BasicNameValuePair("type", type));
            httpRequest.setEntity(new UrlEncodedFormEntity(params, "utf-8")); //�����ַ�����
            HttpResponse httpResponse=httpClient.execute(httpRequest);     //����
            if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK)  //�ɹ�
            {
                String result=EntityUtils.toString(httpResponse.getEntity());  //��ȡ�Ż���Ϣ
                return result;
            }
            else {
            	Log.e("һֻС�۷�", "checkUser: error");
                return "error";
            }
            
        }catch(Exception e)
        {
        	
            return "error";
        }
    }
    
    
    /**
     * �ж������Ƿ�������
     * @param Context context
     * @return
     */
    public boolean isConnect(Context context)
    {
        try{
            ConnectivityManager cm=(ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
            if(cm!=null)
            {
                NetworkInfo info=cm.getActiveNetworkInfo();
                if(info!=null&&info.isConnected())
                {
                    if(info.getState()==NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }catch(Exception e)
        {
            return false;
        }
        return false;
    }
}
