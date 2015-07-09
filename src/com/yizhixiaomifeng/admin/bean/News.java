package com.yizhixiaomifeng.admin.bean;

import java.io.Serializable;

public class News implements Serializable{
	private long id;
	private String title;
	private String content;
	private long releasedTime;
	private int status;
	public News() {}
	public News(long id, String title, String content, long releasedTime,
			int status) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.releasedTime = releasedTime;
		this.status = status;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getReleasedTime() {
		return releasedTime;
	}
	public void setReleasedTime(long releasedTime) {
		this.releasedTime = releasedTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
