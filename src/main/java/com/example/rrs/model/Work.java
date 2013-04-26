package com.example.rrs.model;

import org.hibernate.validator.constraints.NotEmpty;

public class Work {

	@NotEmpty
	private String position;

	@NotEmpty
	private String company;

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

}
