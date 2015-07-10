package com.yizhixiaomifeng.hss.admin.bean.impl.helper;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yizhixiaomifeng.domain.BusinessTypeEntity;
import com.yizhixiaomifeng.domain.DepartmenttEntity;
import com.yizhixiaomifeng.domain.DutyTypeEntity;

public class JsonParseDepartment {
	/**
	 * 解析部门列表
	 * @param jsonString
	 * @return
	 */
	public static List<DepartmenttEntity> parseDeparment(String jsonString){
		List<DepartmenttEntity> data=new ArrayList<DepartmenttEntity>();
		/**
		 * 解析json数据
		 */
		try {
			DepartmenttEntity department;
			BusinessTypeEntity business;
//			JSONObject root=new JSONObject(jsonString);

//			JSONArray jsonArray = root.getJSONArray("departmentList");
			JSONArray jsonArray = new JSONArray(jsonString);
			JSONObject jsonObj = null;
			JSONObject json_business;
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObj = jsonArray.getJSONObject(i);
				department = new DepartmenttEntity();
				department.setNumber(jsonObj.getLong("number"));
				department.setName(jsonObj.getString("name"));
				department.setPhone(jsonObj.getString("phone"));
				department.setWorkNumber(jsonObj.getInt("workerNumber"));
				/**
				 * 解析业务分类
				 */
				json_business=jsonObj.getJSONObject("businessType");
				business=new BusinessTypeEntity();
				business.setBusinessId(json_business.getLong("businessTypeId"));
				business.setName(json_business.getString("name"));
				
				department.setBusinessTypeEntity(business);
				data.add(department);
			}
		} catch (JSONException e) {
			Log.d("JsonParseDepartment_parseDeparment", "JSON解析出错");
			Log.e("JsonParseDepartment_parseDeparment", jsonString);
			e.printStackTrace();
		}
		
		return data;
	}
	/**
	 * 解析部门列表及其包含的职位列表
	 * @param jsonString
	 * @return
	 */
	public static List<DepartmenttEntity> parseDeparmentSAndAllDutyType(String jsonString){
		List<DepartmenttEntity> data=new ArrayList<DepartmenttEntity>();
		Log.e("JsonParseDepartment_parseDeparmentSAndAllDutyType", jsonString);
		/**
		 * 解析json数据
		 */
		try {
			DepartmenttEntity department;
			BusinessTypeEntity business;
			DutyTypeEntity duty;
//			JSONObject root=new JSONObject(jsonString);
//
//			JSONArray jsonArray = root.getJSONArray("departmentList");
			JSONArray jsonArray = new JSONArray(jsonString);
			JSONObject jsonObj = null;
			JSONObject json_business;
			JSONObject json_duty;
			JSONArray json_dutyList;
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObj = jsonArray.getJSONObject(i);
				department = new DepartmenttEntity();
				department.setNumber(jsonObj.getLong("number"));
				department.setName(jsonObj.getString("name"));
				department.setPhone(jsonObj.getString("phone"));
				department.setWorkNumber(jsonObj.getInt("workerNumber"));
				/**
				 * 解析业务分类
				 */
				json_business=jsonObj.getJSONObject("businessType");
				business=new BusinessTypeEntity();
				business.setBusinessId(json_business.getLong("businessTypeId"));
				business.setName(json_business.getString("name"));
				
				department.setBusinessTypeEntity(business);
				/**
				 * 解析业务类型下的职位类型
				 */
				json_dutyList=json_business.getJSONArray("dutyTypeList");
				for(int j = 0; j < json_dutyList.length(); j++){
					json_duty=json_dutyList.getJSONObject(j);
					duty=new DutyTypeEntity();
					duty.setDutyId(json_duty.getLong("dutyId"));
					duty.setName(json_duty.getString("name"));
					business.getDutyTypeEntities().add(duty);
				}
				data.add(department);
			}
		} catch (JSONException e) {
			Log.d("JsonParseDepartment_parseDeparmentSAndAllDutyType", "JSON解析出错");
			Log.e("JsonParseDepartment_parseDeparmentSAndAllDutyType", jsonString);
			e.printStackTrace();
		}
		
		return data;
	}
}
