package com.example.rrs.web;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.example.rrs.Constants;
import com.example.rrs.model.Connection;
import com.example.rrs.model.Connection.Status;
import com.example.rrs.model.User;
import com.example.rrs.security.SecurityUtils;
import com.example.rrs.service.ConnectionService;
import com.example.rrs.service.MailService;
import com.example.rrs.service.UserService;

@RequestMapping("/register")
@Controller
public class RegisterAction {

	private static final Logger log = LoggerFactory
			.getLogger(RegisterAction.class);

	@Inject
	private UserService userService;

	@Inject
	private ConnectionService connectionService;

	@Inject
	private MailService emailService;

	@Value("${app.baseUrl}")
	String appUrl;

	@Inject
	@Named("passwordEncoder")
	private PasswordEncoder passwordEncoder;

	void addDateTimeFormatPatterns(Model uiModel) {
		uiModel.addAttribute(
				"user_creationdate_date_format",
				DateTimeFormat.patternForStyle("M-",
						LocaleContextHolder.getLocale()));
		uiModel.addAttribute(
				"user_lastupdatedate_date_format",
				DateTimeFormat.patternForStyle("M-",
						LocaleContextHolder.getLocale()));
	}

	@RequestMapping(method = RequestMethod.POST)
	public String register(@Valid() RegisterForm registerForm,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (log.isDebugEnabled()) {
			log.debug("call create@");
			log.debug("registerForm@" + registerForm);
		}

		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, registerForm);
			return "register";
		}

		String email = registerForm.getEmail();

		if (!registerForm.getPassword()
				.equals(registerForm.getRepeatPassword())) {
			bindingResult.rejectValue("password", "password mismatch",
					"password mismatch");
			registerForm.setPassword(null);
			registerForm.setRepeatPassword(null);
			populateEditForm(uiModel, registerForm);
			return "register";
		}

		if (null != userService.findUserByEmail(email)) {
			bindingResult.rejectValue("email", "email is existed",
					"email is exsited!");
			populateEditForm(uiModel, registerForm);
			return "register";
		}

		uiModel.asMap().clear();
		User user = new User();

		BeanUtils.copyProperties(registerForm, user);

		user.setCreationDate(new Date());
		user.setPassword(passwordEncoder.encodePassword(user.getPassword(),
				user.salt()));

		// generating the confirmation code.
		String confirmationCode = KeyGenerators.string().generateKey();
		user.setConfirmationCode(confirmationCode);

		// save user data
		String inviterId = registerForm.getInviterId();
		if (inviterId != null) {
			user.setEnabled(true);
		}
		user = userService.saveUser(user);

		// if registered from other people in the RRS, skip the email activation
		// step.
		// and also add connection between them directly.
		if (inviterId != null) {
			if (log.isDebugEnabled()) {
				log.debug("register from inviter@"
						+ inviterId);
			}
			Connection conn = new Connection();
			conn.setConnectedDate(new Date());
			conn.setStatus(Status.ACCEPTED);
			conn.getUserIds().add(inviterId);
			conn.getUserIds().add(user.getId());
			connectionService.saveConnection(conn);
			
			return "redirect:/user/home";

		} else {

			// sending the activation email to user.
			if (log.isDebugEnabled()) {
				log.debug("sending activatation emal@" + email + ",code@"
						+ confirmationCode + ",baseUrl@" + appUrl);
			}

			Map emailModel = new HashMap();
			emailModel.put("email", email);
			emailModel.put("url", appUrl + "register/activate/"
					+ encodeUrlPathSegment(email, httpServletRequest) + "-"
					+ confirmationCode);
			emailModel.put("appUrl", appUrl);

			try {
				emailService.sendEmail(email, "Welcome to RSS",
						"signup-welcome", emailModel);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return "redirect:/registerOk";
		}
		
	}

	@RequestMapping(method = RequestMethod.GET, produces = { "text/html" })
	public String createForm(Model uiModel) {
		if (SecurityUtils.isAuthenticated()) {
			log.debug("redirect to user home page.");
			
		}

		populateEditForm(uiModel, new RegisterForm());
		return "register";
	}

	@RequestMapping(value = "/invitation-{inviterId}", method = RequestMethod.GET, produces = { "text/html" })
	public String createFormFromInvitation(
			@PathVariable("inviterId") String inviterId, Model uiModel) {
		if (SecurityUtils.isAuthenticated()) {
			log.debug("redirect to user home page.");
			return "redirect:/user/home";
		}

		RegisterForm form = new RegisterForm();
		form.setInviterId(inviterId);
		form.setInviterName(userService.findUser(inviterId).getName());
		populateEditForm(uiModel, form);
		return "register";
	}

	@RequestMapping(value = "/activate/{email}-{confirmationCode}", method = RequestMethod.GET, produces = { "text/html" })
	public String activate(
			@PathVariable("email") @NotNull @NotEmpty String email,
			@PathVariable("confirmationCode") @NotNull @NotEmpty String confirmationCode,
			RedirectAttributes attrs) {

		if (log.isDebugEnabled()) {
			log.debug("Activate the user account.");
		}

		User user = userService.findUserByEmail(email);

		if (user != null && confirmationCode.equals(user.getConfirmationCode())) {

			user.setEnabled(true);
			user.setConfirmationCode(null);
			userService.saveUser(user);

			attrs.addFlashAttribute(Constants.GLOBAL_MESSAGE,
					"User account is activated successfully.");

			return "redirect:/login";
		}

		return "redirect:/acitivateFailed";

	}

	String encodeUrlPathSegment(String pathSegment,
			HttpServletRequest httpServletRequest) {
		String enc = httpServletRequest.getCharacterEncoding();
		if (enc == null) {
			enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
		}
		try {
			pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
		} catch (UnsupportedEncodingException uee) {
		}
		return pathSegment;
	}

	void populateEditForm(Model uiModel, RegisterForm user) {
		uiModel.addAttribute("registerForm", user);
		addDateTimeFormatPatterns(uiModel);
	}

}
