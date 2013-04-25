package com.example.rrs.web;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import com.example.rrs.model.User;
import com.example.rrs.security.SecurityUtils;
import com.example.rrs.service.ConnectionService;
import com.example.rrs.service.UserService;

public class UserHandlerInterceptor implements WebRequestInterceptor {

	private static final Logger log = LoggerFactory
			.getLogger(UserHandlerInterceptor.class);

	@Inject
	UserService userService;

	@Inject
	ConnectionService connectionService;

	public UserHandlerInterceptor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void preHandle(WebRequest request) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(WebRequest request, ModelMap model) throws Exception {
		User user = SecurityUtils.getCurrentUser();
		if (log.isDebugEnabled()) {
			log.debug("call @initDataBinder, put user in request scope");
		}
		request.setAttribute("user", user, RequestAttributes.SCOPE_REQUEST);

		long connectinsCount = connectionService.countConnectionsForUser(user
				.getId());
		long pendingConnectinsCount = connectionService
				.countPendingConnectionRequestsForUser(user.getId());

		long viewedCount = userService.viewedCountInSevenDaysForUser(user
				.getId());

		if (log.isDebugEnabled()) {
			log.debug("connectinsCount@" + connectinsCount);
			log.debug("pendingConnectinsCount@" + pendingConnectinsCount);
			log.debug("viewedCount@" + viewedCount);
		}

		request.setAttribute("connectionsCount", connectinsCount,
				RequestAttributes.SCOPE_REQUEST);

		request.setAttribute("pendingConnectionsCount", pendingConnectinsCount,
				RequestAttributes.SCOPE_REQUEST);

		request.setAttribute("viewedCount", viewedCount,
				RequestAttributes.SCOPE_REQUEST);

	}

	@Override
	public void afterCompletion(WebRequest request, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
