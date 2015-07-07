package com.yizhixiaomifeng.domain;

import java.util.List;

public class WorkerEntity {
	private Integer jobNum; // //员工编号、主键
	private String name; // 员工姓名
	private String nativePlace; // 籍贯
	private String gender; // 性别
	private String educationBackground; // 学历
	private Integer age; // 年龄
	private String major; // 专业
	private DutyTypeEntity dutyTypeEntity;  //职务   一对多
	private Long entryDate; // 入职时间
	private Integer workerYear; // 年资
	private Integer basePay; // 基本工资
	private DepartmenttEntity departmenttEntity; // 所属部门   多对一
	private String cellPhone;   //手机号码
	private String password;    //外勤系统的登录密码
	private String headPortraitUrl; // 头像URL
    private List<MissionAllotEntity> missionAllotEntities;  //任务列表   一对多
	public Integer getJobNum() {
		return jobNum;
	}

	public void setJobNum(Integer jobNum) {
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
