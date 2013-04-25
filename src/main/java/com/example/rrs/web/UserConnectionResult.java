package com.example.rrs.web;

import java.util.Date;

import com.example.rrs.model.User;

public class UserConnectionResult {
	private Date connectedDate;
	private User user;
	private String connectionStatus;

	public UserConnectionResult(User user, String connectionStatus,
			Date connectedDate) {
		super();
		this.connectedDate = connectedDate;
		this.connectionStatus = connectionStatus;
		this.user = user;
	}

	public UserConnectionResult() {
		super();
	}

	public Date getConnectedDate() {
		return connectedDate;
	}

	public void setConnectedDate(Date connectedDate) {
		this.connectedDate = connectedDate;
	}

	public String getConnectionStatus() {
		return connectionStatus;
	}

	public void setConnectionStatus(String connectionStatus) {
		this.connectionStatus = connectionStatus;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
