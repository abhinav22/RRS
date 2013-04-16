package com.example.rrs.web;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.rrs.Constants;
import com.example.rrs.model.SalutationLine;
import com.example.rrs.model.User;
import com.example.rrs.security.SecurityUtils;
import com.example.rrs.service.UserService;

@Controller
@RequestMapping(value = "/user")
public class UserController {

	private static final Logger log = LoggerFactory
			.getLogger(UserController.class);

	@Autowired
	UserService userService;

	@RequestMapping(value = { "/home" }, method = RequestMethod.GET, produces = { "text/html" })
	public String home(Model uiModel, RedirectAttributes atts) {

		User user = SecurityUtils.getCurrentUser();

		boolean nameAvailbility = checkNameAvailiblity(user);
		if (!nameAvailbility) {
			atts.addFlashAttribute(Constants.GLOBAL_MESSAGE,
					"First name and last name is not given, set up now!");
			return "redirect:/user/setname";
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
		if(log.isDebugEnabled()){
			log.debug("call @saveName to set up name:"+nameForm);
		}

		if (bindingResult.hasErrors()) {
			populateNameForm(uiModel, nameForm);
			return "user/setname";
		}
		
		User user = SecurityUtils.getCurrentUser();
		
		BeanUtils.copyProperties(nameForm, user);
		
		userService.saveUser(user);
		
		SecurityContextHolder.getContext().setAuthentication(SecurityContextHolder.getContext().getAuthentication());
		
		atts.addFlashAttribute(Constants.GLOBAL_MESSAGE, "User name is set successfully");
		
		return "redirect:/user/home";
	}

	private boolean checkNameAvailiblity(User user) {
		if (log.isDebugEnabled()) {
			log.debug("call @checkNameAvailiblity");
		}

		if (user.getFirstName() == null || user.getLastName() == null) {
			return false;
		}

		return true;
	}
}
