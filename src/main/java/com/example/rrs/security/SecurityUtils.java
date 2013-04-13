package com.example.rrs.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.rrs.model.User;

public class SecurityUtils {

	private static final Logger log = LoggerFactory
			.getLogger(SecurityUtils.class);

	public static User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (auth instanceof AnonymousAuthenticationToken) {
			return null;
		}

		Object userObj = auth.getPrincipal();

		User currentUser = null;
		if (userObj instanceof User) {
			currentUser = (User) userObj;
		}

		if (log.isDebugEnabled()) {
			log.debug("current user @" + currentUser);
		}

		return currentUser;
	}

	public static boolean isAuthenticated() {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (auth instanceof AnonymousAuthenticationToken) {
			return false;
		}
		return true;
	}

}
