package com.yizhixiaomifeng.hss.admin.bean.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.yizhixiaomifeng.domain.BusinessTypeEntity;
import com.yizhixiaomifeng.domain.DepartmenttEntity;
import com.yizhixiaomifeng.domain.WorkerEntity;
import com.yizhixiaomifeng.hss.admin.bean.OtherOperate;
import com.yizhixiaomifeng.hss.admin.bean.impl.helper.HTTPURL;
import com.yizhixiaomifeng.hss.admin.bean.impl.helper.JsonParseBusinessType;
import com.yizhixiaomifeng.hss.admin.bean.impl.helper.JsonParseDepartment;
import com.yizhixiaomifeng.hss.admin.bean.impl.helper.JsonParseWorker;
import com.yizhixiaomifeng.hss.util.HTTPRequest;

public class OtherOperateImpl implements OtherOperate {

	@Override
	public List<BusinessTypeEntity> loaderBusinessTypes() {

		List<BusinessTypeEntity> list=null;

		HashMap<String, String> paramsData=new HashMap<String,String>();
		
		String result=HTTPRequest.request(HTTPURL.LOAD_BUSINESS_TYPE_NAME, paramsData);
		if(!result.equals("«Î«Û“Ï≥£")&&!result.equals("«Î«Û ß∞‹")&&!result.equals("failed")){
			if(result.equals("error")){
				list=new ArrayList<BusinessTypeEntity>();
			}else{
				list=JsonParseBusinessType.parseBusinessTypeOfName(result);
			}
		}else{
			list=null;
		}
		return list;
	}

	@Override
	public List<DepartmenttEntity> loadDepartmentsAndDutyTypes() {

		List<DepartmenttEntity> list=null;

		HashMap<String, String> paramsData=new HashMap<String,String>();
		
		String result=HTTPRequest.request(HTTPURL.LOAD_DEPARTMENTS_AND_DUTY, paramsData);
		if(!result.equals("«Î«Û“Ï≥£")&&!result.equals("«Î«Û ß∞‹")&&!result.equals("failed")){
			if(result.equals("error")){
				list=new ArrayList<DepartmenttEntity>();
			}else{
				list=JsonParseDepartment.parseDeparmentSAndAllDutyType(result);
			}
		}else{
			list=null;
		}
		return list;
	}

	@Override
	public List<WorkerEntity> searchWorkerByName(String name) {
		List<WorkerEntity> list=null;

		HashMap<String, String> paramsData=new HashMap<String,String>();
		paramsData.put("name", name);
		
		String result=HTTPRequest.request(HTTPURL.LOAD_WORKER_BY_NAME, paramsData);
		if(!result.equals("«Î«Û“Ï≥£")&&!result.equals("«Î«Û ß∞‹")&&!result.equals("failed")){
			if(result.equals("error")){
				list=new ArrayList<WorkerEntity>();
			}else{
				list=JsonParseWorker.parseWorkers(result);
			}
		}else{
			list=null;
		}
		return list;
	}

}
