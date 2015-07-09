/**
 *2015
 *
 * Licensed under the hl License, Version 1.0 (the "License");
 */
package com.yizhixiaomifeng.domain;

import java.util.ArrayList;
import java.util.List;

public class DutyTypeEntity {
	private Integer dutyId; 
	private String name; 
    private BusinessTypeEntity businessTypeEntity; 
	private List<WorkerEntity> workerEntities=new ArrayList<WorkerEntity>();  
	public Integer getDutyId() {
		return dutyId;
	}

	public void setDutyId(Integer dutyId) {
		this.dutyId = dutyId;
	}

	public String getName() {
		return name;
	}

	

	public void setName(String name) {
		this.name = name;
	}
	public List<WorkerEntity> getWorkerEntities() {
		return workerEntities;
	}

	public void setWorkerEntities(List<WorkerEntity> workerEntities) {
		this.workerEntities = workerEntities;
	}
	public BusinessTypeEntity getBusinessTypeEntity() {
		return businessTypeEntity;
	}

	public void setBusinessTypeEntity(BusinessTypeEntity businessTypeEntity) {
		this.businessTypeEntity = businessTypeEntity;
	}
	
	
}
