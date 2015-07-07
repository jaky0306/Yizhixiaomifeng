package com.yizhixiaomifeng.hss.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;


public class HTTPRequest {

	public static String SESSION_ID = null;
	private static boolean reLogin=false; 
	
	public static String request(String url,
			HashMap<String, String> paramsData) {
		Log.d("HTTPRequest", "-----------" + url + "--------------");
		String result = "�����쳣";
		HttpClient http = HttpConnector.getInstance().getHttpClient();

		HttpPost post = new HttpPost(url);
		// ��һ��һ���ǻ�δ����ֵ������ֵ��SessionId����������
		if (null != SESSION_ID) {
			post.setHeader("Cookie", "JSPSESSID=" + SESSION_ID);
			Log.d("HTTP", "cookie->sessionID��" + SESSION_ID);
		} else {
			Log.d("HTTP", "û��sessionID");
		}
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("param", "post"));

		if (paramsData != null) {
			Iterator<String> iter = paramsData.keySet().iterator();
			String key = null;
			String value = null;
			while (iter.hasNext()) {
				key = iter.next();
				value = paramsData.get(key);
				params.add(new BasicNameValuePair(key, value));
			}
		}
		try {
			post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse response = http.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity(), "utf-8");
				CookieStore mCookieStore = ((DefaultHttpClient) http)
						.getCookieStore();
				List<Cookie> cookies = mCookieStore.getCookies();
				for (int i = 0; i < cookies.size(); i++) {
					// �����Ƕ�ȡCookie['JSPSESSID']��ֵ���ھ�̬�����У���֤ÿ�ζ���ͬһ��ֵ
					if ("JSESSIONID".equals(cookies.get(i).getName())) {
						SESSION_ID = cookies.get(i).getValue();
						Log.d("HTTP_send()", "sessionID:" + SESSION_ID);
						break;
					}
					Log.e("HTTP_send()", cookies.get(i).getName()
							+ "----vaulue----" + cookies.get(i).getValue());
				}
			} else
				result = "����ʧ��";
		} catch (Exception e) {
			e.printStackTrace();
		}
		//�����ص�¼����
		if(result.equals("unLogin")&&!reLogin&&autoLogin!=null){
			reLogin=true;
			
			if (autoLogin.autoLogin()) {
				result=request(url,paramsData);
			}else{
				result="����ʧ��";
			}
		}else if(result.equals("unLogin")){
			result="����ʧ��";
		}
		reLogin=false;
		return result;
	}
	
	public static AutoLogin autoLogin=null;
	
	public interface AutoLogin{
		public boolean autoLogin();
	}
}
