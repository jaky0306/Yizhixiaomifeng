package com.yizhixiaomifeng.tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LocalStorage {
	
	SharedPreferences shared ;
	SharedPreferences.Editor editor ;
	
	public LocalStorage(Activity context){
		shared = context.getSharedPreferences("yizhixiaomifeng",context.MODE_PRIVATE);
		editor = shared.edit();
	}
	
	public LocalStorage(SharedPreferences post){
		shared = post ;
		editor = post.edit() ;
	}
	
	public Editor putInt(String key,int value){
		return editor.putInt(key, value);
	}
	
	public Editor putString(String key,String value){
		return editor.putString(key, value);
	}
	
	public boolean commitEditor(){
		return editor.commit();
	}
	
	public int getInt(String key,int defValue){
		return shared.getInt(key, defValue) ;
	}
	
	public String getString(String key,String defValue){
		return shared.getString(key, defValue);
	}
	
	public boolean has(String key){
		return shared.contains(key);
	}
	
}
