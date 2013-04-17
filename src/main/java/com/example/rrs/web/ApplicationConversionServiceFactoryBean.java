package com.example.rrs.web;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

import com.example.rrs.model.EmailAddress;
import com.example.rrs.model.Link;
import com.example.rrs.model.Phone;
import com.example.rrs.model.User;
import com.example.rrs.service.UserService;

public class ApplicationConversionServiceFactoryBean extends
		FormattingConversionServiceFactoryBean {

	@Autowired
	UserService userService;

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		installLabelConverters(getObject());
	}

	public Converter<EmailAddress, String> getEmailToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<com.example.rrs.model.EmailAddress, java.lang.String>() {
			@Override
			public String convert(EmailAddress email) {
				return new StringBuilder().append(email.getType()).append(' ')
						.append(email.getContent()).toString();
			}
		};
	}


	public Converter<String, User> getIdToUserConverter() {
		return new org.springframework.core.convert.converter.Converter<String, com.example.rrs.model.User>() {
			@Override
			public com.example.rrs.model.User convert(String id) {
				return userService.findUser(id);
			}
		};
	}

	public Converter<Phone, String> getPhoneToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<com.example.rrs.model.Phone, java.lang.String>() {
			@Override
			public String convert(Phone phone) {
				return new StringBuilder().append(phone.getType()).append(' ')
						.append(phone.getContent()).toString();
			}
		};
	}

	public Converter<String, EmailAddress> getStringToEmailConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, com.example.rrs.model.EmailAddress>() {
			@Override
			public com.example.rrs.model.EmailAddress convert(String id) {
				return getObject().convert(
						getObject().convert(id, BigInteger.class), EmailAddress.class);
			}
		};
	}

	public Converter<String, Phone> getStringToPhoneConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, com.example.rrs.model.Phone>() {
			@Override
			public com.example.rrs.model.Phone convert(String id) {
				return getObject().convert(
						getObject().convert(id, BigInteger.class), Phone.class);
			}
		};
	}

	public Converter<String, Link> getStringToURLConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, com.example.rrs.model.Link>() {
			@Override
			public com.example.rrs.model.Link convert(String id) {
				return getObject().convert(
						getObject().convert(id, BigInteger.class), Link.class);
			}
		};
	}

	public Converter<String, User> getStringToUserConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, com.example.rrs.model.User>() {
			@Override
			public com.example.rrs.model.User convert(String id) {
				return getObject().convert(
						getObject().convert(id, BigInteger.class), User.class);
			}
		};
	}

	public Converter<Link, String> getURLToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<com.example.rrs.model.Link, java.lang.String>() {
			@Override
			public String convert(Link uRL) {
				return new StringBuilder().append(uRL.getType()).append(' ')
						.append(uRL.getContent()).toString();
			}
		};
	}

	public Converter<User, String> getUserToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<com.example.rrs.model.User, java.lang.String>() {
			@Override
			public String convert(User user) {
				return new StringBuilder().append(user.getEmail()).append(' ')
						.append(user.getFirstName()).append(' ')
						.append(user.getLastName()).append(' ')
						.append(user.getPassword()).toString();
			}
		};
	}

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
	}

	public void installLabelConverters(FormatterRegistry registry) {
		registry.addConverter(getEmailToStringConverter());
		registry.addConverter(getStringToEmailConverter());
		registry.addConverter(getPhoneToStringConverter());
		registry.addConverter(getStringToPhoneConverter());
		registry.addConverter(getURLToStringConverter());
		registry.addConverter(getStringToURLConverter());
		registry.addConverter(getUserToStringConverter());
		registry.addConverter(getIdToUserConverter());
	}
}
