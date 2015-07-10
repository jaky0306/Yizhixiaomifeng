/**
 *2015
 *
 * Licensed under the hl License, Version 1.0 (the "License");
 */
package com.yizhixiaomifeng.domain;

import java.io.Serializable;


public class WorkingRecordEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long workingRecordId;
	private Long recordDate; 
	private Long firstRecordTime; 
	private Long lastRecordTime; 
	private Double longitude; 
	private Double latitude; 
	private String remark; 
	private MissionAllotEntity missionAllotEntity; 
	private String imageUrl; 

	public long getWorkingRecordId() {
		return workingRecordId;
	}

	public void setWorkingRecordId(long workingRecordId) {
		this.workingRecordId = workingRecordId;
	}
	public Long getRecordDate() {
		return recordDate;
	}

	

	public void setRecordDate(Long recordDate) {
		this.recordDate = recordDate;
	}

	public Long getFirstRecordTime() {
		return firstRecordTime;
	}

	public void setFirstRecordTime(Long firstRecordTime) {
		this.firstRecordTime = firstRecordTime;
	}

	public Long getLastRecordTime() {
		return lastRecordTime;
	}

	public void setLastRecordTime(Long lastRecordTime) {
		this.lastRecordTime = lastRecordTime;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public MissionAllotEntity getMissionAllotEntity() {
		return missionAllotEntity;
	}

	public void setMissionAllotEntity(MissionAllotEntity missionAllotEntity) {
		this.missionAllotEntity = missionAllotEntity;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	

}
