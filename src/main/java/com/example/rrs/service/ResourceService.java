package com.example.rrs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.rrs.model.QResource;
import com.example.rrs.model.QResourceComment;
import com.example.rrs.model.QResourceLiked;
import com.example.rrs.model.Resource;
import com.example.rrs.model.ResourceComment;
import com.example.rrs.model.ResourceLiked;
import com.example.rrs.model.User;
import com.example.rrs.repository.ResourceCommentRepository;
import com.example.rrs.repository.ResourceLikedRepository;
import com.example.rrs.repository.ResourceRepository;
import com.example.rrs.security.SecurityUtils;

@Service
public class ResourceService {
	private static final Logger log = LoggerFactory
			.getLogger(ResourceService.class);

	@Inject
	ResourceRepository resourceRepository;

	@Inject
	ResourceLikedRepository resourceLikedRepository;

	@Inject
	ResourceCommentRepository commentRepository;

	public List<Resource> sharedResourceForUser(String userId) {
		if (log.isDebugEnabled()) {
			log.debug("shared resource for user @" + userId);
		}
		QResource qres = QResource.resource;

		return (List<Resource>) resourceRepository.findAll(qres.userId
				.eq(userId));
	}

	public long countSharedForUser(String userId) {
		if (log.isDebugEnabled()) {
			log.debug("count shared resource for user @" + userId);
		}
		QResource qres = QResource.resource;

		return resourceRepository.count(qres.userId.eq(userId));
	}

	public List<Resource> likedResourcesForUser(String userId) {
		if (log.isDebugEnabled()) {
			log.debug("liked resource for user @" + userId);
		}
		QResourceLiked qlike = QResourceLiked.resourceLiked;

		List<ResourceLiked> likes = (List<ResourceLiked>) resourceLikedRepository
				.findAll(qlike.userId.eq(userId));

		List<Resource> res = new ArrayList<Resource>();
		for (ResourceLiked like : likes) {
			res.add(resourceRepository.findOne(like.getResourceId()));
		}

		return res;
	}

	public ResourceLiked resourceLikedForUser(String userId, String resourceId) {
		QResourceLiked qlike = QResourceLiked.resourceLiked;
		List<ResourceLiked> likes = (List<ResourceLiked>) resourceLikedRepository
				.findAll(qlike.userId.eq(userId).and(
						qlike.resourceId.eq(resourceId)));
		if (!likes.isEmpty()) {
			return likes.get(0);
		}

		return null;
	}

	public void likeResource(String userId, String resourceId) {
		if (log.isDebugEnabled()) {
			log.debug("like resource user@" + userId + "->" + resourceId);
		}

		QResourceLiked qlike = QResourceLiked.resourceLiked;

		List<ResourceLiked> likes = (List<ResourceLiked>) resourceLikedRepository
				.findAll(qlike.userId.eq(userId).and(
						qlike.resourceId.eq(resourceId)));

		if (likes.isEmpty()) {
			if (log.isDebugEnabled()) {
				log.debug("save resource liked @");
			}
			resourceLikedRepository.save(new ResourceLiked(userId, resourceId));
		}

	}

	public long countLikedForResource(String resourceId) {
		QResourceLiked qlike = QResourceLiked.resourceLiked;

		return resourceLikedRepository.count(qlike.resourceId.eq(resourceId));
	}

	public long countCommentedForResource(String resourceId) {
		QResourceComment qcomment = QResourceComment.resourceComment;
		return commentRepository.count(qcomment.resourceId.eq(resourceId));
	}

	public List<ResourceComment> commentsForResource(String resourceId) {
		QResourceComment qcomment = QResourceComment.resourceComment;
		return (List<ResourceComment>) commentRepository
				.findAll(qcomment.resourceId.eq(resourceId));
	}

	public long countLikedForUser(String userId) {
		QResourceLiked qlike = QResourceLiked.resourceLiked;

		return resourceLikedRepository.count(qlike.userId.eq(userId));
	}

	public Resource saveResource(Resource res) {
		return resourceRepository.save(res);
	}

	public List<Resource> recommendedResource(int i) {
		if (log.isDebugEnabled()) {
			log.debug("call @ recommendedResource@" + i);
		}
		QResource qres = QResource.resource;

		User user = SecurityUtils.getCurrentUser();

		List<String> skills = user.getSkills();
		String randomSkill = skills.get(new Random().nextInt(skills.size()));

		if (log.isDebugEnabled()) {
			log.debug("random skill @" + randomSkill);
		}

		PageRequest pr = new PageRequest(0, i);

		List<Resource> results = resourceRepository.findAll(
				qres.name.containsIgnoreCase(randomSkill)
						.or(qres.tags.any().containsIgnoreCase(randomSkill))
						.or(qres.description.containsIgnoreCase(randomSkill))
						.or(qres.longDesc.containsIgnoreCase(randomSkill)), pr)
				.getContent();

		if (log.isDebugEnabled()) {
			log.debug("result size() @" + results.size());
		}

		return results;
	}

	public void commentResource(String userId, String resourceId, String comment) {
		if (log.isDebugEnabled()) {
			log.debug("comment resource @userId =" + userId + ", resource id@"
					+ resourceId + ", comment=" + comment);
		}

		commentRepository
				.save(new ResourceComment(userId, resourceId, comment));
	}

	public List<Resource> searchResource(String keyword, int page, int size) {
		if (log.isDebugEnabled()) {
			log.debug("search  resource by keyword @keyword =" + keyword
					+ ", page@" + page + ", size=" + size);
		}
		QResource qres = QResource.resource;

		PageRequest pr = new PageRequest(page, size);

		List<Resource> results = resourceRepository.findAll(
				qres.name.containsIgnoreCase(keyword)
						.or(qres.tags.any().containsIgnoreCase(keyword))
						.or(qres.description.containsIgnoreCase(keyword))
						.or(qres.longDesc.containsIgnoreCase(keyword)), pr)
				.getContent();

		if (log.isDebugEnabled()) {
			log.debug("result size() @" + results.size());
		}

		return results;
	}

	public long countSearchResource(String keyword) {
		if (log.isDebugEnabled()) {
			log.debug("search  resource by keyword @keyword =" + keyword + "");
		}

		QResource qres = QResource.resource;

		return resourceRepository.count(qres.name.containsIgnoreCase(keyword)
				.or(qres.tags.any().containsIgnoreCase(keyword))
				.or(qres.description.containsIgnoreCase(keyword))
				.or(qres.longDesc.containsIgnoreCase(keyword)));
	}

	public Resource findResource(String id) {
		return resourceRepository.findOne(id);
	}

	public void deleteResource(String id) {
		resourceRepository.delete(id);
	}

}
