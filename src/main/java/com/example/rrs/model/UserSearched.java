package com.example.rrs.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Document
public class UserSearched {
	
	@Id
	private String id;

	@DBRef
	private User user;

	@DBRef
	private User searchedBy;

	@DateTimeFormat(iso = ISO.DATE)
	private Date searchedDate;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getSearchedBy() {
		return searchedBy;
	}

	public void setSearchedBy(User searchedBy) {
		this.searchedBy = searchedBy;
	}

	public Date getSearchedDate() {
		return searchedDate;
	}

	public void setSearchedDate(Date searchedDate) {
		this.searchedDate = searchedDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
