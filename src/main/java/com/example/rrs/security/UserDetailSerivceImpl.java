package com.example.rrs.security;

import java.util.List;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.rrs.model.User;
import com.example.rrs.repository.UserRepository;

public class UserDetailSerivceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		List<User> users = userRepository.findByEmail(username);
		
		if (users.isEmpty()) {
			throw new UsernameNotFoundException("Username '" + username
					+ "' is not found");
		}

		UserDetails user= users.get(0);
	
		return user;
	}
}
