/**
 *2015
 *
 * Licensed under the hl License, Version 1.0 (the "License");
 */
package com.yizhixiaomifeng.domain;

import java.util.List;

public class ClientEntity {
	private Integer clientId; 
	private String name; 
	private String projectName; 
	private String address; 
	private double longitude; 
	private double latitude; 
	private Long startTime; 
	private Long endTime; 
	private List<MissionAllotEntity> missionAllotEntities; 

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
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

	public List<MissionAllotEntity> getMissionAllotEntities() {
		return missionAllotEntities;
	}

	public void setMissionAllotEntities(
			List<MissionAllotEntity> missionAllotEntities) {
		this.missionAllotEntities = missionAllotEntities;
	}

}
