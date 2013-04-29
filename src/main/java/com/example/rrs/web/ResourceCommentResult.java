package com.example.rrs.web;

import com.example.rrs.model.ResourceComment;
import com.example.rrs.model.User;

public class ResourceCommentResult {
	private ResourceComment comment;
	private User user;
	
	public ResourceCommentResult(User user, ResourceComment comment) {
		super();
		this.user = user;
		this.comment = comment;
	}
	public ResourceComment getComment() {
		return comment;
	}
	public void setComment(ResourceComment comment) {
		this.comment = comment;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
