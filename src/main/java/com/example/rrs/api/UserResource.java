package com.example.rrs.api;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.rrs.model.Avatar;
import com.example.rrs.model.User;
import com.example.rrs.security.SecurityUtils;
import com.example.rrs.service.ConnectionService;
import com.example.rrs.service.UserService;

@Controller
@RequestMapping(value = "/api/user")
public class UserResource extends RestApiResource {
	private static final Logger log = LoggerFactory
			.getLogger(UserResource.class);

	@Inject
	UserService userService;

	@Inject
	ConnectionService connectionService;

	@RequestMapping(value = "/{id}/profile", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<User> readProfile(@PathVariable("id") String id,
			HttpServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("call @readProfile:" + id);
		}

		User user = userService.findUser(id);

		if (user != null) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			return new ResponseEntity<User>(user, headers, HttpStatus.OK);
		}

		return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/{id}/connection-{profileId}/status", method = RequestMethod.GET, produces = { MediaType.TEXT_PLAIN_VALUE })
	@ResponseBody
	public ResponseEntity<String> readConnectionStatus(
			@PathVariable("id") String id,
			@PathVariable("profileId") String profileId,
			HttpServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("call read connection of@" + id + ", prorifle id @"
					+ profileId);
		}

		String result = "";

		if (id.equals(profileId)) {
			result = "self";
		} else {
			result = connectionService.connectionStatus(id, profileId);
		}
		
		if (log.isDebugEnabled()) {
			log.debug("connection result@" + result);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		return new ResponseEntity<String>(result, headers, HttpStatus.OK);

	}

	@RequestMapping(value = "/{id}/connection-{profileId}/accept", method = RequestMethod.GET, produces = { MediaType.TEXT_PLAIN_VALUE })
	@ResponseBody
	public ResponseEntity<String> acceptConnection(
			@PathVariable("id") String id,
			@PathVariable("profileId") String profileId,
			HttpServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("call read connection of@" + id + ", prorifle id @"
					+ profileId);
		}

		connectionService.acceptConnection(profileId, id);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/connection-{profileId}/ignore", method = RequestMethod.GET, produces = { MediaType.TEXT_PLAIN_VALUE })
	@ResponseBody
	public ResponseEntity<String> ignoreConnection(
			@PathVariable("id") String id,
			@PathVariable("profileId") String profileId,
			HttpServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("call read connection of@" + id + ", prorifle id @"
					+ profileId);
		}

		connectionService.ignoreConnection(profileId, id);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/connection-{profileId}/send", method = RequestMethod.GET, produces = { MediaType.TEXT_PLAIN_VALUE })
	@ResponseBody
	public ResponseEntity<String> sendConnection(@PathVariable("id") String id,
			@PathVariable("profileId") String profileId,
			HttpServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("call read connection of@" + id + ", prorifle id @"
					+ profileId);
		}

		connectionService.sendConnection(id, profileId);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/me", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<User> readMyProfile() {
		if (log.isDebugEnabled()) {
			log.debug("call @readMyProfile:");
		}

		User user = SecurityUtils.getCurrentUser();

		if (log.isDebugEnabled()) {
			log.debug("current user@" + user.getId());
		}

		if (user != null) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			return new ResponseEntity<User>(user, headers, HttpStatus.OK);
		}

		return new ResponseEntity<User>(HttpStatus.FORBIDDEN);
	}

	@RequestMapping(headers = { "Accept=application/json" }, value = "/me", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<User> saveMyProfile(@RequestBody User user,
			HttpServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("save myProfile , consumes  data @" + user);
		}

		User _user = SecurityUtils.getCurrentUser();

		BeanUtils
				.copyProperties(user, _user, new String[] { "id", "password" });
		userService.saveUser(_user);
		return new ResponseEntity<User>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/avatar", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> readPicture(@PathVariable("id") String id,
			HttpServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("call @readPicture from user:" + id);
		}

		Avatar data = userService.findUserAvatar(id);

		if (log.isDebugEnabled()) {
			log.debug("read avatar data@" + data);
		}

		if (data != null) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentLength(data.getSize());
			headers.setContentType(MediaType.valueOf(data.getMimeType()));

			return new ResponseEntity<byte[]>(data.getContent(), headers,
					HttpStatus.OK);
		}

		return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
	}

}
