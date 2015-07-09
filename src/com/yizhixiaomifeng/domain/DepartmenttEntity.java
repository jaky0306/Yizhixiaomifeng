/**
 *2015
 *
 * Licensed under the hl License, Version 1.0 (the "License");
 */
package com.yizhixiaomifeng.domain;

import java.io.Serializable;
import java.util.List;

public class DepartmenttEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer number; 
	private BusinessTypeEntity businessTypeEntity; 
	private String name; 
	private List<WorkerEntity> workerEntities; 
	private String phone; 
	private Integer workNumber; 
	  

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getWorkNumber() {
		return workNumber;
	}

	public void setWorkNumber(Integer workNumber) {
		this.workNumber = workNumber;
	}
	public BusinessTypeEntity getBusinessTypeEntity() {
		return businessTypeEntity;
	}

	public void setBusinessTypeEntity(BusinessTypeEntity businessTypeEntity) {
		this.businessTypeEntity = businessTypeEntity;
	}
	public List<WorkerEntity> getWorkerEntities() {
		return workerEntities;
	}

	public void setWorkerEntities(List<WorkerEntity> workerEntities) {
		this.workerEntities = workerEntities;
	}
	
	

}
