/**
 *2015
 *
 * Licensed under the hl License, Version 1.0 (the "License");
 */
package com.yizhixiaomifeng.domain;


public class MessageEntity {
	private Integer id; 
	private String title; 
	private String content; 
	private Long releasedTime; 

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public Long getReleasedTime() {
		return releasedTime;
	}

	public void setReleasedTime(Long releasedTime) {
		this.releasedTime = releasedTime;
	}

}
