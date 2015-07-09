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
	 * ���������б�
	 * @param jsonString
	 * @return
	 */
	public static List<DepartmenttEntity> parseDeparment(String jsonString){
		List<DepartmenttEntity> data=new ArrayList<DepartmenttEntity>();
		/**
		 * ����json����
		 */
		try {
			DepartmenttEntity department;
			BusinessTypeEntity business;
			JSONObject root=new JSONObject(jsonString);

			JSONArray jsonArray = root.getJSONArray("departmentList");
			JSONObject jsonObj = null;
			JSONObject json_business;
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObj = jsonArray.getJSONObject(i);
				department = new DepartmenttEntity();
				department.setNumber(jsonObj.getInt("number"));
				department.setName(jsonObj.getString("name"));
				department.setWorkNumber(jsonObj.getInt("workerNumber"));
				/**
				 * ����ҵ�����
				 */
				json_business=jsonObj.getJSONObject("businessType");
				business=new BusinessTypeEntity();
				business.setBusinessId(json_business.getInt("id"));
				business.setName(json_business.getString("name"));
				
				department.setBusinessTypeEntity(business);
				data.add(department);
			}
		} catch (JSONException e) {
			Log.d("JsonParseDepartment_parseDeparment", "JSON��������");
			e.printStackTrace();
		}
		
		return data;
	}
	/**
	 * ���������б��������ְλ�б�
	 * @param jsonString
	 * @return
	 */
	public static List<DepartmenttEntity> parseDeparmentSAndAllDutyType(String jsonString){
		List<DepartmenttEntity> data=new ArrayList<DepartmenttEntity>();
		/**
		 * ����json����
		 */
		try {
			DepartmenttEntity department;
			BusinessTypeEntity business;
			DutyTypeEntity duty;
			JSONObject root=new JSONObject(jsonString);

			JSONArray jsonArray = root.getJSONArray("departmentList");
			JSONObject jsonObj = null;
			JSONObject json_business;
			JSONObject json_duty;
			JSONArray json_dutyList;
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObj = jsonArray.getJSONObject(i);
				department = new DepartmenttEntity();
				department.setNumber(jsonObj.getInt("number"));
				department.setName(jsonObj.getString("name"));
				department.setWorkNumber(jsonObj.getInt("workerNumber"));
				/**
				 * ����ҵ�����
				 */
				json_business=jsonObj.getJSONObject("businessType");
				business=new BusinessTypeEntity();
				business.setBusinessId(json_business.getInt("businessId"));
				business.setName(json_business.getString("name"));
				
				department.setBusinessTypeEntity(business);
				/**
				 * ����ҵ�������µ�ְλ����
				 */
				json_dutyList=json_business.getJSONArray("dutyTypeList");
				for(int j = 0; j < json_dutyList.length(); j++){
					json_duty=json_dutyList.getJSONObject(j);
					duty=new DutyTypeEntity();
					duty.setDutyId(json_duty.getInt("dutyId"));
					duty.setName(json_duty.getString("naem"));
					business.getDutyTypeEntities().add(duty);
				}
				data.add(department);
			}
		} catch (JSONException e) {
			Log.d("JsonParseDepartment_parseDeparmentSAndAllDutyType", "JSON��������");
			e.printStackTrace();
		}
		
		return data;
	}
}
