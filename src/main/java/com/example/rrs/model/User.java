package com.example.rrs.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.rrs.web.LinkForm;

@Document
@Persistent
public class User implements UserDetails, Serializable {

	@Id
	@Indexed
	private String id;

	@NotNull
	private boolean enabled = false;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date creationDate;

	@Size(max = 20)
	private String firstName;

	@Size(max = 20)
	private String lastName;

	private SalutationLine salutationLine = SalutationLine.NONE;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date lastUpdateDate;

	@NotNull
	@NotEmpty
	@Size(max = 20)
	private String password;

	@NotNull
	@Size(min = 3, max = 40)
	@Email
	private String email;

	private String confirmationCode;
	
	@DBRef
	private Picture avatar;

	private Map<String, Link> links = new HashMap<String, Link>();
	
	private Map<String, Phone> phones = new HashMap<String, Phone>();

	private Map<String, EmailAddress> emails = new HashMap<String, EmailAddress>();

	public Map<String, Link> getLinks() {
		return links;
	}

	public void setLinks(Map<String, Link> urls) {
		this.links = urls;
	}

	public Map<String, Phone> getPhones() {
		return phones;
	}

	public void setPhones(Map<String, Phone> phones) {
		this.phones = phones;
	}

	public Map<String, EmailAddress> getEmails() {
		return emails;
	}

	public void setEmails(Map<String, EmailAddress> emails) {
		this.emails = emails;
	}

	public SalutationLine getSalutationLine() {
		if (salutationLine == null) {
			this.salutationLine = SalutationLine.NONE;
		}
		return salutationLine;
	}

	public void setSalutationLine(SalutationLine salutationLine) {
		this.salutationLine = salutationLine;
	}

	@Override
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
	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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
		switch (this.getSalutationLine()) {
		case FIRSTNAME:
			return this.firstName;

		case FIRSTNAME_AND_LASTNAME:
			return this.firstName + " " + this.lastName;

		case FIRSTNAME_FIRST_CHAR_OF_LASTNAME:
			return this.firstName + " "
					+ String.valueOf(this.lastName.charAt(0)).toUpperCase();
		case NONE:
			return this.getEmail();
		default:
			return this.getEmail();
		}
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

	public void addLink(String url, Link link) {
		this.links.put(url, link);
	}

	public void removeLink(String url) {
		this.links.remove(url);
	}

	public void addEmail(String email, EmailAddress e) {
		this.emails.put(email, e);
	}

	public void removeEmail(String url) {
		this.emails.remove(email);
	}

	
	public void addPhone(String num, Phone e) {
		this.phones.put(num, e);
	}

	public void removePhone(String num) {
		this.phones.remove(num);
	}

	public Picture getAvatar() {
		return avatar;
	}

	public void setAvatar(Picture avatar) {
		this.avatar = avatar;
	}
}
