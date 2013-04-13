package com.example.rrs.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import flexjson.JSONSerializer;

@Document
@Persistent
public class User implements UserDetails, Serializable {


	@NotNull
	private Boolean active = true;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date creationDate;

	@Id
	@Indexed
	private String id;

	@Size(max = 20)
	private String firstName;

	@Size(max = 20)
	private String lastName;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date lastUpdateDate;

	@NotNull
	@NotEmpty
	@Size(max = 20)
	private String password;

	@NotNull
	@Column(unique = true)
	@Size(min = 3, max = 40)
	@Email
	private String email;


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
	}


	public Date getCreationDate() {
		return this.creationDate;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getId() {
		return this.id;
	}

	public String getLastName() {
		return this.lastName;
	}

	public Date getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	@Override
	public String getPassword() {
		return this.password;
	}


	@Override
	@javax.persistence.Transient
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@javax.persistence.Transient
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@javax.persistence.Transient
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@javax.persistence.Transient
	public boolean isEnabled() {
		return true;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}


	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getUsername() {
		return this.email;
	}
	
	public String getName() {
		return (this.firstName == null ? "" : this.firstName)
				+ (this.lastName == null ? "" : " "+this.lastName);
	}

	public String toJson() {
		return new JSONSerializer().exclude("*.class").serialize(this);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@javax.persistence.Transient
	public String getSalt() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		return sdf.format(this.getCreationDate());
	}

}
