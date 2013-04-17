package com.example.rrs.web;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class EmailForm {
	
	@NotEmpty
	private String type;
	
	@NotEmpty
	@Email
	private String content;

	public EmailForm() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmailForm(String type, String content) {
		super();
		this.type = type;
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "EmailForm [type=" + type + ", content=" + content + "]";
	}
	
}
