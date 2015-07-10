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

import com.yizhixiaomifeng.admin.bean.Client;
import com.yizhixiaomifeng.admin.bean.News;
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
        	Log.e("checkUser: error", e.toString());
            return "error";
        }
    }
    
    /**
	 * ����Ա��¼�ж�
	 * @param userid
	 * @param password
	 * @return �ɹ����� "ok"  ʧ�ܷ��� "error"
	 */
    public String checkAdmin(String phone,String password,String type)
    {
    	try
        {
    		String target = URLConfig.checkAdmin;
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
            	Log.e("һֻС�۷�", "checkAdmin: error");
                return "error";
            }
            
        }catch(Exception e)
        {
        	Log.e("checkAdmin: error", e.toString());
            return "error";
        }
    }
    
    public String getAllUserInfo(){
    	try {
			String target = URLConfig.getAllUser;
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
			HttpPost httpRequest = new HttpPost(target);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			httpRequest.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
    	    HttpResponse httpResponse = httpClient.execute(httpRequest);
    	    if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
    	    	String result = EntityUtils.toString(httpResponse.getEntity());
    	    	return result;
    	    }else {
				return "error";
			}
    	} catch (Exception e) {
    		
    		Log.e("getAllUserInfo: error", e.toString());
            return "error";
		}
    }
    
    public String getAllUnRegisterUserInfo(){
    	try {
			String target = URLConfig.getUnRegisterUserNameAndIdInfo;
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
			HttpPost httpRequest = new HttpPost(target);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			httpRequest.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
    	    HttpResponse httpResponse = httpClient.execute(httpRequest);
    	    if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
    	    	String result = EntityUtils.toString(httpResponse.getEntity());
    	    	return result;
    	    }else {
				return "error";
			}
    	} catch (Exception e) {
    		
    		Log.e("getAllUserInfo: error", e.toString());
            return "error";
		}
    }
    
    
    public String getUserInfoByPhone(String phone){
    	try {
			String target = "";
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
			HttpPost httpRequest = new HttpPost(target);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("phone",phone));
			httpRequest.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
    	    HttpResponse httpResponse = httpClient.execute(httpRequest);
    	    if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
    	    	String result = EntityUtils.toString(httpResponse.getEntity());
    	    	return result;
    	    }else {
				return "error";
			}
    	} catch (Exception e) {
    		
    		Log.e("getUserInfoByPhone: error", e.toString());
            return "error";
		}
    }
    
    /**
     * ͨ���±��ȡ�û���Ϣ�����Խ�Լ��Դ
     * @param start �±�
     * @return
     */
    public String getAllClientDataByPhone(String phone){
    	try {
			String target = URLConfig.getAllClientByPhone;
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
			HttpPost httpRequest = new HttpPost(target);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("phone",phone));
			httpRequest.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
    	    HttpResponse httpResponse = httpClient.execute(httpRequest);
    	    if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
    	    	String result = EntityUtils.toString(httpResponse.getEntity());
    	    	return result;
    	    }else {
				return "error";
			}
    	} catch (Exception e) {
    		
    		Log.e("getAllClientData: error", e.toString());
            return "error";
		}
    }
    
    
    /**
     * ͨ���±��ȡ�û���Ϣ�����Խ�Լ��Դ
     * @param start �±�
     * @return
     */
    public String getClientData(int start){
    	try {
			String target = URLConfig.getClientByStart;
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
			HttpPost httpRequest = new HttpPost(target);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("start",""+start));
			httpRequest.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
    	    HttpResponse httpResponse = httpClient.execute(httpRequest);
    	    if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
    	    	String result = EntityUtils.toString(httpResponse.getEntity());
    	    	return result;
    	    }else {
				return "error";
			}
    	} catch (Exception e) {
    		
    		Log.e("getClientData: error", e.toString());
            return "error";
		}
    }
    public String getCheckStatusByUserAndClient(String user,String clientId){
    	try {
			String target = URLConfig.canCheckInandOut;
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
			HttpPost httpRequest = new HttpPost(target);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("phone",""+user));
			params.add(new BasicNameValuePair("clientId",""+clientId));
			httpRequest.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
    	    HttpResponse httpResponse = httpClient.execute(httpRequest);
    	    if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
    	    	String result = EntityUtils.toString(httpResponse.getEntity());
    	    	return result;
    	    }else {
				return "error";
			}
    	} catch (Exception e) {
    		
    		Log.e("getCheckStatusByUserAndClient: error", e.toString());
            return "error";
		}
    }
    
    /**
     * ����ͻ�
     * @param client
     * @return
     */
    public String saveClient(Client client){
    	try {
			String target = URLConfig.saveClient;
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
			HttpPost httpRequest = new HttpPost(target);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name",""+client.getName()));
			params.add(new BasicNameValuePair("projectName",""+client.getProjectName()));
			params.add(new BasicNameValuePair("address",""+client.getAddress()));
			params.add(new BasicNameValuePair("longitude",""+client.getLongitude()));
			params.add(new BasicNameValuePair("latitude",""+client.getLatitude()));
			params.add(new BasicNameValuePair("startTime",""+client.getStartTime()));
			params.add(new BasicNameValuePair("endTime",""+client.getEndTime()));
			httpRequest.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
    	    HttpResponse httpResponse = httpClient.execute(httpRequest);
    	    if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
    	    	String result = EntityUtils.toString(httpResponse.getEntity());
    	    	return result;
    	    }else {
				return "error";
			}
    	} catch (Exception e) {
    		
    		Log.e("saveClient: error", e.toString());
            return "error";
		}
    }
    
    /**
     * ���¿ͻ�
     * @param client
     * @return
     */
    public String updateClient(Client client){
    	try {
			String target = URLConfig.updateClient;
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
			HttpPost httpRequest = new HttpPost(target);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id",""+client.getId()));
			params.add(new BasicNameValuePair("name",""+client.getName()));
			params.add(new BasicNameValuePair("projectName",""+client.getProjectName()));
			params.add(new BasicNameValuePair("address",""+client.getAddress()));
			params.add(new BasicNameValuePair("longitude",""+client.getLongitude()));
			params.add(new BasicNameValuePair("latitude",""+client.getLatitude()));
			params.add(new BasicNameValuePair("startTime",""+client.getStartTime()));
			params.add(new BasicNameValuePair("endTime",""+client.getEndTime()));
			httpRequest.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
    	    HttpResponse httpResponse = httpClient.execute(httpRequest);
    	    if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
    	    	String result = EntityUtils.toString(httpResponse.getEntity());
    	    	return result;
    	    }else {
				return "error";
			}
    	} catch (Exception e) {
    		
    		Log.e("updateClient: error", e.toString());
            return "error";
		}
    }
    /**
     * ɾ���ͻ�
     * @param client
     * @return
     */
    public String deleteClient(Client client){
    	try {
			String target = URLConfig.deleteClient;
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
			HttpPost httpRequest = new HttpPost(target);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id",""+client.getId()));
			httpRequest.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
    	    HttpResponse httpResponse = httpClient.execute(httpRequest);
    	    if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
    	    	String result = EntityUtils.toString(httpResponse.getEntity());
    	    	return result;
    	    }else {
				return "error";
			}
    	} catch (Exception e) {
    		
    		Log.e("deleteClient: error", e.toString());
            return "error";
		}
    }
    
    /**
     * ͨ���±��ȡ��Ϣ��Ϣ�����Խ�Լ��Դ
     * @param start �±�
     * @return
     */
    public String getNewsData(int start,int status){
    	try {
			String target = URLConfig.getNewsByStartAndStatus;
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
			HttpPost httpRequest = new HttpPost(target);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("start",""+start));
			params.add(new BasicNameValuePair("status",""+status));
			httpRequest.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
    	    HttpResponse httpResponse = httpClient.execute(httpRequest);
    	    if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
    	    	String result = EntityUtils.toString(httpResponse.getEntity());
    	    	return result;
    	    }else {
				return "error";
			}
    	} catch (Exception e) {
    		
    		Log.e("getNewsData: error", e.toString());
            return "error";
		}
    }
    
    /**
     * ������Ϣ
     * @param news
     * @return
     */
    public String saveNews(News news){
    	try {
			String target = URLConfig.saveNews;
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
			HttpPost httpRequest = new HttpPost(target);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("title",""+news.getTitle()));
			params.add(new BasicNameValuePair("content",""+news.getContent()));
			params.add(new BasicNameValuePair("status",""+news.getStatus()));
			httpRequest.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
    	    HttpResponse httpResponse = httpClient.execute(httpRequest);
    	    if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
    	    	String result = EntityUtils.toString(httpResponse.getEntity());
    	    	return result;
    	    }else {
				return "error";
			}
    	} catch (Exception e) {
    		
    		Log.e("saveNews: error", e.toString());
            return "error";
		}
    }
    
    /**
     * ɾ����Ϣ
     * @param news
     * @return
     */
    public String deleteNews(News news){
    	try {
			String target = URLConfig.deleteNews;
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
			HttpPost httpRequest = new HttpPost(target);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id",""+news.getId()));
			httpRequest.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
    	    HttpResponse httpResponse = httpClient.execute(httpRequest);
    	    if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
    	    	String result = EntityUtils.toString(httpResponse.getEntity());
    	    	return result;
    	    }else {
				return "error";
			}
    	} catch (Exception e) {
    		
    		Log.e("deleteNews: error", e.toString());
            return "error";
		}
    }
    /**
     * ������Ϣ
     * @param news
     * @return
     */
    public String updateNews(News news){
    	try {
			String target = URLConfig.updateNews;
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
			HttpPost httpRequest = new HttpPost(target);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", ""+news.getId()));
			params.add(new BasicNameValuePair("title",""+news.getTitle()));
			params.add(new BasicNameValuePair("content",""+news.getContent()));
			params.add(new BasicNameValuePair("status",""+news.getStatus()));
			httpRequest.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
    	    HttpResponse httpResponse = httpClient.execute(httpRequest);
    	    if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
    	    	String result = EntityUtils.toString(httpResponse.getEntity());
    	    	return result;
    	    }else {
				return "error";
			}
    	} catch (Exception e) {
    		
    		Log.e("updateNews: error", e.toString());
            return "error";
		}
    }
    /**
     * ���ݿͻ�����ȡ�ͻ�������
     * @param name
     * @return json��ʽ�Ŀͻ�������Ϣ
     */
    public String getClientInfoByName(String name){
    	try {
			String target = "";
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
			HttpPost httpRequest = new HttpPost(target);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name",""+name));
			httpRequest.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
    	    HttpResponse httpResponse = httpClient.execute(httpRequest);
    	    if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
    	    	String result = EntityUtils.toString(httpResponse.getEntity());
    	    	return result;
    	    }else {
				return "error";
			}
    	} catch (Exception e) {
    		
    		Log.e("getClientInfoByName: error", e.toString());
            return "error";
		}
    }
    /**
     * ǩ��
     * @param phone �û��绰
     * @param clientId	�ͻ�ID
     * @param latitude	�û���ǰ����
     * @param longitute �û���ǰγ��
     * @param sceneUrl	ǩ���ֳ�ͼƬ·��
     * @param voiceUrl	ǩ���ֳ�¼��·��
     * @param distance	ǩ���ֳ�����Ŀ�ĵصľ���
     * @return
     */
    public String checkIn(String phone,long clientId,double latitude,double longitute,String sceneUrl,String voiceUrl,double distance){
    	try {
			String target = URLConfig.checkin;
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
			HttpPost httpRequest = new HttpPost(target);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("phone",phone));
			params.add(new BasicNameValuePair("clientId",""+clientId));
			params.add(new BasicNameValuePair("latitude",""+latitude));
			params.add(new BasicNameValuePair("longitude",""+longitute));
			params.add(new BasicNameValuePair("sceneUrl",""+sceneUrl));
			params.add(new BasicNameValuePair("voiceUrl",""+voiceUrl));
			params.add(new BasicNameValuePair("distance", ""+distance));  //��λ�õ�״̬������̨
			httpRequest.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
    	    HttpResponse httpResponse = httpClient.execute(httpRequest);
    	    if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
    	    	String result = EntityUtils.toString(httpResponse.getEntity());
    	    	return result;
    	    }else {
				return "error";
			}
    	} catch (Exception e) {
    		
    		Log.e("checkIn: error", e.toString());
            return "error";
		}
    }
    
    public String checkOut(String phone,double latitude,double longitute,String voiceUrl,String positionStatus){
    	try {
			String target = URLConfig.checkin;
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
			HttpPost httpRequest = new HttpPost(target);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("phone",phone));
			params.add(new BasicNameValuePair("latitude",""+latitude));
			params.add(new BasicNameValuePair("longitude",""+longitute));
			params.add(new BasicNameValuePair("voiceUrl",""+voiceUrl));
			params.add(new BasicNameValuePair("positionStatus", ""+positionStatus));  //��λ�õ�״̬������̨
			httpRequest.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
    	    HttpResponse httpResponse = httpClient.execute(httpRequest);
    	    if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
    	    	String result = EntityUtils.toString(httpResponse.getEntity());
    	    	return result;
    	    }else {
				return "error";
			}
    	} catch (Exception e) {
    		
    		Log.e("checkOut: error", e.toString());
            return "error";
		}
    }
    
    
    public String saveArrangement(String json){
    	try {
			String target = URLConfig.checkin;
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
			HttpPost httpRequest = new HttpPost(target);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("arrange",json));
			httpRequest.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
    	    HttpResponse httpResponse = httpClient.execute(httpRequest);
    	    if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
    	    	String result = EntityUtils.toString(httpResponse.getEntity());
    	    	return result;
    	    }else {
				return "error";
			}
    	} catch (Exception e) {
    		
    		Log.e("saveArrangement: error", e.toString());
            return "error";
		}
    }
    
    public String getAttendanceDataByDate(long date){
    	try {
			String target = URLConfig.getAttendanceDataByDate;
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
			HttpPost httpRequest = new HttpPost(target);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("date",""+date));
			httpRequest.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
    	    HttpResponse httpResponse = httpClient.execute(httpRequest);
    	    if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
    	    	String result = EntityUtils.toString(httpResponse.getEntity());
    	    	return result;
    	    }else {
				return "error";
			}
    	} catch (Exception e) {
    		
    		Log.e("getAttendanceDataByDate: error", e.toString());
            return "error";
		}
    }
    
    public String getAttendanceDataByDate(long startTime,long endTime){
    	try {
			String target = URLConfig.getAttendanceDataByDate;
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
			HttpPost httpRequest = new HttpPost(target);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("startTime",""+startTime));
			params.add(new BasicNameValuePair("endTime",""+endTime));
			httpRequest.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
    	    HttpResponse httpResponse = httpClient.execute(httpRequest);
    	    if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
    	    	String result = EntityUtils.toString(httpResponse.getEntity());
    	    	return result;
    	    }else {
				return "error";
			}
    	} catch (Exception e) {
    		
    		Log.e("getAttendanceDataByDate: error", e.toString());
            return "error";
		}
    }
    
    public String getAttendanceDataByDateAndName(String name , long startTime,long endTime){
    	try {
			String target = URLConfig.getAttendanceDataByDate;
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
			HttpPost httpRequest = new HttpPost(target);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name",""+name));
			params.add(new BasicNameValuePair("startTime",""+startTime));
			params.add(new BasicNameValuePair("endTime",""+endTime));
			httpRequest.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
    	    HttpResponse httpResponse = httpClient.execute(httpRequest);
    	    if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
    	    	String result = EntityUtils.toString(httpResponse.getEntity());
    	    	return result;
    	    }else {
				return "error";
			}
    	} catch (Exception e) {
    		
    		Log.e("getAttendanceDataByDateAndName: error", e.toString());
            return "error";
		}
    }
    
    public String registerUser(long id,String phone,String password){
    	try {
			String target = URLConfig.getAttendanceDataByDate;
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
			HttpPost httpRequest = new HttpPost(target);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id",""+id));
			params.add(new BasicNameValuePair("phone",""+phone));
			params.add(new BasicNameValuePair("password",""+password));
			httpRequest.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
    	    HttpResponse httpResponse = httpClient.execute(httpRequest);
    	    if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
    	    	String result = EntityUtils.toString(httpResponse.getEntity());
    	    	return result;
    	    }else {
				return "error";
			}
    	} catch (Exception e) {
    		
    		Log.e("registerUser: error", e.toString());
            return "error";
		}
    }
    
}
