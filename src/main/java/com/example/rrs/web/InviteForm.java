package com.example.rrs.web;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

public class InviteForm {

	@NotEmpty
	private List<String> emails=new ArrayList<String>();

	@NotEmpty
	private String title;

	@NotEmpty
	private String content;
	
	private String extra;
	

	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
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
