package com.yizhixiaomifeng.hss.admin.bean.impl.helper;

import java.io.File;

public class HTTPURL {
	public final static String REALM_NAME="http://172.16.44.38:8080";
	public final static String PROJECT_URL=REALM_NAME+File.separator+"hl-framework-all";
	/**
	 * ���ز�����ϢURL
	 */
	public final static String LOAD_DEPARTMENTS=PROJECT_URL+File.separator+"departmentController.do?listDepartment";
	/**
	 * ɾ��������ϢURL
	 */
	public final static String DELETE_DEPARTMENT=PROJECT_URL+File.separator+"departmentController.do?deleteDepartment";
	/**
	 * �༭������ϢURL
	 */
	public final static String EDIT_DEPARTMENT=PROJECT_URL+File.separator+"departmentController.do?updateDepartment";
	/**
	 * ��Ӳ�����ϢURL
	 */
	public final static String ADD_DEPARTMENT=PROJECT_URL+File.separator+"departmentController.do?saveDepartment";
	

	/**
	 * ����Ա����ϢURL
	 */
	public final static String LOAD_WORKER=PROJECT_URL+File.separator+"workerController.do?listWorker";
	/**
	 * ɾ��Ա����ϢURL
	 */
	public final static String DELETE_WORKER=PROJECT_URL+File.separator+"workerController.do?deleteWorker";
	/**
	 * �༭Ա����ϢURL
	 */
	public final static String EDIT_WORKER=PROJECT_URL+File.separator+"workerController.do?editWorker";
	/**
	 * ���Ա����ϢURL
	 */
	public final static String ADD_WORKER=PROJECT_URL+File.separator+"workerController.do?saveWorker";
	

	/**
	 * ����ҵ����������
	 */
	public final static String LOAD_BUSINESS_TYPE_NAME=PROJECT_URL+File.separator+"businessTypeController.do?listBusinessType";
	/**
	 * ͨ�����Ʋ��ҷ���������Ա��
	 */
	public final static String LOAD_DEPARTMENTS_AND_DUTY=PROJECT_URL+File.separator+"departmentController.do?listDepartmentAndDuty";
	/**
	 * ͨ�����Ʋ��ҷ���������Ա��
	 */
	public final static String LOAD_WORKER_BY_NAME=PROJECT_URL+File.separator+"workerController.do?listWorkerName";
}
