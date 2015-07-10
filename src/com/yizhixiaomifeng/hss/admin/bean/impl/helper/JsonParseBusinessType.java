package com.yizhixiaomifeng.hss.admin.bean.impl.helper;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yizhixiaomifeng.domain.BusinessTypeEntity;

public class JsonParseBusinessType {

	public static List<BusinessTypeEntity> parseBusinessTypeOfName(String jsonString){
		List<BusinessTypeEntity> data=new ArrayList<BusinessTypeEntity>();
		/**
		 * ����json����
		 */
		try {
			BusinessTypeEntity business;
//			JSONObject root=new JSONObject(jsonString);
//			JSONArray jsonArray = root.getJSONArray("businessTypeList");;
			JSONArray jsonArray = new JSONArray(jsonString);
			JSONObject jsonObj = null;
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObj = jsonArray.getJSONObject(i);
				business = new BusinessTypeEntity();
				business.setBusinessId(jsonObj.getLong("businessId"));
				business.setName(jsonObj.getString("name"));
				data.add(business);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.d("JsonParseBusinessType", "JSON��������");
			Log.e("JsonParseBusinessType", jsonString);
			return null;
		}
		return data;
	}
}
