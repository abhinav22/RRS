package com.example.rrs.web;

import java.util.Date;

import com.example.rrs.model.User;

public class UserViewedResult {
	private Date viewedDate;
	private User user;

	public UserViewedResult(Date viewedDate, User user) {
		super();
		this.viewedDate = viewedDate;
		this.user = user;
	}

	public UserViewedResult() {
		super();
	}

	public Date getViewedDate() {
		return viewedDate;
	}

	public void setViewedDate(Date viewedDate) {
		this.viewedDate = viewedDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
