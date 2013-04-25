package com.example.rrs.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.rrs.model.Connection;
import com.example.rrs.model.Connection.Status;
import com.example.rrs.model.QConnection;
import com.example.rrs.model.User;
import com.example.rrs.repository.ConnectionRepository;
import com.example.rrs.repository.UserRepository;

@Named
@Service
public class ConnectionService {

	private static final Logger log = LoggerFactory
			.getLogger(ConnectionService.class);

	@Inject
	ConnectionRepository connectionRepository;

	@Inject
	UserRepository userRepository;

	@Inject
	MailService mailSender;

	public String connectionStatus(String userId, String profileId) {
		if (log.isDebugEnabled()) {
			log.debug("fetch connection status@" + userId + "->" + profileId);
		}

		String result = Connection.CONNECTION_STATUS_NONE;
		QConnection qcon = QConnection.connection;
		List<Connection> cons = (List<Connection>) connectionRepository
				.findAll(qcon.userIds.get(0).eq(userId)
						.and(qcon.userIds.get(1).eq(profileId)));

		if (!cons.isEmpty()) {
			if (log.isDebugEnabled()) {
				log.debug("found connection@userId:" + userId + "->profileId:"
						+ profileId);
			}
			Status status = cons.get(0).getStatus();
			switch (status) {
			case PENDING:
				result = Connection.CONNECTION_STATUS_WAITING;
				break;
			case ACCEPTED:
				result = Connection.CONNECTION_STATUS_CONNECTED;
				break;
			default:
				break;
			}

		} else {
			cons = (List<Connection>) connectionRepository.findAll(qcon.userIds
					.get(0).eq(profileId).and(qcon.userIds.get(1).eq(userId)));
			if (!cons.isEmpty()) {
				if (log.isDebugEnabled()) {
					log.debug("found connection@ profile:" + profileId
							+ "->userId:" + userId);
				}
				Status status = cons.get(0).getStatus();
				switch (status) {
				case PENDING:
					result = Connection.CONNECTION_STATUS_BEING_WAITED;
					break;
				case ACCEPTED:
					result = Connection.CONNECTION_STATUS_CONNECTED;
					break;
				default:
					break;
				}
			}

		}

		return result;

	}

	public Connection findConnectionByUsers(String userId, String connectedTo) {
		if (log.isDebugEnabled()) {
			log.debug("fetch connection@" + userId + "->" + connectedTo);
		}

		QConnection qcon = QConnection.connection;
		List<Connection> cons = (List<Connection>) connectionRepository
				.findAll(qcon.userIds.get(0).eq(userId)
						.and(qcon.userIds.get(1).eq(connectedTo)));

		if (!cons.isEmpty()) {
			return cons.get(0);
		}

		return null;
	}

	public void acceptConnection(String userId, String connectedTo) {
		if (log.isDebugEnabled()) {
			log.debug("fetch connection@" + userId + "->" + connectedTo);
		}

		Connection con = findConnectionByUsers(userId, connectedTo);
		if (con != null) {
			con.setStatus(Connection.Status.ACCEPTED);
			con.setConnectedDate(new Date());
			connectionRepository.save(con);
		}
	}

	public void ignoreConnection(String userId, String connectedTo) {
		if (log.isDebugEnabled()) {
			log.debug("fetch connection@" + userId + "->" + connectedTo);
		}

		Connection con = findConnectionByUsers(userId, connectedTo);
		if (con != null) {
			con.setStatus(Connection.Status.IGNORED);
			connectionRepository.save(con);
		}
	}

	public void sendConnection(String userId, String connectedTo) {
		if (log.isDebugEnabled()) {
			log.debug("fetch connection@" + userId + "->" + connectedTo);
		}

		Connection con = findConnectionByUsers(userId, connectedTo);
		if (con == null) {
			con = new Connection(userId, connectedTo);
		}

		con.setStatus(Connection.Status.PENDING);
		connectionRepository.save(con);

		User user = userRepository.findOne(userId);
		User connected = userRepository.findOne(connectedTo);

		Map model = new HashMap();
		model.put("user", user);
		model.put("profile", connected);

		mailSender.sendEmail(connected.getEmail(), user.getFirstName()
				+ " requested to connect you!", "connection-request", model);
	}

	public List<Connection> findConnectionsForUser(String userId) {
		QConnection qcon = QConnection.connection;
		List<Connection> cons = (List<Connection>) connectionRepository
				.findAll((qcon.userIds.get(0).eq(userId).or(qcon.userIds.get(1)
						.eq(userId))).and(qcon.status.eq(Status.ACCEPTED)));
		return cons;
	}

	public long countConnectionsForUser(String userId) {
		QConnection qcon = QConnection.connection;
		long consCount = connectionRepository.count((qcon.userIds.get(0).eq(
				userId).or(qcon.userIds.get(1).eq(userId))).and(qcon.status
				.eq(Status.ACCEPTED)));
		return consCount;
	}

	public List<Connection> findPendingConnectionsForUser(String userId) {
		QConnection qcon = QConnection.connection;
		List<Connection> cons = (List<Connection>) connectionRepository
				.findAll((qcon.userIds.get(1).eq(userId)).and(qcon.status
						.eq(Status.PENDING)));
		return cons;
	}

	public long countPendingConnectionRequestsForUser(String userId) {
		QConnection qcon = QConnection.connection;
		long consCount = connectionRepository.count((qcon.userIds.get(1)
				.eq(userId)).and(qcon.status.eq(Status.PENDING)));
		return consCount;
	}

}
