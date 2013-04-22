package com.example.rrs.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rrs.model.Avatar;
import com.example.rrs.model.QAvatar;
import com.example.rrs.model.QUser;
import com.example.rrs.model.User;
import com.example.rrs.repository.AvatarRepository;
import com.example.rrs.repository.UserRepository;
import com.example.rrs.security.SecurityUtils;
import com.example.rrs.web.SearchForm;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.Predicate;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	private static final Logger log = LoggerFactory
			.getLogger(UserServiceImpl.class);

	@Autowired
	UserRepository userRepository;

	@Inject
	AvatarRepository avatarRepository;

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
		// to do...
		return userRepository.findAll(predicate, pageable);
	}

	@Override
	public Avatar findUserAvatar(User _user) {
		if (log.isDebugEnabled()) {
			log.debug("find user avatar by user@" + _user);
		}

		List<Avatar> all = (List<Avatar>) avatarRepository.findAll();

		if (log.isDebugEnabled()) {
			log.debug("all  size @" + all.size());
			for (Avatar a : all) {
				log.debug(" avatar @ id=" + a.getId() + ", user id="
						+ a.getUser().getId());
			}
		}

		List<Avatar> avatars = (List<Avatar>) avatarRepository
				.findAll(QAvatar.avatar.user.eq(_user));

		if (log.isDebugEnabled()) {
			log.debug("avatars size @" + avatars.size());
		}

		if (!avatars.isEmpty()) {
			return avatars.get(0);
		}

		return null;
	}

}
