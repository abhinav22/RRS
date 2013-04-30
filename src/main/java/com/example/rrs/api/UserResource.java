package com.example.rrs.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.rrs.model.Avatar;
import com.example.rrs.model.User;
import com.example.rrs.security.SecurityUtils;
import com.example.rrs.service.ConnectionService;
import com.example.rrs.service.MailService;
import com.example.rrs.service.ResourceService;
import com.example.rrs.service.UserService;
import com.example.rrs.web.InviteForm;

@Controller
@RequestMapping(value = "/api/user")
public class UserResource extends RestApiResource {
	private static final Logger log = LoggerFactory
			.getLogger(UserResource.class);

	@Inject
	UserService userService;

	@Inject
	ConnectionService connectionService;

	@Inject
	ResourceService resourceService;

	@Inject
	MailService emailService;

	@Value("${app.baseUrl}")
	String appUrl;

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

	@RequestMapping(value = "/s-{keyword}-{page}-{size}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<List<User>> search(
			@PathVariable("keyword") String keyword,
			@PathVariable("page") @Min(value = 0) int page,
			@PathVariable("size") @Min(value = 1) int size) {
		if (log.isDebugEnabled()) {
			log.debug("call @search");
		}

		List<User> results = userService.searchUser(keyword, page, size);

		if (log.isDebugEnabled()) {
			log.debug("results size @" + results.size());
		}
		return new ResponseEntity<List<User>>(results, HttpStatus.OK);
	}

	@RequestMapping(value = "/c-{keyword}", method = RequestMethod.GET, produces = { MediaType.TEXT_PLAIN_VALUE })
	@ResponseBody
	public ResponseEntity<String> countSearch(
			@PathVariable("keyword") String keyword) {
		if (log.isDebugEnabled()) {
			log.debug("call @search");
		}

		long resultSize = userService.countSearchUser(keyword);

		if (log.isDebugEnabled()) {
			log.debug("results size @" + resultSize);
		}
		return new ResponseEntity<String>(String.valueOf(resultSize),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/invite", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<String> sendInvitation(
			@Valid() @RequestBody InviteForm inviteForm,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest, RedirectAttributes attrs) {
		if (log.isDebugEnabled()) {
			log.debug("call create@");
			log.debug("InviteForm@" + inviteForm);
		}

		User currentUser = SecurityUtils.getCurrentUser();

		List<String> emails = inviteForm.getEmails();

		Map model = new HashMap();
		model.put("inviter", currentUser.getName());
		model.put("url", appUrl + "register/invitation-" + currentUser.getId());
		model.put("appUrl", appUrl);
		model.put("extra", inviteForm.getExtra());

		String html = emailService.renderMailTemplate("signup-invitation",
				model, false);

		try {
			for (String e : emails) {
				emailService.sendEmail(e, inviteForm.getTitle(),
						inviteForm.getContent(), html);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/invite-check",  method = RequestMethod.POST, produces = { MediaType.TEXT_PLAIN_VALUE })
	@ResponseBody
	public ResponseEntity<String> checkExistedEmail(
			@RequestBody String email) {

		boolean existed = userService.findUserByEmail(email) != null;

		if (log.isDebugEnabled()) {
			log.debug("check user existed @" + email + ", result@" + existed);
		}

		return new ResponseEntity<String>(String.valueOf(existed),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/invite", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<InviteForm> createForm(Model uiModel,
			HttpServletRequest httpServletRequest) {
		InviteForm data = new InviteForm();

		User currentUser = SecurityUtils.getCurrentUser();

		Map model = new HashMap();
		model.put("inviter", currentUser.getName());
		model.put("url", appUrl + "register/invitation-" + currentUser.getId());
		model.put("appUrl", appUrl);

		String html = emailService.renderMailTemplate("signup-invitation",
				model, false);

		data.setContent(html);
		data.setTitle(currentUser.getName() + " sent you an invitation");

		if (log.isDebugEnabled()) {
			log.debug("inviteForm@" + data);
		}

		return new ResponseEntity<InviteForm>(data, HttpStatus.OK);
	}
}
