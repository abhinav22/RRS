package com.example.rrs.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.rrs.security.SecurityUtils;

@Controller
public class IndexController {

	private static final Logger log = LoggerFactory
			.getLogger(IndexController.class);

	@RequestMapping(value = { "/index", "/home", "/" }, method = RequestMethod.GET, produces = { "text/html" })
	public String index(Model uiModel) {

		if (SecurityUtils.isAuthenticated()) {
			log.debug("redirect to user home page.");
			return "redirect:/user/home";
		}

		return "redirect:/register";
	}

	@RequestMapping(value = { "/login" }, method = RequestMethod.GET, produces = { "text/html" })
	public String login(Model uiModel) {

		if (SecurityUtils.isAuthenticated()) {
			log.debug("redirect to user home page.");
			return "redirect:/user/home";
		}

		return "login";
	}

}
