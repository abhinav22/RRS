package com.example.rrs.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.util.Assert;

@Document
public class Connection {
	public static final String CONNECTION_STATUS_NONE="none";
	public static final String CONNECTION_STATUS_CONNECTED="connected";
	public static final String CONNECTION_STATUS_WAITING="waiting";
	public static final String CONNECTION_STATUS_BEING_WAITED="being-waited";

	public enum Status {
		PENDING, ACCEPTED, IGNORE;
	}
	
	@Id
	private String id;

	@Size(min=2, max=2)
	private List<String> userIds=new ArrayList<String>(2);

	@DateTimeFormat(iso = ISO.DATE)
	private Date connectedDate;

	private Status status=Status.PENDING;

	public Connection(String userId, String connectedTo) {
		Assert.notNull(userId);
		Assert.notNull(connectedTo);
		this.userIds.add(userId);
		this.userIds.add(connectedTo);	
	}

	public List<String> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}

	public Date getConnectedDate() {
		return connectedDate;
	}

	public void setConnectedDate(Date connectedDate) {
		this.connectedDate = connectedDate;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
