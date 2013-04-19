package com.example.rrs.api;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.rrs.model.Picture;
import com.example.rrs.model.User;
import com.example.rrs.service.UserService;

@Controller
public class UserResource extends BaseApiController {
	private static final Logger log = LoggerFactory
			.getLogger(UserResource.class);

	@Inject
	UserService userService;

	@RequestMapping(value = "/user/profile-{id}", method = RequestMethod.GET)
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

	@RequestMapping(value = "/user/profile-{id}", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<User> saveProfile(@RequestBody User user,
			HttpServletResponse response) {
		userService.saveUser(user);
		return new ResponseEntity<User>(HttpStatus.OK);
	}

}
