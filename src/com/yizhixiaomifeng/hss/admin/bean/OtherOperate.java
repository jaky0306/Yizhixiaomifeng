package com.yizhixiaomifeng.hss.admin.bean;

import java.util.List;

import com.yizhixiaomifeng.domain.BusinessTypeEntity;
import com.yizhixiaomifeng.domain.DepartmenttEntity;
import com.yizhixiaomifeng.domain.WorkerEntity;

public interface OtherOperate {
	/**
	 * 加载所有的业务类型
	 * @return
	 */
	public List<BusinessTypeEntity> loaderBusinessTypes();
	/**
	 * 加载部门和部门对应的职位类型
	 * @return
	 */
	public List<DepartmenttEntity> loadDepartmentsAndDutyTypes();
	/**
	 * 通过名称查找符合条件的员工
	 * @param name
	 * @return
	 */
	public List<WorkerEntity> searchWorkerByName(String name);
}
