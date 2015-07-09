package com.yizhixiaomifeng.hss.admin.bean;

import java.util.List;

import com.yizhixiaomifeng.domain.BusinessTypeEntity;
import com.yizhixiaomifeng.domain.DepartmenttEntity;
import com.yizhixiaomifeng.domain.WorkerEntity;

public interface OtherOperate {
	/**
	 * �������е�ҵ������
	 * @return
	 */
	public List<BusinessTypeEntity> loaderBusinessTypes();
	/**
	 * ���ز��źͲ��Ŷ�Ӧ��ְλ����
	 * @return
	 */
	public List<DepartmenttEntity> loadDepartmentsAndDutyTypes();
	/**
	 * ͨ�����Ʋ��ҷ���������Ա��
	 * @param name
	 * @return
	 */
	public List<WorkerEntity> searchWorkerByName(String name);
}
