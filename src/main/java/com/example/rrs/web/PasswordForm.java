package com.example.rrs.web;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class PasswordForm {
	
	@NotEmpty
	@Size(min=6)
	private String password;
	
	@NotEmpty
	@Size(min=6)
	private String repeatPassword;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepeatPassword() {
		return repeatPassword;
	}

	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}
	

}
