package com.example.rrs.api;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.Min;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.rrs.model.Resource;
import com.example.rrs.model.ResourceComment;
import com.example.rrs.model.User;
import com.example.rrs.security.SecurityUtils;
import com.example.rrs.service.ConnectionService;
import com.example.rrs.service.ResourceService;
import com.example.rrs.service.UserService;
import com.example.rrs.web.ResourceCommentResult;
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

	@RequestMapping(value = "/{id}/detail", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<ResourceResult> get(@PathVariable("id") String id) {
		if (log.isDebugEnabled()) {
			log.debug("call @get resource:" + id);
		}

		User user = SecurityUtils.getCurrentUser();
		Resource r = resourceService.findResource(id);

		ResourceResult result = new ResourceResult();
		result.setResource(r);

		result.setCommentedCount(resourceService.countCommentedForResource(r
				.getId()));

		result.setLiked(resourceService.resourceLikedForUser(user.getId(),
				r.getId()) != null);
		result.setLikedCount(resourceService.countLikedForResource(r.getId()));
		result.setSharedBy(userService.findUser(r.getUserId()));

		return new ResponseEntity<ResourceResult>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/comments", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<List<ResourceCommentResult>> commentsForResource(
			@PathVariable("id") String id) {
		if (log.isDebugEnabled()) {
			log.debug("call @commentsForResource:" + id);
		}

		List<ResourceComment> cmts = resourceService.commentsForResource(id);

		List<ResourceCommentResult> comments = new ArrayList<ResourceCommentResult>();
		for (ResourceComment c : cmts) {

			ResourceCommentResult result = new ResourceCommentResult(
					userService.findUser(c.getUserId()), c);
			comments.add(result);
		}

		return new ResponseEntity<List<ResourceCommentResult>>(comments,
				HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/like", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity likeResource(@PathVariable("id") String id) {
		if (log.isDebugEnabled()) {
			log.debug("call @likeResource=" + id);
		}

		User user = SecurityUtils.getCurrentUser();
		resourceService.likeResource(user.getId(), id);
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/comment", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity commentResource(@RequestBody String comment,
			@PathVariable("id") String id) {
		if (log.isDebugEnabled()) {
			log.debug("call @commentResource, resourceid =" + id + ", comment="
					+ comment);
		}

		User user = SecurityUtils.getCurrentUser();
		resourceService.commentResource(user.getId(), id, comment);
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(value = "/s-{keyword}-{page}-{size}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<List<ResourceResult>> search(
			@PathVariable("keyword") String keyword,
			@PathVariable("page") @Min(value = 0) int page,
			@PathVariable("size") @Min(value = 1) int size) {
		if (log.isDebugEnabled()) {
			log.debug("call @search");
		}

		List<Resource> res = resourceService
				.searchResource(keyword, page, size);

		List<ResourceResult> results = new ArrayList<ResourceResult>();

		User user = SecurityUtils.getCurrentUser();

		for (Resource r : res) {
			ResourceResult result = new ResourceResult();
			result.setResource(r);

			result.setCommentedCount(resourceService
					.countCommentedForResource(r.getId()));

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

	@RequestMapping(value = "/c-{keyword}", method = RequestMethod.GET, produces = { MediaType.TEXT_PLAIN_VALUE })
	@ResponseBody
	public ResponseEntity<String> countSearch(
			@PathVariable("keyword") String keyword) {
		if (log.isDebugEnabled()) {
			log.debug("call @search");
		}

		long resourceSize = resourceService.countSearchResource(keyword);

		if (log.isDebugEnabled()) {
			log.debug("results size @" + resourceSize);
		}
		return new ResponseEntity<String>(String.valueOf(resourceSize),
				HttpStatus.OK);
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

			result.setCommentedCount(resourceService
					.countCommentedForResource(r.getId()));

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
