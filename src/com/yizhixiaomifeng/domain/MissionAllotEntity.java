/**
 *2015
 *
 * Licensed under the hl License, Version 1.0 (the "License");
 */
package com.yizhixiaomifeng.domain;

import java.util.List;

public class MissionAllotEntity {
	private Integer missionAllotId; 
	private ClientEntity clientEntity; 
	private WorkerEntity workerEntity;
	private Long startTime;   
	private Long endTime;     
	private List<WorkingRecordEntity> workingRecordEntities;  
	public Integer getMissionAllotId() {
		return missionAllotId;
	}

	public void setMissionAllotId(Integer missionAllotId) {
		this.missionAllotId = missionAllotId;
	}
	public ClientEntity getClientEntity() {
		return clientEntity;
	}

	public void setClientEntity(ClientEntity clientEntity) {
		this.clientEntity = clientEntity;
	}
	public WorkerEntity getWorkerEntity() {
		return workerEntity;
	}

	public void setWorkerEntity(WorkerEntity workerEntity) {
		this.workerEntity = workerEntity;
	}
	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	public List<WorkingRecordEntity> getWorkingRecordEntities() {
		return workingRecordEntities;
	}

	public void setWorkingRecordEntities(
			List<WorkingRecordEntity> workingRecordEntities) {
		this.workingRecordEntities = workingRecordEntities;
	}
	
	

}
