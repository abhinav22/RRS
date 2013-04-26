package com.example.rrs.model;

import org.hibernate.validator.constraints.NotEmpty;

public class Place {

	@NotEmpty
	private String city;

	@NotEmpty
	private String country;

	public Place(String country, String city) {
		super();
		this.country = country;
		this.city = city;
	}

	public Place() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
