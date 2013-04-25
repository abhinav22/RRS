package com.example.rrs.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.rrs.model.Avatar;
import com.example.rrs.model.QAvatar;
import com.example.rrs.model.QUser;
import com.example.rrs.model.QUserViewed;
import com.example.rrs.model.User;
import com.example.rrs.model.UserViewed;
import com.example.rrs.repository.AvatarRepository;
import com.example.rrs.repository.UserRepository;
import com.example.rrs.repository.UserViewedRepository;
import com.example.rrs.security.SecurityUtils;
import com.example.rrs.web.SearchForm;
import com.mysema.query.types.Predicate;

@Service
public class UserService {
	private static final Logger log = LoggerFactory
			.getLogger(UserService.class);

	@Autowired
	UserRepository userRepository;

	@Inject
	AvatarRepository avatarRepository;

	@Inject
	UserViewedRepository userViewedRepository;

	public long countAllUsers() {
		return userRepository.count();
	}

	public void deleteUser(User user) {
		userRepository.delete(user);
	}

	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	public User findUser(String id) {
		return userRepository.findOne(id);
	}

	public List<User> findUserEntries(int firstResult, int maxResults) {
		return userRepository.findAll(
				new org.springframework.data.domain.PageRequest(firstResult
						/ maxResults, maxResults)).getContent();
	}

	public User saveUser(User user) {
		return userRepository.save(user);
	}

	public User updateUser(User user) {
		return userRepository.save(user);
	}

	public User findUserByEmail(String email) {
		List<User> users = userRepository.findByEmail(email);
		if (!users.isEmpty()) {
			return users.get(0);
		}
		return null;
	}

	public Page<User> findConnectionsByKeywords(SearchForm searchForm,
			Pageable pageable) {

		QUser quser = QUser.user;

		User currentUser = SecurityUtils.getCurrentUser();

		Predicate predicate = null;
		// to do...
		return userRepository.findAll(predicate, pageable);
	}

	public Avatar findUserAvatar(String _userId) {
		if (log.isDebugEnabled()) {
			log.debug("find user avatar by user@" + _userId);
		}

		List<Avatar> all = (List<Avatar>) avatarRepository.findAll();

		if (log.isDebugEnabled()) {
			log.debug("all  size @" + all.size());
			for (Avatar a : all) {
				log.debug(" avatar @ id=" + a.getId() + ", user id="
						+ a.getUserId());
			}
		}

		List<Avatar> avatars = (List<Avatar>) avatarRepository
				.findAll(QAvatar.avatar.userId.eq(_userId));

		if (log.isDebugEnabled()) {
			log.debug("avatars size @" + avatars.size());
		}

		if (!avatars.isEmpty()) {
			return avatars.get(0);
		}

		return null;
	}

	public void viewProfile(String id) {
		User currentUser = SecurityUtils.getCurrentUser();
		if (!currentUser.getId().equals(id)) {
			if (log.isDebugEnabled()) {
				log.debug("save user viewed log for user @" + id);
			}
			Date today = new Date();
			today = DateUtils.truncate(today, Calendar.DAY_OF_MONTH);

			QUserViewed quv = QUserViewed.userViewed;
			List<UserViewed> viewed = (List<UserViewed>) userViewedRepository
					.findAll(quv.userId.eq(id).and(
							quv.viewedBy.eq(currentUser.getId()).and(
									quv.viewedDate.eq(today))));
			if (log.isDebugEnabled()) {
				log.debug("user viewed log@user id:" + id + ", viewed by @"
						+ currentUser.getId() + ", today:" + today
						+ " viewed count today@" + viewed.size());
			}
			if (viewed.isEmpty()) {
				userViewedRepository.save(new UserViewed(id, currentUser
						.getId()));
			}
		}
	}

	public long viewedCountInSevenDaysForUser(String userId) {
		QUserViewed quv = QUserViewed.userViewed;

		Date date = DateUtils.addDays(new Date(), -7);
		date = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);

		if (log.isDebugEnabled()) {
			log.debug("7 days before is @" + date);
		}

		long cnt = userViewedRepository.count(quv.userId.eq(userId).and(
				quv.viewedDate.after(date)));

		if (log.isDebugEnabled()) {
			log.debug("viewedCount @" + cnt);
		}
		return cnt;
	}

	public List<UserViewed> viewedUsersInSevenDaysForUser(String userId) {
		QUserViewed quv = QUserViewed.userViewed;

		Date date = DateUtils.addDays(new Date(), -7);
		date = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);

		if (log.isDebugEnabled()) {
			log.debug("7 days before is @" + date);
		}

		List<UserViewed> viewed = (List<UserViewed>) userViewedRepository
				.findAll(quv.userId.eq(userId).and(quv.viewedDate.after(date)));

		if (log.isDebugEnabled()) {
			log.debug("viewed resultCount @" + viewed.size());
		}
		
		return viewed;
	}

}
