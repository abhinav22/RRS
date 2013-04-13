package com.example.rrs.web;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.example.rrs.model.User;
import com.example.rrs.security.SecurityUtils;
import com.example.rrs.service.UserService;

@RequestMapping("/register")
@Controller
public class RegisterAction {

	private static final Logger log = LoggerFactory
			.getLogger(RegisterAction.class);

	@Autowired
	UserService userService;

	@Autowired
	@Qualifier("passwordEncoder")
	PasswordEncoder passwordEncoder;

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

		if (null != userService.findUserByEmail(registerForm.getEmail())) {
			bindingResult.rejectValue("email", "email is existed",
					"email is exsited!");
			populateEditForm(uiModel, registerForm);
			return "register";
		}

		uiModel.asMap().clear();
		User user = new User();

		BeanUtils.copyProperties(registerForm, user, new String[] { "address",
				"gender", "phone" });

		user.setCreationDate(new Date());
		user.setPassword(passwordEncoder.encodePassword(user.getPassword(),
				user.getSalt()));
		user = userService.saveUser(user);
		return "redirect:/user/home";
	}

	@RequestMapping(method = RequestMethod.GET, produces = { "text/html" })
	public String createForm(Model uiModel) {
		if (SecurityUtils.isAuthenticated()) {
			log.debug("redirect to user home page.");
			return "redirect:/user/home";
		}

		populateEditForm(uiModel, new RegisterForm());
		return "register";
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
