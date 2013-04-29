package com.example.rrs.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/people")
public class PeopleController {

	@RequestMapping(value = "/{id}")
	public String viewDetails(@PathVariable("id") String id, Model uiModel) {

		uiModel.addAttribute("profileId", "id");
		return "people/details";
	}

}
