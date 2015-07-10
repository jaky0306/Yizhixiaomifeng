package com.yizhixiaomifeng.config;

public class URLConfig {
	//public static String IP="http://172.16.102.50"+"/";
	public static String IP="http://172.16.107.57:8080"+"/hl-framework-all/";
	public static String checkUser=IP+"knotAppController.do?login";
	public static String checkAdmin=IP+"knotAppController.do?login";
	public static String getAllUser=IP+"workerController.do?listAllWorkerMessage";
	public static String getAllClientByPhone=IP+"knotAppController.do?listAllClient";
	public static String getClientByStart=IP+"knotAppController.do?listClient";
	public static String saveClient=IP+"knotAppController.do?saveClient";
	public static String deleteClient=IP+"knotAppController.do?deleteClient";
	public static String updateClient=IP+"knotAppController.do?updateClient";
	public static String getNewsByStartAndStatus=IP+"messageController.do?listMessage";
	public static String deleteNews=IP+"messageController.do?deleteMessage";
	public static String saveNews=IP+"messageController.do?saveMessage";
	public static String updateNews=IP+"messageController.do?updateMessage";
	public static String saveArrangement=IP+"missionAllotController.do?saveMissionAllot";
	//public static String checkin=IP+"missionAllotController.do?saveMissionAllot";
	public static String checkin=IP+"workingRecordController.do?saveWorkingRecord";
	public static String getAttendanceDataByDate=IP+"attendanceController.do?listWorkingRecord";
	
}	
