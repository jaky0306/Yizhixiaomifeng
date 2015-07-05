package com.yizhixiaomifeng.tools;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

/**
 * 
 * @author Jaky
 * 退出APP时关闭所有的Activity
 * 用法：在应用中每一个Activity的onCreate(Bundle savedInstance)
 * 方法中加入当前Activity
 * 即：ActivityCloser.activities.add(this);
 * 在应用退出时：
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
