package com.yizhixiaomifeng.hss.admin.bean.impl.helper;

import java.io.File;

public class HTTPURL {
	public final static String REALM_NAME="http://172.16.137.13:9090";
	public final static String PROJECT_URL=REALM_NAME+File.separator+"campus_jizhi";
	
	/**
	 * ���ز�����ϢURL
	 */
	public final static String LOAD_DEPARTMENTS=PROJECT_URL+File.separator+"";
	/**
	 * ɾ��������ϢURL
	 */
	public final static String DELETE_DEPARTMENT=PROJECT_URL+File.separator+"";
	/**
	 * �༭������ϢURL
	 */
	public final static String EDIT_DEPARTMENT=PROJECT_URL+File.separator+"";
	/**
	 * ��Ӳ�����ϢURL
	 */
	public final static String ADD_DEPARTMENT=PROJECT_URL+File.separator+"";
	

	/**
	 * ����Ա����ϢURL
	 */
	public final static String LOAD_WORKER=PROJECT_URL+File.separator+"";
	/**
	 * ɾ��Ա����ϢURL
	 */
	public final static String DELETE_WORKER=PROJECT_URL+File.separator+"";
	/**
	 * �༭Ա����ϢURL
	 */
	public final static String EDIT_WORKER=PROJECT_URL+File.separator+"";
	

	/**
	 * ����ҵ����������
	 */
	public final static String LOAD_BUSINESS_TYPE_NAME=PROJECT_URL+File.separator+"";
}
