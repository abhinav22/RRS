package com.example.rrs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rrs.model.QUser;
import com.example.rrs.model.User;
import com.example.rrs.repository.UserRepository;
import com.example.rrs.security.SecurityUtils;
import com.example.rrs.web.SearchForm;
import com.mysema.query.types.Predicate;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Override
	public long countAllUsers() {
		return userRepository.count();
	}

	@Override
	public void deleteUser(User user) {
		userRepository.delete(user);
	}

	@Override
	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public User findUser(String id) {
		return userRepository.findOne(id);
	}

	@Override
	public List<User> findUserEntries(int firstResult, int maxResults) {
		return userRepository.findAll(
				new org.springframework.data.domain.PageRequest(firstResult
						/ maxResults, maxResults)).getContent();
	}

	@Override
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public User updateUser(User user) {
		return userRepository.save(user);
	}


	@Override
	public User findUserByEmail(String email) {
		List<User> users = userRepository.findByEmail(email);
		if (!users.isEmpty()) {
			return users.get(0);
		}
		return null;
	}

	@Override
	public Page<User> findConnectionsByKeywords(SearchForm searchForm,
			Pageable pageable) {

		QUser quser = QUser.user;

		User currentUser = SecurityUtils.getCurrentUser();
		
		Predicate predicate = null;
		//to do...
		return userRepository.findAll(predicate, pageable);
	}
}
