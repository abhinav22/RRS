package com.example.rrs.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.velocity.VelocityEngineUtils;
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

@RequestMapping("/invite")
@Controller
public class InviteAction {

	private static final Logger log = LoggerFactory
			.getLogger(InviteAction.class);

	@Inject
	private UserService userService;

	@Inject
	private MailService emailService;

	@Value("${app.baseUrl}")
	String appUrl;

	// void addDateTimeFormatPatterns(Model uiModel) {
	// uiModel.addAttribute(
	// "user_creationdate_date_format",
	// DateTimeFormat.patternForStyle("M-",
	// LocaleContextHolder.getLocale()));
	// uiModel.addAttribute(
	// "user_lastupdatedate_date_format",
	// DateTimeFormat.patternForStyle("M-",
	// LocaleContextHolder.getLocale()));
	// }
	//
	// @RequestMapping(method = RequestMethod.POST)
	// public String register(@Valid() InviteForm inviteForm,
	// BindingResult bindingResult, Model uiModel,
	// HttpServletRequest httpServletRequest, RedirectAttributes attrs) {
	// if (log.isDebugEnabled()) {
	// log.debug("call create@");
	// log.debug("InviteForm@" + inviteForm);
	// }
	//
	// if (bindingResult.hasErrors()) {
	// populateEditForm(uiModel, inviteForm);
	// return "register";
	// }
	//
	// String email = inviteForm.getEmails();
	//
	// List<String> emailList = new ArrayList<String>();
	//
	// String[] emails = email.split(",");
	//
	// List<String> existedEmails = new ArrayList<String>();
	//
	// if (emails.length > 0) {
	// for (String e : emails) {
	// if (null != userService.findUserByEmail(e)) {
	// existedEmails.add(e);
	// } else {
	// emailList.add(e);
	// }
	// }
	// }
	//
	// try {
	// for (String e : emailList) {
	// emailService.sendEmail(e, inviteForm.getTitle(),
	// inviteForm.getContent(), inviteForm.getContent());
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// String message = "Invitations are sent successfully!";
	// if (!existedEmails.isEmpty()) {
	// String existed = StringUtils.join(existedEmails, ",");
	// message += existed + " are existed in the application and ignored.";
	//
	// }
	//
	// attrs.addFlashAttribute(Constants.GLOBAL_MESSAGE, message);
	//
	// return "redirect:/user/home";
	// }
	//
	// @RequestMapping(method = RequestMethod.GET, produces = { "text/html" })
	// public String createForm(Model uiModel,
	// HttpServletRequest httpServletRequest) {
	// InviteForm data = new InviteForm();
	//
	// User currentUser = SecurityUtils.getCurrentUser();
	//
	// Map model = new HashMap();
	// model.put("inviter", currentUser.getName());
	// model.put("url", appUrl + "register/invitation-"
	// + encodeUrlPathSegment(currentUser.getId(), httpServletRequest));
	// model.put("appUrl", appUrl);
	//
	//
	// String html = emailService.renderMailTemplate("signup-invitation", model,
	// false);
	//
	// data.setContent(html);
	// data.setTitle(currentUser.getName() + " sent you an invitation");
	//
	// if (log.isDebugEnabled()) {
	// log.debug("inviteForm@" + data);
	// }
	//
	// populateEditForm(uiModel, data);
	// return "invite/home";
	// }
	//
	// String encodeUrlPathSegment(String pathSegment,
	// HttpServletRequest httpServletRequest) {
	// String enc = httpServletRequest.getCharacterEncoding();
	// if (enc == null) {
	// enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
	// }
	// try {
	// pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
	// } catch (UnsupportedEncodingException uee) {
	// }
	// return pathSegment;
	// }
	//
	// void populateEditForm(Model uiModel, InviteForm user) {
	// uiModel.addAttribute("inviteForm", user);
	// addDateTimeFormatPatterns(uiModel);
	// }

	@RequestMapping(method = RequestMethod.GET, produces = { "text/html" })
	public String inviteForm() {
		if (log.isDebugEnabled()) {
			log.debug("go to invite form");
		}
	
		return "invite/home";
	}

}
