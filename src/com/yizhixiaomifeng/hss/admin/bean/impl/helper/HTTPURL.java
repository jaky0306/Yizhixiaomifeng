package com.yizhixiaomifeng.hss.admin.bean.impl.helper;

import java.io.File;

public class HTTPURL {
	public final static String REALM_NAME="http://172.16.44.38:8080";
	public final static String PROJECT_URL=REALM_NAME+File.separator+"hl-framework-all";
	/**
	 * 加载部门信息URL
	 */
	public final static String LOAD_DEPARTMENTS=PROJECT_URL+File.separator+"departmentController.do?listDepartment";
	/**
	 * 删除部门信息URL
	 */
	public final static String DELETE_DEPARTMENT=PROJECT_URL+File.separator+"departmentController.do?deleteDepartment";
	/**
	 * 编辑部门信息URL
	 */
	public final static String EDIT_DEPARTMENT=PROJECT_URL+File.separator+"departmentController.do?updateDepartment";
	/**
	 * 添加部门信息URL
	 */
	public final static String ADD_DEPARTMENT=PROJECT_URL+File.separator+"departmentController.do?saveDepartment";
	

	/**
	 * 加载员工信息URL
	 */
	public final static String LOAD_WORKER=PROJECT_URL+File.separator+"workerController.do?listWorker";
	/**
	 * 删除员工信息URL
	 */
	public final static String DELETE_WORKER=PROJECT_URL+File.separator+"workerController.do?deleteWorker";
	/**
	 * 编辑员工信息URL
	 */
	public final static String EDIT_WORKER=PROJECT_URL+File.separator+"workerController.do?editWorker";
	/**
	 * 添加员工信息URL
	 */
	public final static String ADD_WORKER=PROJECT_URL+File.separator+"workerController.do?saveWorker";
	

	/**
	 * 加载业务类型名称
	 */
	public final static String LOAD_BUSINESS_TYPE_NAME=PROJECT_URL+File.separator+"businessTypeController.do?listBusinessType";
	/**
	 * 通过名称查找符合条件的员工
	 */
	public final static String LOAD_DEPARTMENTS_AND_DUTY=PROJECT_URL+File.separator+"departmentController.do?listDepartmentAndDuty";
	/**
	 * 通过名称查找符合条件的员工
	 */
	public final static String LOAD_WORKER_BY_NAME=PROJECT_URL+File.separator+"workerController.do?listWorkerName";
}
