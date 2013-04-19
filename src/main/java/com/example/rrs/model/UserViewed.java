package com.example.rrs.model;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Document
public class UserViewed {

	@DBRef
	private User user;

	@DBRef
	private User viewedBy;

	@DateTimeFormat(iso = ISO.DATE)
	private Date viewedDate;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getViewedBy() {
		return viewedBy;
	}

	public void setViewedBy(User viewedBy) {
		this.viewedBy = viewedBy;
	}

	public Date getViewedDate() {
		return viewedDate;
	}

	public void setViewedDate(Date viewedDate) {
		this.viewedDate = viewedDate;
	}

}
