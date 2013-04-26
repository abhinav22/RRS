package com.example.rrs.api;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.rrs.model.Resource;
import com.example.rrs.model.User;
import com.example.rrs.security.SecurityUtils;
import com.example.rrs.service.ConnectionService;
import com.example.rrs.service.ResourceService;
import com.example.rrs.service.UserService;
import com.example.rrs.web.ResourceResult;

@Controller
@RequestMapping(value = "/api/res")
public class ResResource extends RestApiResource {
	private static final Logger log = LoggerFactory
			.getLogger(ResResource.class);

	@Inject
	UserService userService;

	@Inject
	ConnectionService connectionService;

	@Inject
	ResourceService resourceService;

	@RequestMapping(value = "/{id}/like", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity likeResource(@PathVariable("id") String id) {
		if (log.isDebugEnabled()) {
			log.debug("call @recommendedResource");
		}
		
		User user = SecurityUtils.getCurrentUser();
		resourceService.likeResource(user.getId(), id);		
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(value = "/recommended", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<List<ResourceResult>> recommendedResource() {
		if (log.isDebugEnabled()) {
			log.debug("call @recommendedResource");
		}

		List<Resource> res = resourceService.recommendedResource(5);

		List<ResourceResult> results = new ArrayList<ResourceResult>();

		User user = SecurityUtils.getCurrentUser();

		for (Resource r : res) {
			ResourceResult result = new ResourceResult();
			result.setResource(r);
			result.setCommentedCount(0);// TODO

			result.setLiked(resourceService.resourceLikedForUser(user.getId(),
					r.getId()) != null);
			result.setLikedCount(resourceService.countLikedForResource(r
					.getId()));
			result.setSharedBy(userService.findUser(r.getUserId()));
			results.add(result);
		}

		if (log.isDebugEnabled()) {
			log.debug("results size @" + results.size());
		}
		return new ResponseEntity<List<ResourceResult>>(results, HttpStatus.OK);
	}
}
