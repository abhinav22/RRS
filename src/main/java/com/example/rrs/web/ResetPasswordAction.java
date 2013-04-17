package com.example.rrs.web;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.example.rrs.Constants;
import com.example.rrs.model.User;
import com.example.rrs.security.SecurityUtils;
import com.example.rrs.service.MailService;
import com.example.rrs.service.UserService;

@RequestMapping("/resetpwd")
@Controller
public class ResetPasswordAction {

	private static final Logger log = LoggerFactory
			.getLogger(ResetPasswordAction.class);

	@Inject
	private UserService userService;

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

	@RequestMapping(method = RequestMethod.GET, produces = { "text/html" })
	public String initResetPassword(Model uiModel) {
		if (log.isDebugEnabled()) {
			log.debug("initial resetpassword.");
		}

		if (SecurityUtils.getCurrentUser() != null) {
			return "redirect:/user/home";
		}

		uiModel.addAttribute("resetPasswordForm", new ResetPasswordForm());
		return "resetpwd";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String resetPassword(@Valid() ResetPasswordForm resetPasswordForm,
			BindingResult bindingResult, Model uiModel, RedirectAttributes atts) {
		String email = resetPasswordForm.getEmail();

		if (log.isDebugEnabled()) {
			log.debug(" send reset passwordemail to @" + email);
		}
		
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("resetPasswordForm", resetPasswordForm);
			return "resetpwd";
		}

		User user = userService.findUserByEmail(email);
		if (user == null) {
			bindingResult.rejectValue("email",
					"email is not existed in the application",
					"email is not existed in the application");

			uiModel.addAttribute("resetPasswordForm", resetPasswordForm);
			return "resetpwd";
		}

		String newPassword = KeyGenerators.string().generateKey();

		if (log.isDebugEnabled()) {
			log.debug("new password@" + newPassword);
		}

		user.setPassword(passwordEncoder.encodePassword(newPassword,
				user.salt()));

		userService.saveUser(user);

		//send new password by email.
		Map model = new HashMap();
		model.put("userName", user.getName());
		model.put("newPassword", newPassword);
		model.put("appUrl", appUrl);

		emailService.sendEmail(email, "Password is reset!", "reset-password",
				model);

		atts.addFlashAttribute(Constants.GLOBAL_MESSAGE,
				"The new password is sent to you by email, please check your inbox.");

		return "redirect:/login";
	}
}
