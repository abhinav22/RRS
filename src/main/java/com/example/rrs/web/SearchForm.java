package com.example.rrs.web;

import java.io.Serializable;

public class SearchForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String keyword;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@Override
	public String toString() {
		return "SearchForm [keyword=" + keyword + "]";
	}	

}
