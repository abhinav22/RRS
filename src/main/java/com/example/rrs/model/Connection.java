package com.example.rrs.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Document
public class Connection {

	public enum Status {
		PENDING, ACCEPTED;
	}
	
	@Id
	private String id;

	@DBRef
	@Size(min=2, max=2)
	private List<User> users;

	@DateTimeFormat(iso = ISO.DATE)
	private Date connectedDate;

	private Status status;

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
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
