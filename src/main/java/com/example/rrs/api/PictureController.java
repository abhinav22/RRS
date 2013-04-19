package com.example.rrs.api;

import javax.activation.MimeType;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.rrs.model.Picture;
import com.example.rrs.repository.PictureRepository;

@Controller
public class PictureController extends BaseApiController {
	private static final Logger log = LoggerFactory
			.getLogger(PictureController.class);

	@Inject
	PictureRepository pictureRepostory;

	@RequestMapping("/pic-{id}")
	@ResponseBody
	public ResponseEntity<byte[]> readPicture(@PathVariable("id") String id,
			HttpServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("call @readPicture:" + id);
		}

		Picture data = pictureRepostory.findOne(id);

		if (data != null) {

			HttpHeaders headers = new HttpHeaders();
			headers.setContentLength(data.getSize());
			headers.setContentType(MediaType.valueOf(data.getMimeType()));

			return new ResponseEntity<byte[]>(data.getContent(), headers, HttpStatus.OK);
		}

		throw new EntityNotFoundException();
	}

}
