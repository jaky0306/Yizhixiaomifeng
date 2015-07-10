package com.yizhixiaomifeng.admin.bean;

import java.io.Serializable;

public class FailAttendance implements Serializable{
	private String phone;  //考勤人员的电话号码（即用户名）
	private String name;	//姓名
	private long checkInTime;  //签到时间
	private String checkInStatus; //签到状态
	private String checkInResult;//签到结果
	private long checkOutTime;	//签退时间
	private String checkOutStatus;	//签退转态
	private String checkOutResult;	//签退结果
	private String checkInSceneUrl;	//签到现场URL
	private String checkInVoiceUrl;	//签到录音URL
	private String checkOutVoiceUrl;	//签退录音url
	public FailAttendance(){}
	public FailAttendance(String phone, String name, long checkInTime,
			String checkInStatus, String checkInResult, long checkOutTime,
			String checkOutStatus, String checkOutResult,
			String checkInSceneUrl, String checkInVoiceUrl,
			String checkOutVoiceUrl) {
		super();
		this.phone = phone;
		this.name = name;
		this.checkInTime = checkInTime;
		this.checkInStatus = checkInStatus;
		this.checkInResult = checkInResult;
		this.checkOutTime = checkOutTime;
		this.checkOutStatus = checkOutStatus;
		this.checkOutResult = checkOutResult;
		this.checkInSceneUrl = checkInSceneUrl;
		this.checkInVoiceUrl = checkInVoiceUrl;
		this.checkOutVoiceUrl = checkOutVoiceUrl;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(long checkInTime) {
		this.checkInTime = checkInTime;
	}

	public String getCheckInStatus() {
		return checkInStatus;
	}

	public void setCheckInStatus(String checkInStatus) {
		this.checkInStatus = checkInStatus;
	}

	public String getCheckInResult() {
		return checkInResult;
	}

	public void setCheckInResult(String checkInResult) {
		this.checkInResult = checkInResult;
	}

	public long getCheckOutTime() {
		return checkOutTime;
	}

	public void setCheckOutTime(long checkOutTime) {
		this.checkOutTime = checkOutTime;
	}

	public String getCheckOutStatus() {
		return checkOutStatus;
	}

	public void setCheckOutStatus(String checkOutStatus) {
		this.checkOutStatus = checkOutStatus;
	}

	public String getCheckOutResult() {
		return checkOutResult;
	}

	public void setCheckOutResult(String checkOutResult) {
		this.checkOutResult = checkOutResult;
	}

	public String getCheckInSceneUrl() {
		return checkInSceneUrl;
	}

	public void setCheckInSceneUrl(String checkInSceneUrl) {
		this.checkInSceneUrl = checkInSceneUrl;
	}

	public String getCheckInVoiceUrl() {
		return checkInVoiceUrl;
	}

	public void setCheckInVoiceUrl(String checkInVoiceUrl) {
		this.checkInVoiceUrl = checkInVoiceUrl;
	}

	public String getCheckOutVoiceUrl() {
		return checkOutVoiceUrl;
	}

	public void setCheckOutVoiceUrl(String checkOutVoiceUrl) {
		this.checkOutVoiceUrl = checkOutVoiceUrl;
	}

	
	
}
