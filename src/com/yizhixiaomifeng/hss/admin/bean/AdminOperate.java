package com.yizhixiaomifeng.hss.admin.bean;

import java.util.List;

import com.yizhixiaomifeng.domain.DepartmenttEntity;
import com.yizhixiaomifeng.domain.WorkerEntity;

public interface AdminOperate {
	public List<DepartmenttEntity> loadDepartments();
	public boolean deleteDepartment(DepartmenttEntity department);
	public boolean editDepartment(DepartmenttEntity department);
	public boolean addDepartment(DepartmenttEntity department);
	
	public List<WorkerEntity> loadWokers(DepartmenttEntity department);
	public boolean deleteWorker(WorkerEntity worker);
	public boolean editWorker(WorkerEntity worker);
	public boolean addWorker(WorkerEntity worker);
}
