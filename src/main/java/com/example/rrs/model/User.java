package com.example.rrs.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
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

@Document
@Persistent
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements UserDetails, Serializable {

	@Id
	@Indexed
	private String id;

	@Size(max = 20)
	private String name;

	private Work currentWork;

	private Place currentPlace;
	
	private String summary;

	@NotNull
	private boolean enabled = false;

	@NotNull
	@NotEmpty
	@Size(max = 20)
	@JsonIgnore
	// ignore password to json stream for security.
	private String password;

	@NotNull
	@Size(min = 3, max = 40)
	@Email
	private String email;

	private String confirmationCode;

	private String avatarUrl;

	private List<Link> links = new ArrayList<Link>();

	private List<Phone> phones = new ArrayList<Phone>();

	private List<EmailAddress> emails = new ArrayList<EmailAddress>();

	private List<String> skills = new ArrayList<String>();

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date creationDate;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date lastUpdateDate;

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Work getCurrentWork() {
		return currentWork;
	}

	public void setCurrentWork(Work currentWork) {
		this.currentWork = currentWork;
	}

	public Place getCurrentPlace() {
		return currentPlace;
	}

	public void setCurrentPlace(Place currentPlace) {
		this.currentPlace = currentPlace;
	}

	public List<String> getSkills() {
		return skills;
	}

	public void setSkills(List<String> skills) {
		this.skills = skills;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public List<Phone> getPhones() {
		return phones;
	}

	public void setPhones(List<Phone> phones) {
		this.phones = phones;
	}

	public List<EmailAddress> getEmails() {
		return emails;
	}

	public void setEmails(List<EmailAddress> emails) {
		this.emails = emails;
	}

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
	}

	public String getConfirmationCode() {
		return confirmationCode;
	}

	public void setConfirmationCode(String confirmationCode) {
		this.confirmationCode = confirmationCode;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public String getId() {
		return this.id;
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
	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public void setId(String id) {
		this.id = id;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public String salt() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		return sdf.format(this.getCreationDate());
	}

	public void addLink(Link link) {
		this.links.add(link);
	}

	public void removeLink(String url) {
		this.links.remove(url);
	}

	public void addEmail(EmailAddress e) {
		this.emails.add(e);
	}

	public void removeEmail(String url) {
		this.emails.remove(email);
	}

	public void addPhone(Phone phone) {
		this.phones.add(phone);
	}

	public void removePhone(String num) {
		this.phones.remove(num);
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

}
