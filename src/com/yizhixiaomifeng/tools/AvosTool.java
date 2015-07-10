package com.yizhixiaomifeng.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.LogUtil.log;
import com.yizhixiaomifeng.config.ParameterConfig;
import com.yizhixiaomifeng.config.YzxmfConfig;
/**
 * ������LeanCloud���н���
 * @author Jaky
 *
 */
public class AvosTool {
	/**
	 * ����username��ͷ��LeanCloud
	 * @param username
	 */
	public void saveHead(final String username,String type){
		try{
			AVQuery<AVObject> query = new AVQuery<AVObject>("_File");
			query.whereEqualTo("name", type+"_"+username+"_head");
			query.deleteAll();
			AVFile file;
			try {
				file = AVFile.withAbsoluteLocalPath(type+"_"+username+"_head", "data/data/com.yizhixiaomifeng/files/head.jpg");
				file.save();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}catch(Exception  e){
			log.e("saveHead error",""+e.toString());
		}
	}
	/**
	 * ��ȡusername ��LeanCloud��ͷ������
	 * @param username
	 * @return
	 */
	public  byte[]  getHead(String username,String type){
		byte []data = null;
		AVQuery<AVObject> query = new AVQuery<AVObject>("_File");
		query.whereEqualTo("name", type+"_"+username+"_head");
		try {
			
			List<AVObject> avObjects=query.find();
			if(avObjects.size()>0){
				String url = avObjects.get(0).getString("url");
		        AVFile avFile = new AVFile("goal_head", url,null);
		        data = avFile.getData();
		        return data;
		        
			}else {
				return data;
			}
		} catch (AVException e) {
			e.printStackTrace();
			return data;
		}
	}
	/**
	 * �����û��������ڱ����û�ǩ��ʱ���龰
	 * @param username �û���(�ֻ���)
	 * @param date ��new Date().toString()��
	 */
	public boolean saveCheckInScene(String username,String type,String date){
		File existfile = new File("data/data/com.yizhixiaomifeng/files/checkinScene.jpg");
		if(!existfile.exists()){
			return false;
		}
		try{
			AVQuery<AVObject> query = new AVQuery<AVObject>("_File");
			query.whereEqualTo("name", type+"_"+username+"_"+date+"_checkin_scene");
			query.deleteAll();
			AVFile file=null;
			file = AVFile.withAbsoluteLocalPath(type+"_"+username+"_"+date+"_checkin_scene", "data/data/com.yizhixiaomifeng/files/checkinScene.jpg");
			file.save();
			return true;
		}catch(Exception  e){
			
			log.e("saveCheckInScene error",""+e.toString());
			return false;
		}
	}
	/**
	 * ��ȡǩ���ֳ���URL
	 * @param username �û���(�ֻ���)
	 * @param type �û�����
	 * @param date	���ڣ�new Date().toString()��
	 * @return URL
	 */
	public String getCheckInSceneUrl(String username,String type,String date){
		AVQuery<AVObject> query = new AVQuery<AVObject>("_File");
		query.whereEqualTo("name",type+"_"+username+"_"+date+"_checkin_scene");
		try {
			
			List<AVObject> avObjects=query.find();
			if(avObjects.size()>0){
				String url = avObjects.get(0).getString("url");
		        return url;
		        
			}else {
				return null;
			}
		} catch (AVException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * ����ǩ����¼��
	 * @param username �û���(�ֻ���)
	 * @param type �û�����
	 * @param date	���ڣ�new Date().toString()��
	 */
	public boolean saveCheckInVoice(String username,String type,String date){
		File existfile = new File(YzxmfConfig.voicesrc);
		if(!existfile.exists()){
			return false;
		}
		try{
			AVQuery<AVObject> query = new AVQuery<AVObject>("_File");
			query.whereEqualTo("name", type+"_"+username+"_"+date+"_checkin_voice");
			query.deleteAll();
			AVFile file;
			file = AVFile.withAbsoluteLocalPath(type+"_"+username+"_"+date+"_checkin_voice", YzxmfConfig.voicesrc);
			file.save();
			return true;
		}catch(Exception  e){
			log.e("saveCheckInVoice error",""+e.toString());
			return false;
		}
	}
	/**
	 * ����ǩ�˵�¼��
	 * @param username �û���(�ֻ���)
	 * @param type �û�����
	 * @param date	���ڣ�new Date().toString()��
	 */
	public boolean saveCheckOutVoice(String username,String type,String date){
		File existfile = new File(YzxmfConfig.voicesrc);
		if(!existfile.exists()){
			return false;
		}
		try{
			AVQuery<AVObject> query = new AVQuery<AVObject>("_File");
			query.whereEqualTo("name", type+"_"+username+"_"+date+"_checkout_voice");
			query.deleteAll();
			AVFile file;
			file = AVFile.withAbsoluteLocalPath(type+"_"+username+"_"+date+"_checkout_voice", YzxmfConfig.voicesrc);
			file.save();
			return true;
		}catch(Exception  e){
			log.e("saveCheckOutVoice error",""+e.toString());
			return false;
		}
	}
	/**
	 * ��ȡǩ����¼����URL
	 * @param username �û���(�ֻ���)
	 * @param type �û�����
	 * @param date	���ڣ�new Date().toString()��
	 * @return URL
	 */
	public String getCheckInVoiceUrl(String username,String type,String date){
		AVQuery<AVObject> query = new AVQuery<AVObject>("_File");
		query.whereEqualTo("name",type+"_"+username+"_"+date+"_checkin_voice");
		try {
			
			List<AVObject> avObjects=query.find();
			if(avObjects.size()>0){
				String url = avObjects.get(0).getString("url");
		        return url;
		        
			}else {
				return null;
			}
		} catch (AVException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * ��ȡǩ�˵�¼����URL
	 * @param username �û���(�ֻ���)
	 * @param type �û�����
	 * @param date	���ڣ�new Date().toString()��
	 * @return URL
	 */
	public String getCheckOutVoiceUrl(String username,String type,String date){
		AVQuery<AVObject> query = new AVQuery<AVObject>("_File");
		query.whereEqualTo("name",type+"_"+username+"_"+date+"_checkout_voice");
		try {
			
			List<AVObject> avObjects=query.find();
			if(avObjects.size()>0){
				String url = avObjects.get(0).getString("url");
		        return url;
		        
			}else {
				return null;
			}
		} catch (AVException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public  byte[] getImgData(String url){
        try {
        	AVFile avFile = new AVFile("goal_img", url,null);
			return avFile.getData();
		} catch (AVException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
