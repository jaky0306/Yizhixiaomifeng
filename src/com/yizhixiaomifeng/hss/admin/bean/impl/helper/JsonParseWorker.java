package com.yizhixiaomifeng.hss.admin.bean.impl.helper;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yizhixiaomifeng.domain.DepartmenttEntity;
import com.yizhixiaomifeng.domain.DutyTypeEntity;
import com.yizhixiaomifeng.domain.WorkerEntity;

public class JsonParseWorker {
	public static List<WorkerEntity> parseWorkers(String jsonString){
		List<WorkerEntity> data=new ArrayList<WorkerEntity>();
		/**
		 * 解析员工信息
		 */

		try {
			WorkerEntity worker;
			DepartmenttEntity department;
			DutyTypeEntity duty;
//			JSONObject root=new JSONObject(jsonString);
//
//			JSONArray jsonArray = root.getJSONArray("workerList");
			JSONArray jsonArray = new JSONArray(jsonString);
			JSONObject json_worker = null;
			JSONObject json_department = null;
			JSONObject json_duty = null;
			for (int i = 0; i < jsonArray.length(); i++) {
				json_worker = jsonArray.getJSONObject(i);
				worker=new WorkerEntity();
				worker.setJobNum(json_worker.getLong("jobNum"));
				worker.setName(json_worker.getString("name"));
				worker.setEntryDate(json_worker.getLong("entryDate"));
				worker.setBasePay(json_worker.getInt("basepay"));
				/**
				 * 解析部门
				 */
				department = new DepartmenttEntity();
				json_department=json_worker.getJSONObject("department");
				department.setNumber(json_department.getLong("number"));
				department.setName(json_department.getString("name"));
				department.setPhone(json_department.getString("phone"));
				department.setWorkNumber(json_department.getInt("workerNumber"));
				worker.setDepartmenttEntity(department);
				/**
				 * 解析职位类型
				 */
				duty=new DutyTypeEntity();
				json_duty=json_worker.getJSONObject("dutyType");
				duty.setDutyId(json_duty.getLong("dutyId"));
				duty.setName(json_duty.getString("name"));
				worker.setDutyTypeEntity(duty);
				data.add(worker);
			}
		} catch (JSONException e) {
			Log.d("JsonParseWorker_parseWorkers", "JSON解析出错");
			Log.d("JsonParseWorker_parseWorkers", jsonString);
			e.printStackTrace();
		}
		return data;
	}
}
