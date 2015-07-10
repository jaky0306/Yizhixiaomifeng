package com.yizhixiaomifeng.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WorkerEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long jobNum; 
	private String name; 
	private String nativePlace; 
	private String gender; 
	private String educationBackground; 
	private Integer age; 
	private String major; 
	private DutyTypeEntity dutyTypeEntity; 
	private Long entryDate; 
	private Integer workerYear=0;
	private Integer basePay=0; 
	private DepartmenttEntity departmenttEntity;
	private String cellPhone; 
	private String password; 
	private String headPortraitUrl; 
    private List<MissionAllotEntity> missionAllotEntities=new ArrayList<MissionAllotEntity>(); 
	public long getJobNum() {
		return jobNum;
	}

	public void setJobNum(long jobNum) {
		this.jobNum = jobNum;
	}

	public DepartmenttEntity getDepartmenttEntity() {
		return departmenttEntity;
	}

	public void setDepartmenttEntity(DepartmenttEntity departmenttEntity) {
		this.departmenttEntity = departmenttEntity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEducationBackground() {
		return educationBackground;
	}

	public void setEducationBackground(String educationBackground) {
		this.educationBackground = educationBackground;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public Long getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Long entryDate) {
		this.entryDate = entryDate;
	}

	public Integer getWorkerYear() {
		return workerYear;
	}

	public void setWorkerYear(Integer workerYear) {
		this.workerYear = workerYear;
	}

	public Integer getBasePay() {
		return basePay;
	}

	public void setBasePay(Integer basePay) {
		this.basePay = basePay;
	}

	public String getHeadPortraitUrl() {
		return headPortraitUrl;
	}

	public void setHeadPortraitUrl(String headPortraitUrl) {
		this.headPortraitUrl = headPortraitUrl;
	}

	public DutyTypeEntity getDutyTypeEntity() {
		return dutyTypeEntity;
	}

	public void setDutyTypeEntity(DutyTypeEntity dutyTypeEntity) {
		this.dutyTypeEntity = dutyTypeEntity;
	}
	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public List<MissionAllotEntity> getMissionAllotEntities() {
		return missionAllotEntities;
	}

	public void setMissionAllotEntities(
			List<MissionAllotEntity> missionAllotEntities) {
		this.missionAllotEntities = missionAllotEntities;
	}
	
}
