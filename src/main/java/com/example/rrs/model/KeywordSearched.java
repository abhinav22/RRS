package com.example.rrs.model;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Document
public class KeywordSearched {

	private String keyword;

	private long count;

	@DateTimeFormat(iso = ISO.DATE)
	private Date lastSearchedDate;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public Date getLastSearchedDate() {
		return lastSearchedDate;
	}

	public void setLastSearchedDate(Date lastSearchedDate) {
		this.lastSearchedDate = lastSearchedDate;
	}

	
}
