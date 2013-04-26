package com.example.rrs.web;

import javax.validation.Valid;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import com.example.rrs.model.Place;
import com.example.rrs.model.Work;

public class UserForm {
	@NotEmpty
	private String name;

	@Valid
	private Work currentWork;

	@Valid
	private Place currentPlace;
	
	@NotEmpty
	private String summary;

	@Email
	private String email;

	@URL
	private String link;

	private String phoneNumber;

	@NotEmpty
	private String skillStr;

	public UserForm() {
		super();
		this.currentPlace = new Place();
		this.currentWork = new Work();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Work getCurrentWork() {
		return currentWork;
	}

	public void setCurrentWork(Work currentWork) {
		this.currentWork = currentWork;
	}

	public Place getCurrentPlace() {
		return currentPlace;
	}

	public void setCurrentPlace(Place currentPlace) {
		this.currentPlace = currentPlace;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getSkillStr() {
		return skillStr;
	}

	public void setSkillStr(String skillStr) {
		this.skillStr = skillStr;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

}
