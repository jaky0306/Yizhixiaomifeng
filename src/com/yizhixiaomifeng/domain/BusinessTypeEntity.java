/**
 *2015
 *
 * Licensed under the hl License, Version 1.0 (the "License");
 */
package com.yizhixiaomifeng.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class BusinessTypeEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long businessId; 
	private String name;
	private List<DutyTypeEntity> dutyTypeEntities=new ArrayList<DutyTypeEntity>();
	private DepartmenttEntity departmenttEntity;
	
	public long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(long businessId) {
		this.businessId = businessId;
	}
	public String getName() {
		return name;
	}

	

	public void setName(String name) {
		this.name = name;
	}
	public List<DutyTypeEntity> getDutyTypeEntities() {
		return dutyTypeEntities;
	}

	public void setDutyTypeEntities(List<DutyTypeEntity> dutyTypeEntities) {
		this.dutyTypeEntities = dutyTypeEntities;
	}
	 public DepartmenttEntity getDepartmenttEntity() {
		return departmenttEntity;
	}

	public void setDepartmenttEntity(DepartmenttEntity departmenttEntity) {
		this.departmenttEntity = departmenttEntity;
	}
	
}
