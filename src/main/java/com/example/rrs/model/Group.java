package com.example.rrs.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Persistent
public class Group {
	public enum Type {
		PUBLIC, PRIVATE;
	}

	@Id
	private String id;

	private String title;

	private String creationDate;

	private String description;

}
