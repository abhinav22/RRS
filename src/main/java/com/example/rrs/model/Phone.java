package com.example.rrs.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Persistent;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Persistent
public class Phone {

	@NotNull
	@Size(min = 3, max = 40)
	private String content;

	@Id
	private BigInteger id;

	@NotNull
	@Size(max = 20)
	private String type;

	public String getContent() {
		return this.content;
	}

	public BigInteger getId() {
		return this.id;
	}

	public String getType() {
		return this.type;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String toJson() {
		return new JSONSerializer().exclude("*.class").serialize(this);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
