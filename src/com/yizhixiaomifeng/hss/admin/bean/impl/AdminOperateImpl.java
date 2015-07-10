package com.yizhixiaomifeng.hss.admin.bean.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.yizhixiaomifeng.domain.DepartmenttEntity;
import com.yizhixiaomifeng.domain.WorkerEntity;
import com.yizhixiaomifeng.hss.admin.bean.AdminOperate;
import com.yizhixiaomifeng.hss.admin.bean.impl.helper.HTTPURL;
import com.yizhixiaomifeng.hss.admin.bean.impl.helper.JsonParseDepartment;
import com.yizhixiaomifeng.hss.admin.bean.impl.helper.JsonParseWorker;
import com.yizhixiaomifeng.hss.util.HTTPRequest;

public class AdminOperateImpl implements AdminOperate {

	@Override
	public List<DepartmenttEntity> loadDepartments() {
		List<DepartmenttEntity> list=null;

		HashMap<String, String> paramsData=new HashMap<String,String>();
		
		String result=HTTPRequest.request(HTTPURL.LOAD_DEPARTMENTS, paramsData);
		if(!result.equals("«Î«Û“Ï≥£")&&!result.equals("«Î«Û ß∞‹")&&!result.equals("failed")){
			if(result.equals("error")){
				list=new ArrayList<DepartmenttEntity>();
			}else{
				list=JsonParseDepartment.parseDeparment(result);
			}
		}else{
			list=null;
		}
		return list;
	}

	@Override
	public boolean deleteDepartment(DepartmenttEntity department) {
		HashMap<String, String> paramsData=new HashMap<String,String>();
		paramsData.put("number", department.getNumber()+"");
		String result=HTTPRequest.request(HTTPURL.DELETE_DEPARTMENT, paramsData);
		if(result.equals("ok")){
			return true;
		}
		return false;
	}

	@Override
	public boolean editDepartment(DepartmenttEntity department) {
		HashMap<String, String> paramsData=new HashMap<String,String>();
		paramsData.put("number", department.getNumber()+"");
		paramsData.put("name", department.getName());
		paramsData.put("phone", department.getPhone());
		paramsData.put("businessId", department.getBusinessTypeEntity().getBusinessId()+"");
		String result=HTTPRequest.request(HTTPURL.EDIT_DEPARTMENT, paramsData);
		if(result.equals("ok")){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean addDepartment(DepartmenttEntity department) {
		HashMap<String, String> paramsData=new HashMap<String,String>();
		paramsData.put("name", department.getName());
		paramsData.put("phone", department.getPhone());
		paramsData.put("businessId", department.getBusinessTypeEntity().getBusinessId()+"");
		String result=HTTPRequest.request(HTTPURL.ADD_DEPARTMENT, paramsData);
		if(!result.equals("«Î«Û“Ï≥£")&&!result.equals("«Î«Û ß∞‹")&&!result.equals("failed")){
			if(!result.equals("error")){
				department.setNumber(Integer.valueOf(result));
				return true;
			}
		}
		return false;
	}

	@Override
	public List<WorkerEntity> loadWokers(DepartmenttEntity department) {
		List<WorkerEntity> list=null;

		HashMap<String, String> paramsData=new HashMap<String,String>();
		paramsData.put("number", department.getNumber()+"");
		
		String result=HTTPRequest.request(HTTPURL.LOAD_WORKER, paramsData);
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

	@Override
	public boolean deleteWorker(WorkerEntity worker) {
		HashMap<String, String> paramsData=new HashMap<String,String>();
		paramsData.put("jobnum", worker.getJobNum()+"");
		String result=HTTPRequest.request(HTTPURL.DELETE_WORKER, paramsData);
		if(result.equals("ok")){
			return true;
		}
		return false;
	}

	@Override
	public boolean editWorker(WorkerEntity worker) {
		HashMap<String, String> paramsData=new HashMap<String,String>();
		paramsData.put("jobnum", worker.getJobNum()+"");
		paramsData.put("basePay", worker.getBasePay()+"");
		paramsData.put("name", worker.getName());
		paramsData.put("entryDate", worker.getEntryDate()+"");
		paramsData.put("departmentId", worker.getDepartmenttEntity().getNumber()+"");
		paramsData.put("dutyId", worker.getDutyTypeEntity().getDutyId()+"");
		String result=HTTPRequest.request(HTTPURL.EDIT_WORKER, paramsData);
		if(result.equals("ok")){
			return true;
		}
		return false;
	}

	@Override
	public boolean addWorker(WorkerEntity worker) {
		HashMap<String, String> paramsData=new HashMap<String,String>();
		paramsData.put("basePay", worker.getBasePay()+"");
		paramsData.put("name", worker.getName());
		paramsData.put("entryDate", worker.getEntryDate()+"");
		paramsData.put("departmentId", worker.getDepartmenttEntity().getNumber()+"");
		paramsData.put("dutyId", worker.getDutyTypeEntity().getDutyId()+"");
		String result=HTTPRequest.request(HTTPURL.ADD_WORKER, paramsData);
		if(!result.equals("«Î«Û“Ï≥£")&&!result.equals("«Î«Û ß∞‹")&&!result.equals("failed")){
			if(!result.equals("error")){
				worker.setJobNum(Integer.valueOf(result));
				return true;
			}
		}
		return false;
	}

}
