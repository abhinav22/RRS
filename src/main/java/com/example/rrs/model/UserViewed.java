package com.example.rrs.model;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Document
public class UserViewed {

	@Id
	private String id;

	
	private String userId;

	
	private String viewedBy;

	@DateTimeFormat(iso = ISO.DATE)
	private Date viewedDate;
	
	public UserViewed() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserViewed(String user, String viewedBy) {
		this.userId = user;
		this.viewedBy = viewedBy;
		Date date=new Date();
		
		date=DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
		this.viewedDate = date;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getViewedBy() {
		return viewedBy;
	}

	public void setViewedBy(String viewedBy) {
		this.viewedBy = viewedBy;
	}

	public Date getViewedDate() {
		return viewedDate;
	}

	public void setViewedDate(Date viewedDate) {
		this.viewedDate = viewedDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
