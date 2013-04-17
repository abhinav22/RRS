package com.example.rrs.web;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

public class PhoneForm {
	
	@NotEmpty
	private String type;
	
	@NotEmpty
	private String content;

	public PhoneForm() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PhoneForm(String type, String content) {
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
		return "PhoneForm [type=" + type + ", content=" + content + "]";
	}
	
}
