package com.example.rrs.security;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.rrs.model.User;

@Named("customSaltSource")
public class SaltSourceImpl implements SaltSource {
	private static final Logger log = LoggerFactory
			.getLogger(SaltSourceImpl.class);

	@Override
	public Object getSalt(UserDetails _user) {
		String salt = ((User) _user).salt();
		if (log.isDebugEnabled()) {
			log.debug("salt @@@@" + salt);
		}
		return salt;
	}

}
