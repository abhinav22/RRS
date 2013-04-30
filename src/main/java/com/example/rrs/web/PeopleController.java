package com.example.rrs.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/people")
public class PeopleController {
	private static final Logger log = LoggerFactory
			.getLogger(PeopleController.class);

	@RequestMapping()
	public String home(Model uiModel) {
		if (log.isDebugEnabled()) {
			log.debug(" user/people home");
		}
		return "people/home";
	}

//	@RequestMapping(value = "/{id}")
//	public String viewDetails(@PathVariable("id") String id, Model uiModel) {
//
//		uiModel.addAttribute("profileId", "id");
//		return "people/details";
//	}

}
