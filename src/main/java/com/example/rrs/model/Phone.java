package com.example.rrs.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Phone {

	@NotNull
	@Size(max = 20)
	private String type;
	
	@NotNull
	@Size(min = 3, max = 40)
	private String content;

	public Phone() {
		super();
	}

	public Phone(String type, String content) {
		super();
		this.type = type;
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}

	public String getType() {
		return this.type;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Phone other = (Phone) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
