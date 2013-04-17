package com.example.rrs.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.rrs.Constants;
import com.example.rrs.model.EmailAddress;
import com.example.rrs.model.Link;
import com.example.rrs.model.Phone;
import com.example.rrs.model.SalutationLine;
import com.example.rrs.model.User;
import com.example.rrs.security.SecurityUtils;
import com.example.rrs.service.MailService;
import com.example.rrs.service.UserService;

@Controller
@RequestMapping(value = "/user")
public class UserController {

	private static final Logger log = LoggerFactory
			.getLogger(UserController.class);

	@Inject
	UserService userService;

	@Inject
	MailService emailService;

	@Value("${app.baseUrl}")
	String appUrl;

	@RequestMapping(value = { "/home" }, method = RequestMethod.GET, produces = { "text/html" })
	public String home(Model uiModel, RedirectAttributes atts) {

		User user = SecurityUtils.getCurrentUser();

		boolean nameAvailbility = nameIsSet(user);
		if (!nameAvailbility) {
			atts.addFlashAttribute(Constants.GLOBAL_MESSAGE,
					"First name and last name is not given, set up now!");
			return "redirect:/user/setname";
		}

		if (!alterEmailIsSet(user)) {
			atts.addFlashAttribute(Constants.GLOBAL_MESSAGE,
					" You can setup another email as an alternavite!");
			return "redirect:/user/setemail";
		}

		if (!linkIsSet(user)) {
			atts.addFlashAttribute(Constants.GLOBAL_MESSAGE,
					" Add a social link to your profile now !");
			return "redirect:/user/setlink";
		}
		
		if (!phoneIsSet(user)) {
			atts.addFlashAttribute(Constants.GLOBAL_MESSAGE,
					" Add a phone number !");
			return "redirect:/user/setphone";
		}

		uiModel.addAttribute("user", user);
		return "user/home";
	}

	void populateNameForm(Model uiModel, NameForm user) {
		uiModel.addAttribute("salutationLines", SalutationLine.values());
		uiModel.addAttribute("nameForm", user);
	}

	@RequestMapping(value = { "/setname" }, method = RequestMethod.GET, produces = { "text/html" })
	public String setupName(Model uiModel) {
		populateNameForm(uiModel, new NameForm());

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

		if (user.getFirstName() == null || user.getLastName() == null) {
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

		if (!currentUser.getEmails().containsKey(email)) {

			EmailAddress emailAddress = currentUser.getEmails().get(email);
			if (confirmationCode.equals(emailAddress.getConfirmationCode())
					&& !emailAddress.isVerified()) {

				emailAddress.setConfirmationCode(null);
				emailAddress.setVerified(true);

				currentUser.removeEmail(email);
				currentUser.addEmail(email, emailAddress);

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

		user.addEmail(emailForm.getContent(), email);

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

		user.addLink(linkForm.getContent(), new Link(linkForm.getType(),
				linkForm.getContent()));

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

		user.addPhone(phoneForm.getContent(), new Phone(phoneForm.getType(),
				phoneForm.getContent()));

		userService.saveUser(user);

		SecurityContextHolder.getContext().setAuthentication(
				SecurityContextHolder.getContext().getAuthentication());

		atts.addFlashAttribute(Constants.GLOBAL_MESSAGE,
				"url is set successfully");

		return "redirect:/user/home";
	}

	private void populatePhoneForm(Model uiModel, PhoneForm phoneForm) {
		uiModel.addAttribute("phoneTypes", new String[] { "Google", "Twitter",
				"Facebook", "Other" });
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

}
