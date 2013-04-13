package com.example.rrs.web;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

import com.example.rrs.model.Email;
import com.example.rrs.model.Phone;
import com.example.rrs.model.URL;
import com.example.rrs.model.User;
import com.example.rrs.repository.PhoneRepository;
import com.example.rrs.repository.URLRepository;
import com.example.rrs.service.UserService;

public class ApplicationConversionServiceFactoryBean extends
		FormattingConversionServiceFactoryBean {

	@Autowired
	PhoneRepository phoneRepository;

	@Autowired
	URLRepository uRLRepository;

	@Autowired
	UserService userService;

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		installLabelConverters(getObject());
	}

	public Converter<Email, String> getEmailToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<com.example.rrs.model.Email, java.lang.String>() {
			@Override
			public String convert(Email email) {
				return new StringBuilder().append(email.getType()).append(' ')
						.append(email.getContent()).toString();
			}
		};
	}

	public Converter<BigInteger, Phone> getIdToPhoneConverter() {
		return new org.springframework.core.convert.converter.Converter<java.math.BigInteger, com.example.rrs.model.Phone>() {
			@Override
			public com.example.rrs.model.Phone convert(java.math.BigInteger id) {
				return phoneRepository.findOne(id);
			}
		};
	}

	public Converter<BigInteger, URL> getIdToURLConverter() {
		return new org.springframework.core.convert.converter.Converter<java.math.BigInteger, com.example.rrs.model.URL>() {
			@Override
			public com.example.rrs.model.URL convert(java.math.BigInteger id) {
				return uRLRepository.findOne(id);
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

	public Converter<String, Email> getStringToEmailConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, com.example.rrs.model.Email>() {
			@Override
			public com.example.rrs.model.Email convert(String id) {
				return getObject().convert(
						getObject().convert(id, BigInteger.class), Email.class);
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

	public Converter<String, URL> getStringToURLConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, com.example.rrs.model.URL>() {
			@Override
			public com.example.rrs.model.URL convert(String id) {
				return getObject().convert(
						getObject().convert(id, BigInteger.class), URL.class);
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

	public Converter<URL, String> getURLToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<com.example.rrs.model.URL, java.lang.String>() {
			@Override
			public String convert(URL uRL) {
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
		registry.addConverter(getIdToPhoneConverter());
		registry.addConverter(getStringToPhoneConverter());
		registry.addConverter(getURLToStringConverter());
		registry.addConverter(getIdToURLConverter());
		registry.addConverter(getStringToURLConverter());
		registry.addConverter(getUserToStringConverter());
		registry.addConverter(getIdToUserConverter());
	}
}
