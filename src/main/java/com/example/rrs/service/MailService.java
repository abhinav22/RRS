package com.example.rrs.service;

import java.util.Date;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

@Service
@Named("mailService")
public class MailService {

	private static final Logger log = LoggerFactory
			.getLogger(MailService.class);

	@Inject
	private JavaMailSender mailSender;

	@Inject
	private VelocityEngine velocityEngine;

	@Inject
	private PasswordEncoder encoder;

	@Value("${mail.from}")
	private String from;
	private final static String TEMPLATE_PATH_PREFIX = "/META-INF/mail/";
	private final static String HTML_TEMPLATE_SUFFIX = "-html";
	private final static String TEXT_TEMPLATE_SUFFIX = "-text";
	private final static String TEMPLATE_EXTENSION = ".vm";

	public void sendEmail(final String to, final String subject,
			final String velocityTemplateName, final Map model)
			throws MailException {
		this.sendEmail(to, null, subject, velocityTemplateName, model);
	}

	@Async("myExecutor")
	public void sendEmail(final String to, final String replyTo,
			final String subject, final String velocityTemplateName,
			final Map model) throws MailException {
		if (log.isDebugEnabled()) {
			log.debug("send email from @" + from + "@ to ->" + to
					+ ", subject->" + subject + ", template path->"
					+ velocityTemplateName + ", model->" + model);
		}

		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
						true, "UTF-8");
				message.setTo(to);
				message.setSubject(subject);
				message.setFrom(new InternetAddress(from, "RRS Team"));
				if (replyTo != null) {
					message.setReplyTo(replyTo);
				}
				message.setSentDate(new Date());

				final String textTemplatePath = TEMPLATE_PATH_PREFIX
						+ velocityTemplateName + TEXT_TEMPLATE_SUFFIX
						+ TEMPLATE_EXTENSION;
				final String htmlTemplatePath = TEMPLATE_PATH_PREFIX
						+ velocityTemplateName + HTML_TEMPLATE_SUFFIX
						+ TEMPLATE_EXTENSION;

				String text = VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine, textTemplatePath, "UTF-8", model);
				String html = VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine, htmlTemplatePath, "UTF-8", model);
				if (log.isDebugEnabled()) {
					log.debug(">>>>>>>message content @" + text);
					log.debug(">>>>>>>message content@html @" + html);
				}
				message.setText(text, html);
			}
		};
		this.mailSender.send(preparator);
	}

	@Async("myExecutor")
	public void sendEmail(final String to, final String subject,
			final String htmlContent, final String plainContent)
			throws MailException {

		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
						true, "UTF-8");
				message.setTo(to);
				message.setSubject(subject);
				message.setFrom(new InternetAddress(from, "RRS Team"));
				message.setSentDate(new Date());
				message.setText(plainContent, htmlContent);
			}
		};
		this.mailSender.send(preparator);
	}

	public String renderMailTemplate(String velocityTemplateName,
			Map<String, Object> model, boolean textMail) {

		String result = "";
		String templatePath = "";

		if (textMail) {
			templatePath = TEMPLATE_PATH_PREFIX + velocityTemplateName
					+ TEXT_TEMPLATE_SUFFIX + TEMPLATE_EXTENSION;

		} else {
			templatePath = TEMPLATE_PATH_PREFIX + velocityTemplateName
					+ HTML_TEMPLATE_SUFFIX + TEMPLATE_EXTENSION;
		}
		if (log.isDebugEnabled()) {
			log.debug(">>>>>>>message content @" + result);

		}
		result = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
				templatePath, "UTF-8", model);

		if (log.isDebugEnabled()) {
			log.debug(" text mail@" + textMail);
			log.debug(" message content @" + result);
		}

		return result;
	}

}
