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
	 * 用户登录判断
	 * @param userid
	 * @param password
	 * @return 成功返回 "ok"  失败返回 "error"
	 */
    public String checkUser(String phone,String password,String type)
    {
    	try
        {
    		String target = URLConfig.checkUser;
    		//通过httpClient来访问后台
            HttpClient httpClient = new DefaultHttpClient();
            //请求超时
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000); 
            //读取超时
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
            HttpPost httpRequest = new HttpPost(target);        //声明请求。以POST的方式传输
            List<NameValuePair>params=new ArrayList<NameValuePair>(); //设置传输参数
           // params.add(new BasicNameValuePair("method", "checkUser"));  //调用spring中的checkUser方法
            params.add(new BasicNameValuePair("phone",phone));      //USERID
            params.add(new BasicNameValuePair("password",password));	//password
            params.add(new BasicNameValuePair("type", type));
            httpRequest.setEntity(new UrlEncodedFormEntity(params, "utf-8")); //参数字符编码
            HttpResponse httpResponse=httpClient.execute(httpRequest);     //链接
            if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK)  //成功
            {
                String result=EntityUtils.toString(httpResponse.getEntity());  //获取放回信息
                return result;
            }
            else {
            	Log.e("一只小蜜蜂", "checkUser: error");
                return "error";
            }
            
        }catch(Exception e)
        {
        	
            return "error";
        }
    }
    
    
    /**
     * 判断网络是否能连接
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
