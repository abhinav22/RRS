package com.example.rrs.model;

public enum SalutationLine {

	NONE("Not set, use email instead"),
	FIRSTNAME("FirstName only"),
	FIRSTNAME_AND_LASTNAME("Firstname and lastname"),
	FIRSTNAME_FIRST_CHAR_OF_LASTNAME("Firstname and first character of lastname");
	
	private String desc;
	
	SalutationLine(String desc){
		this.desc=desc;
	}
	
	public String getDesc(){
		return this.desc;
	}
}
