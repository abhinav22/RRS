package com.example.rrs.web;

import com.example.rrs.model.Resource;
import com.example.rrs.model.User;

public class ResourceResult {
	
	private User sharedBy;
	
	private Resource resource;
	
	private boolean liked;
	
	private long likedCount;
	
	private long commentedCount;

	public User getSharedBy() {
		return sharedBy;
	}

	public void setSharedBy(User sharedBy) {
		this.sharedBy = sharedBy;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public boolean isLiked() {
		return liked;
	}

	public void setLiked(boolean liked) {
		this.liked = liked;
	}

	public long getLikedCount() {
		return likedCount;
	}

	public void setLikedCount(long likedCount) {
		this.likedCount = likedCount;
	}

	public long getCommentedCount() {
		return commentedCount;
	}

	public void setCommentedCount(long commentedCount) {
		this.commentedCount = commentedCount;
	}
	
	
}
