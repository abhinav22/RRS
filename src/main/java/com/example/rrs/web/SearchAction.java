package com.example.rrs.web;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefaults;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.example.rrs.model.User;
import com.example.rrs.service.UserService;

@RequestMapping("/search")
@Controller
public class SearchAction {

	private static final Logger log = LoggerFactory.getLogger(SearchAction.class);

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
	public String search(SearchForm searchForm,
			@PageableDefaults(pageNumber=0,value=10, sort="creationDate", sortDir=Direction.DESC) Pageable pageable,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (log.isDebugEnabled()) {
			log.debug("call create@");
			log.debug("searchForm@" + searchForm);
		}

		populateEditForm(uiModel, searchForm);
		
		Page<User> page=userService.findConnectionsByKeywords(searchForm, pageable);
		
		uiModel.addAttribute("searchResults", page.getContent());

		return "search";
	}

	@RequestMapping(method = RequestMethod.GET, produces = { "text/html" })
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new SearchForm());
		return "search";
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

	void populateEditForm(Model uiModel, SearchForm user) {
		uiModel.addAttribute("searchForm", user);
		addDateTimeFormatPatterns(uiModel);
	}

}
