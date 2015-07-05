package com.yizhixiaomifeng.tools;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

/**
 * 
 * @author Jaky
 * �˳�APPʱ�ر����е�Activity
 * �÷�����Ӧ����ÿһ��Activity��onCreate(Bundle savedInstance)
 * �����м��뵱ǰActivity
 * ����ActivityCloser.activities.add(this);
 * ��Ӧ���˳�ʱ��
 * ActivityCloser.exitAllActivities(MainActivity.this);
 */
public class ActivityCloser {
	public static List<Activity> activities = new ArrayList<Activity>();
	public static void exitAllActivities(Context context){
		for(int i=0;i<activities.size();i++){
			if(activities.get(i)!=null){
				activities.get(i).finish();
			}
		}
//		ActivityManager aManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
//		aManager.restartPackage(context.getPackageName());
		System.exit(0);
	}

}
