package com.yizhixiaomifeng.hss.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.avos.avoscloud.LogUtil.log;

public class TimeChangeUtil {

	@SuppressLint("SimpleDateFormat")
	public static String getTimeString(Long time){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(time);
	}
	@SuppressLint("SimpleDateFormat")
	public static long getTime(String time){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return df.parse(time).getTime();
		} catch (ParseException e) {
			log.e("TimeChangeUtil", "时间转换出错：："+time);
		}
		return 0;
	}
}
