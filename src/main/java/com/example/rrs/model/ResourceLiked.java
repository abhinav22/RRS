package com.example.rrs.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ResourceLiked {

	@Id
	private String id;

	private String userId;

	private String resourceId;

	private Date likedDate;

	public ResourceLiked() {
		super();
	}

	public ResourceLiked(String userId2, String resourceId2) {
		this.userId = userId2;
		this.resourceId = resourceId2;
		this.likedDate = new Date();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public Date getLikedDate() {
		return likedDate;
	}

	public void setLikedDate(Date likedDate) {
		this.likedDate = likedDate;
	}

}
