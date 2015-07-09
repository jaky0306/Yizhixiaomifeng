package com.yizhixiaomifeng.tools;

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
/**
 * 用来与LeanCloud进行交换
 * @author Jaky
 *
 */
public class AvosTool {
	/**
	 * 保存username的头像到LeanCloud
	 * @param username
	 */
	public void saveHead(final String username){
		try{
			AVQuery<AVObject> query = new AVQuery<AVObject>("_File");
			query.whereEqualTo("name", username+"_head");
			query.deleteAll();
			AVFile file;
			try {
				file = AVFile.withAbsoluteLocalPath(username+"_head", "data/data/com.yizhixiaomifeng/files/head.jpg");
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
	 * 获取username 在LeanCloud的头像数据
	 * @param username
	 * @return
	 */
	public  byte[]  getHead(String username){
		byte []data = null;
		AVQuery<AVObject> query = new AVQuery<AVObject>("_File");
		query.whereEqualTo("name", username+"_head");
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
	 * 根据用户名和日期保存用户签到时的情景
	 * @param username
	 * @param date
	 */
	public void saveCheckInScene(String username,String date){
		try{
			AVFile file;
			file = AVFile.withAbsoluteLocalPath(username+date+"_checkin", "data/data/com.yizhixiaomifeng/files/checkinScene.jpg");
			file.save();
		}catch(Exception  e){
			log.e("saveCheckInScene error",""+e.toString());
		}
	}
	/**
	 * 根据用户名和日期保存用户签退时的情景
	 * @param username
	 * @param date
	 */
	public void saveCheckOutScene(String username,String date){
		try{
			AVFile file;
			file = AVFile.withAbsoluteLocalPath(username+date+"_checkout", "data/data/com.yizhixiaomifeng/files/checkoutScene.jpg");
			file.save();
		}catch(Exception  e){
			log.e("saveCheckOutScene error",""+e.toString());
		}
	}
}
