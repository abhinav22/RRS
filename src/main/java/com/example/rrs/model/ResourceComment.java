package com.example.rrs.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document
public class ResourceComment {

	@Id
	private String oid;
	
	private String resourceId;

	private String userId;

	private String comment;

	@DateTimeFormat(style="M-")
	private Date commentedDate;

	public ResourceComment( String user, String resource, String comment) {
		super();
		this.resourceId = resource;
		this.userId = user;
		this.comment = comment;
		this.commentedDate = new Date();
	}

	public ResourceComment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}


	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getCommentedDate() {
		return commentedDate;
	}

	public void setCommentedDate(Date commentedDate) {
		this.commentedDate = commentedDate;
	}

	@Override
	public String toString() {
		return "ResourceComment [oid=" + oid + ", resource=" + resourceId
				+ ", user=" + userId + ", comment=" + comment
				+ ", commentedDate=" + commentedDate + "]";
	}
	
}
