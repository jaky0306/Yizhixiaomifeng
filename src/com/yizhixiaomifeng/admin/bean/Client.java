package com.yizhixiaomifeng.admin.bean;

import java.io.Serializable;

//[{"address":"中国移动南方基地","endTime":1,"id":1,"laitude":1,"longitude":1,"name":"华软软件学院","projectName":"APP开发","startTime":1},{"address":"广州诺特科技有限公司","endTime":1,"id":2,"laitude":1,"longitude":1,"name":"华软软件学院","projectName":"企业外勤人员考勤管理系统","startTime":1}]
public class Client implements Serializable{
	private long id;
	private String name;
	private String projectName;
	private String address;
	private double longitude;
	private double latitude;
	private long startTime ;
	private long endTime;
	public Client(){}
	public Client(long id, String name, String projectName, String address,
			double longitude, double latitude, long startTime, long endTime) {
		super();
		this.id = id;
		this.name = name;
		this.projectName = projectName;
		this.address = address;
		this.longitude = longitude;
		this.latitude = latitude;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * 获取项目名称
	 * @return
	 */
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
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
}
