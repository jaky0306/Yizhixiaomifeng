package com.yizhixiaomifeng.admin.bean;

import java.io.Serializable;

public class FailAttendance implements Serializable{
	private String phone;  //������Ա�ĵ绰���루���û�����
	private String name;	//����
	private long checkInTime;  //ǩ��ʱ��
	private String checkInStatus; //ǩ��״̬
	private String checkInResult;//ǩ�����
	private long checkOutTime;	//ǩ��ʱ��
	private String checkOutStatus;	//ǩ��ת̬
	private String checkOutResult;	//ǩ�˽��
	private String checkInSceneUrl;	//ǩ���ֳ�URL
	private String checkInVoiceUrl;	//ǩ��¼��URL
	private String checkOutVoiceUrl;	//ǩ��¼��url
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
