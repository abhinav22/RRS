package com.example.rrs.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.rrs.model.Avatar;
import com.example.rrs.model.User;
import com.example.rrs.web.SearchForm;

public interface UserService {

	public abstract long countAllUsers();

	public abstract void deleteUser(User user);

	public abstract List<User> findAllUsers();

	public abstract User findUser(String id);

	public abstract List<User> findUserEntries(int firstResult, int maxResults);

	public abstract User saveUser(User user);

	public abstract User updateUser(User user);

	public abstract User findUserByEmail(String email);

	public abstract Page findConnectionsByKeywords(SearchForm searchForm,
			Pageable pageable);

	
	public abstract Avatar findUserAvatar(String userId);
}
