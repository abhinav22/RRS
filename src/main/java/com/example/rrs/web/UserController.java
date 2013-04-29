package com.example.rrs.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.rrs.Constants;
import com.example.rrs.model.Avatar;
import com.example.rrs.model.Connection;
import com.example.rrs.model.EmailAddress;
import com.example.rrs.model.Link;
import com.example.rrs.model.Phone;
import com.example.rrs.model.Resource;
import com.example.rrs.model.ResourcePicture;
import com.example.rrs.model.SalutationLine;
import com.example.rrs.model.User;
import com.example.rrs.model.UserViewed;
import com.example.rrs.repository.AvatarRepository;
import com.example.rrs.repository.ResourcePictureRepository;
import com.example.rrs.security.SecurityUtils;
import com.example.rrs.service.ConnectionService;
import com.example.rrs.service.MailService;
import com.example.rrs.service.ResourceService;
import com.example.rrs.service.UserService;

@Controller
@RequestMapping(value = "/user")
public class UserController {

	private static final Logger log = LoggerFactory
			.getLogger(UserController.class);

	@Inject
	UserService userService;

	@Inject
	ConnectionService connectionService;

	@Inject
	ResourceService resourceService;

	@Inject
	AvatarRepository avatarRepository;

	@Inject
	ResourcePictureRepository resourcePictureRepository;

	@Inject
	MailService emailService;

	@Inject
	PasswordEncoder passwordEncoder;

	@Value("${app.baseUrl}")
	String appUrl;

	/*
	 * @InitBinder() public void initDataBinder(WebDataBinder dataBinder,
	 * WebRequest request) { User user = SecurityUtils.getCurrentUser(); if
	 * (log.isDebugEnabled()) {
	 * log.debug("call @initDataBinder, put user in request scope"); }
	 * request.setAttribute("user", user, RequestAttributes.SCOPE_REQUEST);
	 * 
	 * long connectinsCount = connectionService.countConnectionsForUser(user
	 * .getId()); long pendingConnectinsCount = connectionService
	 * .countPendingConnectionRequestsForUser(user.getId());
	 * 
	 * long viewedCount = userService.viewedCountInSevenDaysForUser(user
	 * .getId());
	 * 
	 * if (log.isDebugEnabled()) { log.debug("connectinsCount@" +
	 * connectinsCount); log.debug("pendingConnectinsCount@" +
	 * pendingConnectinsCount); log.debug("viewedCount@" + viewedCount); }
	 * 
	 * request.setAttribute("connectionsCount", connectinsCount,
	 * RequestAttributes.SCOPE_REQUEST);
	 * 
	 * request.setAttribute("pendingConnectionsCount", pendingConnectinsCount,
	 * RequestAttributes.SCOPE_REQUEST);
	 * 
	 * request.setAttribute("viewedCount", viewedCount,
	 * RequestAttributes.SCOPE_REQUEST); }
	 */

	@RequestMapping(value = { "/public-profile-{id}" }, method = RequestMethod.GET, produces = { "text/html" })
	public String publicProfile(@PathVariable("id") String id, Model uiModel,
			RedirectAttributes atts) {
		if (log.isDebugEnabled()) {
			log.debug("public profile @" + id);
		}

		userService.viewProfile(id);
		uiModel.addAttribute("profileId", id);
		return "user/public-profile";
	}

	@RequestMapping(value = { "/profile" }, method = RequestMethod.GET, produces = { "text/html" })
	public String readUserProfile(Model uiModel, RedirectAttributes atts) {
		if (log.isDebugEnabled()) {
			log.debug("read the profile of current user @");
		}

		return "user/profile";
	}

	@RequestMapping(value = { "/home" }, method = RequestMethod.GET, produces = { "text/html" })
	public String home(Model uiModel, RedirectAttributes atts) {

		User user = SecurityUtils.getCurrentUser();

		boolean nameAvailbility = nameIsSet(user);
		if (!nameAvailbility) {
			atts.addFlashAttribute(Constants.GLOBAL_MESSAGE,
					"Please setup your basic info!");
			return "redirect:/user/setuser";
		}
		//
		// if (!avatarIsSet(user)) {
		// atts.addFlashAttribute(Constants.GLOBAL_MESSAGE,
		// "Choose a personalized picture as avatar");
		// return "redirect:/user/setavatar";
		// }
		//
		// if (!alterEmailIsSet(user)) {
		// atts.addFlashAttribute(Constants.GLOBAL_MESSAGE,
		// " You can setup another email as an alternavite!");
		// return "redirect:/user/setemail";
		// }
		//
		// if (!linkIsSet(user)) {
		// atts.addFlashAttribute(Constants.GLOBAL_MESSAGE,
		// " Add a social link to your profile now !");
		// return "redirect:/user/setlink";
		// }
		//
		// if (!phoneIsSet(user)) {
		// atts.addFlashAttribute(Constants.GLOBAL_MESSAGE,
		// " Add a phone number !");
		// return "redirect:/user/setphone";
		// }

		return "user/home";
	}

	void populateUserForm(Model uiModel, UserForm user) {
		uiModel.addAttribute("salutationLines", SalutationLine.values());
		uiModel.addAttribute("userForm", user);
	}

	@RequestMapping(value = { "/setuser" }, method = RequestMethod.GET, produces = { "text/html" })
	public String setupUser(Model uiModel) {
		if (log.isDebugEnabled()) {
			log.debug("call setupUser....");
		}

		UserForm userForm = new UserForm();
		populateUserForm(uiModel, userForm);
		return "user/setuser";
	}

	@RequestMapping(value = { "/setuser" }, method = RequestMethod.POST, produces = { "text/html" })
	public String saveUser(@Valid UserForm userForm,
			BindingResult bindingResult, Model uiModel, RedirectAttributes atts) {
		if (log.isDebugEnabled()) {
			log.debug("call @saveUser to set up user:" + userForm);
		}

		if (bindingResult.hasErrors()) {
			populateUserForm(uiModel, userForm);
			return "user/setuser";
		}

		String[] skills = userForm.getSkillStr().split(",");

		if (skills.length > 5) {
			bindingResult.rejectValue("skillStr",
					"You can add 5 skills at most",
					"You can add 5 skills at most");
			populateUserForm(uiModel, userForm);
			return "user/setuser";
		}
		User user = SecurityUtils.getCurrentUser();

		user.getSkills().addAll(Arrays.asList(skills));

		user.setName(userForm.getName());
		user.setCurrentPlace(userForm.getCurrentPlace());
		user.setCurrentWork(userForm.getCurrentWork());

		if (userForm.getEmail() != null) {
			user.addEmail(new EmailAddress("OTHER", userForm.getEmail()));
		}

		if (userForm.getLink() != null) {
			user.addLink(new Link("OTHER", userForm.getLink()));
		}

		if (userForm.getPhoneNumber() != null) {
			user.addPhone(new Phone("OTHER", userForm.getPhoneNumber()));
		}

		userService.saveUser(user);

		SecurityContextHolder.getContext().setAuthentication(
				SecurityContextHolder.getContext().getAuthentication());

		atts.addFlashAttribute(Constants.GLOBAL_MESSAGE,
				"User info is set successfully");

		return "redirect:/user/recommended-resource";
	}

	void populateNameForm(Model uiModel, NameForm user) {
		uiModel.addAttribute("nameForm", user);
	}

	@RequestMapping(value = { "/setname" }, method = RequestMethod.GET, produces = { "text/html" })
	public String setupName(Model uiModel) {
		if (log.isDebugEnabled()) {
			log.debug("call setupName....");
		}
		User user = SecurityUtils.getCurrentUser();

		NameForm nameForm = new NameForm();
		// nameForm.setFirstName(user.getFirstName());
		// nameForm.setLastName(user.getLastName());
		// nameForm.setSalutationLine(user.getSalutationLine());
		populateNameForm(uiModel, nameForm);
		return "user/setname";
	}

	@RequestMapping(value = { "/setname" }, method = RequestMethod.POST, produces = { "text/html" })
	public String saveName(@Valid NameForm nameForm,
			BindingResult bindingResult, Model uiModel, RedirectAttributes atts) {
		if (log.isDebugEnabled()) {
			log.debug("call @saveName to set up name:" + nameForm);
		}

		if (bindingResult.hasErrors()) {
			populateNameForm(uiModel, nameForm);
			return "user/setname";
		}

		User user = SecurityUtils.getCurrentUser();

		BeanUtils.copyProperties(nameForm, user);

		userService.saveUser(user);

		SecurityContextHolder.getContext().setAuthentication(
				SecurityContextHolder.getContext().getAuthentication());

		atts.addFlashAttribute(Constants.GLOBAL_MESSAGE,
				"User name is set successfully");

		return "redirect:/user/home";
	}

	private boolean nameIsSet(User user) {
		if (log.isDebugEnabled()) {
			log.debug("call @nameIsSet");
		}

		if (user.getName() == null) {
			return false;
		}

		return true;
	}

	@RequestMapping(value = { "/setemail" }, method = RequestMethod.GET, produces = { "text/html" })
	public String setupEmail(Model uiModel) {
		populateEmailForm(uiModel, new EmailForm());
		return "user/setemail";
	}

	@RequestMapping(value = { "/setemail/verify-{email}-{confirmationCode}" }, method = RequestMethod.GET, produces = { "text/html" })
	public String verifyEmail(
			@PathVariable("email") @NotEmpty String email,
			@PathVariable("confirmationCode") @NotEmpty String confirmationCode,
			Model uiModel, RedirectAttributes atts) {
		User currentUser = SecurityUtils.getCurrentUser();

		for (EmailAddress emailAddress : currentUser.getEmails()) {
			if (confirmationCode.equals(emailAddress.getConfirmationCode())
					&& !emailAddress.isVerified()) {

				emailAddress.setConfirmationCode(null);
				emailAddress.setVerified(true);

				currentUser.removeEmail(email);
				currentUser.addEmail(emailAddress);

				userService.saveUser(currentUser);

				atts.addFlashAttribute(Constants.GLOBAL_MESSAGE, "Email "
						+ email + " is verified.");

				return "user/home";
			}

		}
		atts.addFlashAttribute(Constants.GLOBAL_MESSAGE, "Errors occured");
		return "redirect:/index";
	}

	private void populateEmailForm(Model uiModel, EmailForm emailForm) {
		uiModel.addAttribute("emailTypes", new String[] { "Private", "Work" });
		uiModel.addAttribute("emailForm", emailForm);
	}

	@RequestMapping(value = { "/setemail" }, method = RequestMethod.POST, produces = { "text/html" })
	public String saveEmail(@Valid EmailForm emailForm,
			BindingResult bindingResult, Model uiModel, RedirectAttributes atts) {
		if (log.isDebugEnabled()) {
			log.debug("call @saveEmail to set up name:" + emailForm);
		}

		if (bindingResult.hasErrors()) {
			populateEmailForm(uiModel, emailForm);
			return "user/setemail";
		}

		User user = SecurityUtils.getCurrentUser();

		EmailAddress email = new EmailAddress();

		BeanUtils.copyProperties(emailForm, email);

		user.addEmail(email);

		String confirmationCode = KeyGenerators.string().generateKey();
		email.setConfirmationCode(confirmationCode);

		userService.saveUser(user);

		Map model = new HashMap();
		model.put("url", appUrl + "user/setemail/verify-" + email.getContent()
				+ "-" + confirmationCode);
		model.put("userName", user.getName());
		model.put("appUrl", appUrl);

		emailService.sendEmail(email.getContent(),
				"Email verification is required", "email-verification", model);

		SecurityContextHolder.getContext().setAuthentication(
				SecurityContextHolder.getContext().getAuthentication());

		atts.addFlashAttribute(Constants.GLOBAL_MESSAGE,
				"The email is saved successfully");

		return "redirect:/user/home";
	}

	private boolean alterEmailIsSet(User user) {
		if (log.isDebugEnabled()) {
			log.debug("call @alterEmailIsSet");
		}

		if (user.getEmails().isEmpty()) {
			return false;
		}
		return true;
	}

	@RequestMapping(value = { "/setlink" }, method = RequestMethod.GET, produces = { "text/html" })
	public String setupLink(Model uiModel) {
		populateLinkForm(uiModel, new LinkForm());
		return "user/setlink";
	}

	@RequestMapping(value = { "/setlink" }, method = RequestMethod.POST, produces = { "text/html" })
	public String saveLink(@Valid LinkForm linkForm,
			BindingResult bindingResult, Model uiModel, RedirectAttributes atts) {
		if (log.isDebugEnabled()) {
			log.debug("call @saveLink to set up url:" + linkForm);
		}

		if (bindingResult.hasErrors()) {
			populateLinkForm(uiModel, linkForm);
			return "user/setlink";
		}

		User user = SecurityUtils.getCurrentUser();

		user.addLink(new Link(linkForm.getType(), linkForm.getContent()));

		userService.saveUser(user);

		SecurityContextHolder.getContext().setAuthentication(
				SecurityContextHolder.getContext().getAuthentication());

		atts.addFlashAttribute(Constants.GLOBAL_MESSAGE,
				"url is set successfully");

		return "redirect:/user/home";
	}

	private void populateLinkForm(Model uiModel, LinkForm linkForm) {
		uiModel.addAttribute("linkTypes", new String[] { "Google", "Twitter",
				"Facebook", "Other" });
		uiModel.addAttribute("linkForm", linkForm);
	}

	private boolean linkIsSet(User user) {
		if (log.isDebugEnabled()) {
			log.debug("call @urlIsSet");
		}

		if (user.getLinks().isEmpty()) {
			return false;
		}

		return true;
	}

	@RequestMapping(value = { "/setphone" }, method = RequestMethod.GET, produces = { "text/html" })
	public String setupPhone(Model uiModel) {
		populatePhoneForm(uiModel, new PhoneForm());
		return "user/setphone";
	}

	@RequestMapping(value = { "/setphone" }, method = RequestMethod.POST, produces = { "text/html" })
	public String savePhone(@Valid PhoneForm phoneForm,
			BindingResult bindingResult, Model uiModel, RedirectAttributes atts) {
		if (log.isDebugEnabled()) {
			log.debug("call @savePhone to set up url:" + phoneForm);
		}

		if (bindingResult.hasErrors()) {
			populatePhoneForm(uiModel, phoneForm);
			return "user/setphone";
		}

		User user = SecurityUtils.getCurrentUser();

		user.addPhone(new Phone(phoneForm.getType(), phoneForm.getContent()));

		userService.saveUser(user);

		SecurityContextHolder.getContext().setAuthentication(
				SecurityContextHolder.getContext().getAuthentication());

		atts.addFlashAttribute(Constants.GLOBAL_MESSAGE,
				"url is set successfully");

		return "redirect:/user/home";
	}

	private void populatePhoneForm(Model uiModel, PhoneForm phoneForm) {
		uiModel.addAttribute("phoneTypes", new String[] { "Home", "Office",
				"Other" });
		uiModel.addAttribute("phoneForm", phoneForm);
	}

	private boolean phoneIsSet(User user) {
		if (log.isDebugEnabled()) {
			log.debug("call @phoneIsSet");
		}

		if (user.getPhones().isEmpty()) {
			return false;
		}

		return true;
	}

	@RequestMapping(value = { "/setavatar" }, method = RequestMethod.GET, produces = { "text/html" })
	public String setupAvatar(Model uiModel) {
		if (log.isDebugEnabled()) {
			log.debug("call @setupAvatar:");
		}
		populateAvatarForm(uiModel, new AvatarForm());
		return "user/setavatar";
	}

	@RequestMapping(value = { "/setavatar" }, method = RequestMethod.POST, produces = { "text/html" })
	public String saveAvatar(@NotNull AvatarForm avatarForm, Model uiModel,
			RedirectAttributes atts) {
		if (log.isDebugEnabled()) {
			log.debug("call @saveAvatar @@@");
		}

		MultipartFile file = avatarForm.getFile();
		if (file.isEmpty()) {
			if (log.isDebugEnabled()) {
				log.debug("file field is empty , return  to upload page.");
			}
			return "user/setavatar";
		}

		User user = SecurityUtils.getCurrentUser();

		Avatar avatar = userService.findUserAvatar(user.getId());

		if (avatar == null) {
			populateAvatarForm(uiModel, new AvatarForm());
			log.debug("call new a avatar orbject @@@");
			avatar = new Avatar();
		}

		avatar.setName(file.getName());
		try {
			avatar.setContent(file.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		avatar.setSize(file.getSize());
		avatar.setMimeType(file.getContentType());

		avatar.setUserId(user.getId());

		avatar = avatarRepository.save(avatar);

		log.debug("avatar id @ " + avatar.getId() + " of user @"
				+ avatar.getUserId());

		user.setAvatarUrl("api/user/" + user.getId() + "/avatar");

		user = userService.saveUser(user);

		SecurityContextHolder.getContext().setAuthentication(
				SecurityContextHolder.getContext().getAuthentication());

		atts.addFlashAttribute(Constants.GLOBAL_MESSAGE,
				"avatar is set successfully");

		return "redirect:/user/home";
	}

	private void populateAvatarForm(Model uiModel, AvatarForm avatarForm) {
		uiModel.addAttribute("avatarForm", avatarForm);
	}

	private boolean avatarIsSet(User user) {
		if (log.isDebugEnabled()) {
			log.debug("call @avatarIsSet");
		}

		if (user.getAvatarUrl() == null || user.getAvatarUrl().length() == 0) {
			return false;
		}

		return true;
	}

	@RequestMapping(value = { "/setpassword" }, method = RequestMethod.GET, produces = { "text/html" })
	public String setupPassword(Model uiModel) {
		populatePasswordForm(uiModel, new PasswordForm());
		return "user/setpassword";
	}

	@RequestMapping(value = { "/setpassword" }, method = RequestMethod.POST, produces = { "text/html" })
	public String savePassword(@Valid PasswordForm passwordForm,
			BindingResult bindingResult, Model uiModel, HttpSession session,
			RedirectAttributes atts) {
		if (log.isDebugEnabled()) {
			log.debug("call @savePassword to set up url:" + passwordForm);
		}

		if (bindingResult.hasErrors()) {
			populatePasswordForm(uiModel, passwordForm);
			return "user/setpassword";
		}

		if (!passwordForm.getPassword()
				.equals(passwordForm.getRepeatPassword())) {
			bindingResult.rejectValue("password", "password mismatch",
					"password mismatch");
			passwordForm.setPassword(null);
			passwordForm.setRepeatPassword(null);
			populatePasswordForm(uiModel, passwordForm);
			return "user/setpassword";
		}

		User user = SecurityUtils.getCurrentUser();

		String encrypted = passwordEncoder.encodePassword(
				passwordForm.getPassword(), user.salt());
		if (log.isDebugEnabled()) {
			log.debug("new encoded password@" + encrypted);
		}

		user.setPassword(encrypted);

		userService.saveUser(user);

		SecurityContextHolder.getContext().setAuthentication(
				SecurityContextHolder.getContext().getAuthentication());

		atts.addFlashAttribute(Constants.GLOBAL_MESSAGE,
				"New password is set successfully, please login");

		// session.invalidate();

		return "redirect:/resources/j_spring_security_logout";
	}

	private void populatePasswordForm(Model uiModel, PasswordForm passwordForm) {
		uiModel.addAttribute("passwordForm", passwordForm);
	}

	@RequestMapping(value = { "/share-resource" }, method = RequestMethod.GET, produces = { "text/html" })
	public String setupResource(Model uiModel) {
		populateResourceForm(uiModel, new ResourceForm());
		return "user/share-resource";
	}

	@RequestMapping(value = { "/share-resource" }, method = RequestMethod.POST, produces = { "text/html" })
	public String saveResource(@Valid ResourceForm resourceForm,
			BindingResult bindingResult, Model uiModel, RedirectAttributes atts) {
		if (log.isDebugEnabled()) {
			log.debug("call @saveResource to set up url:" + resourceForm);
		}

		if (bindingResult.hasErrors()) {
			populateResourceForm(uiModel, resourceForm);
			return "user/share-resource";
		}

		String resourceStr = resourceForm.getTagStr();

		String[] tags = resourceStr.split(",");

		if (tags.length > 5) {
			bindingResult.rejectValue("tagSrc",
					"You can add 5 resources at most",
					"You can add 5 resources at most");
			populateResourceForm(uiModel, resourceForm);
			return "user/share-resource";
		}

		String pictureUrl = null;

		MultipartFile file = resourceForm.getFile();

		User user = SecurityUtils.getCurrentUser();

		Resource res = new Resource();

		res.setCreatedDate(new Date());
		res.setDescription(resourceForm.getDescription());
		res.setExternalLink(resourceForm.getLink());
		res.setLongDesc(resourceForm.getLongDesc());
		res.setName(resourceForm.getName());
		res.setUserId(user.getId());
		res.getTags().addAll(Arrays.asList(tags));

		res = resourceService.saveResource(res);

		if (!file.isEmpty()) {
			ResourcePicture pic = new ResourcePicture();
			try {
				pic.setContent(file.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			pic.setMimeType(file.getContentType());
			pic.setName(file.getName());
			pic.setResourceId(res.getId());
			pic.setSize(file.getSize());
			pic.setResourceId(res.getId());

			pic = resourcePictureRepository.save(pic);

			res.setPictureUrl("api/picture/" + pic.getId());
			res = resourceService.saveResource(res);
		}

		atts.addFlashAttribute(Constants.GLOBAL_MESSAGE,
				"Resource is shared successfully");

		return "redirect:/user/shared";
	}

	private void populateResourceForm(Model uiModel, ResourceForm resourceForm) {
		uiModel.addAttribute("resourceForm", resourceForm);
	}

	@RequestMapping(value = { "/viewed" }, method = RequestMethod.GET, produces = { "text/html" })
	public String viewed(Model uiModel, WebRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("viewed user");
		}

		User user = SecurityUtils.getCurrentUser();

		List<UserViewed> viewed = userService
				.viewedUsersInSevenDaysForUser(user.getId());

		List<UserViewedResult> result = new ArrayList<UserViewedResult>();
		for (UserViewed v : viewed) {
			result.add(new UserViewedResult(v.getViewedDate(), userService
					.findUser(v.getViewedBy())));
		}

		uiModel.addAttribute("viewedResult", result);

		return "user/viewed";
	}

	@RequestMapping(value = { "/connections" }, method = RequestMethod.GET, produces = { "text/html" })
	public String connections(Model uiModel, WebRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("connections...");
		}

		User user = SecurityUtils.getCurrentUser();

		String userId = user.getId();
		List<Connection> conns = connectionService
				.findConnectionsForUser(userId);

		List<UserConnectionResult> result = new ArrayList<UserConnectionResult>();

		for (Connection con : conns) {
			List<String> ids = con.getUserIds();
			String target = "";
			for (String _id : ids) {
				if (!_id.equals(userId)) {
					target = _id;
					break;
				}
			}

			String connStatus = connectionService.connectionStatus(ids.get(0),
					ids.get(1));
			result.add(new UserConnectionResult(userService.findUser(target),
					connStatus, con.getConnectedDate()));
		}

		uiModel.addAttribute("connResult", result);

		return "user/connections";
	}

	@RequestMapping(value = { "/pending-connections" }, method = RequestMethod.GET, produces = { "text/html" })
	public String pendingConnections(Model uiModel, WebRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("connections...");
		}

		User user = SecurityUtils.getCurrentUser();

		String userId = user.getId();
		List<Connection> conns = connectionService
				.findPendingConnectionsForUser(userId);

		List<UserConnectionResult> result = new ArrayList<UserConnectionResult>();

		for (Connection con : conns) {
			List<String> ids = con.getUserIds();
			String target = "";
			for (String _id : ids) {
				if (!_id.equals(userId)) {
					target = _id;
					break;
				}
			}

			String connStatus = connectionService.connectionStatus(ids.get(0),
					ids.get(1));
			result.add(new UserConnectionResult(userService.findUser(target),
					connStatus, con.getConnectedDate()));
		}

		uiModel.addAttribute("connResult", result);

		return "user/pending-connections";
	}

	@RequestMapping(value = { "/shared" }, method = RequestMethod.GET, produces = { "text/html" })
	public String shared(Model uiModel, WebRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("shared resource");
		}

		User user = SecurityUtils.getCurrentUser();

		List<Resource> result = resourceService.sharedResourceForUser(user
				.getId());

		uiModel.addAttribute("sharedResult", result);

		return "user/shared";
	}

	@RequestMapping(value = { "/liked" }, method = RequestMethod.GET, produces = { "text/html" })
	public String liked(Model uiModel, WebRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("liked resource");
		}

		User user = SecurityUtils.getCurrentUser();

		List<Resource> result = resourceService.likedResourcesForUser(user
				.getId());

		uiModel.addAttribute("likedResult", result);

		return "user/liked";
	}
	
	@RequestMapping(value = { "/recommended-resource" }, method = RequestMethod.GET, produces = { "text/html" })
	public String recommendedResource(Model uiModel) {
		return "user/recommended-resource";
	}

}
