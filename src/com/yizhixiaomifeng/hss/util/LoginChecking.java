package com.yizhixiaomifeng.hss.util;

import android.app.Activity;

import com.yizhixiaomifeng.tools.LocalStorage;

public class LoginChecking {
	public static boolean isLogin(Activity activity){
		LocalStorage ls = new LocalStorage(activity);
		if(ls.getString("username", null)==null)
			return false;
		return true;
	}
}
