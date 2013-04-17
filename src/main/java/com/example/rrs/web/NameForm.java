package com.example.rrs.web;

import org.hibernate.validator.constraints.NotEmpty;

import com.example.rrs.model.SalutationLine;

public class NameForm {
	@NotEmpty
	private String firstName;
	
	@NotEmpty
	private String lastName;
	
	private SalutationLine salutationLine=SalutationLine.NONE;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public SalutationLine getSalutationLine() {
		return salutationLine;
	}

	public void setSalutationLine(SalutationLine salutationLine) {
		this.salutationLine = salutationLine;
	}

	@Override
	public String toString() {
		return "NameForm [firstName=" + firstName + ", lastName=" + lastName
				+ ", salutationLine=" + salutationLine + "]";
	}
	
}
