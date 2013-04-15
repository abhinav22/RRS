package com.example.rrs.web;

import org.hibernate.validator.constraints.NotEmpty;

public class InviteForm {

	@NotEmpty
	private String emails;

	@NotEmpty
	private String title;

	@NotEmpty
	private String content;

	public String getEmails() {
		return emails;
	}

	public void setEmails(String emails) {
		this.emails = emails;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "InviteForm [emails=" + emails + ", title=" + title
				+ ", content=" + content + "]";
	}

	
}
