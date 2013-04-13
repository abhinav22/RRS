package com.example.rrs.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.rrs.security.SecurityUtils;
import com.example.rrs.service.UserService;

@Controller
@RequestMapping(value="/user")
public class UserController {

	private static final Logger log = LoggerFactory
			.getLogger(UserController.class);

	@Autowired
	UserService userService;

	@RequestMapping(value = { "/home" }, method = RequestMethod.GET, produces = { "text/html" })
	public String home(Model uiModel) {

		uiModel.addAttribute("user", SecurityUtils.getCurrentUser());

		return "user/home";
	}

}
