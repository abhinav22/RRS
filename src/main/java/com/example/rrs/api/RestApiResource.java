package com.example.rrs.api;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.rrs.model.ResourcePicture;
import com.example.rrs.repository.ResourcePictureRepository;

@RequestMapping("/api")
@Controller
public class RestApiResource {

	private static final Logger log = LoggerFactory
			.getLogger(RestApiResource.class);

	@Inject
	ResourcePictureRepository pictureRepository;

	@RequestMapping(value = "/picture/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> readPicture(@PathVariable("id") String id,
			HttpServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("call read picture @:" + id);
		}

		ResourcePicture data = pictureRepository.findOne(id);

		if (data != null) {
			if (log.isDebugEnabled()) {
				log.debug("found data@ ");
			}
			HttpHeaders headers = new HttpHeaders();
			headers.setContentLength(data.getSize());
			headers.setContentType(MediaType.valueOf(data.getMimeType()));

			return new ResponseEntity<byte[]>(data.getContent(), headers,
					HttpStatus.OK);
		}

		return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
	}

}
