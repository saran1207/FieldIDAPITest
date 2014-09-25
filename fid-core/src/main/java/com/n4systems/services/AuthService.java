package com.n4systems.services;


import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;

public class AuthService extends FieldIdPersistenceService{

	@Transactional(readOnly = true)
	public User findUserByOAuthKey(String tokenKey, String consumerKey) {
		QueryBuilder<User> query = new QueryBuilder<>(User.class, new OpenSecurityFilter());
		query.addWhere(WhereClauseFactory.create("authToken.key", tokenKey));
		query.addWhere(WhereClauseFactory.create("tenant.settings.authConsumer.key", consumerKey));
		return persistenceService.find(query);
	}

	@Transactional(readOnly = true)
	public boolean exceddedRequestLimit(String consumerKey, String tokenKey, long limit) {
		String stmt = "SELECT COUNT(*) FROM `request_log` WHERE `consumer_key` = ? AND `token_key` = ?";
		Query query = getEntityManager().createNativeQuery(stmt);
		query.setParameter(1, consumerKey);
		query.setParameter(2, tokenKey);
		Long requests = (Long) query.getSingleResult();
		return requests > limit;
	}

	@Transactional
	public boolean validateRequest(String consumerKey, String tokenKey, String nonce, Long timestamp) {
		try {
			String stmt = "INSERT INTO `request_log` (`consumer_key`, `token_key`, `nonce`, `timestamp`) VALUES (?, ?, ?, ?)";
			Query query = getEntityManager().createNativeQuery(stmt);
			query.setParameter(1, consumerKey);
			query.setParameter(2, tokenKey);
			query.setParameter(3, nonce);
			query.setParameter(4, timestamp);
			query.executeUpdate();
			return true;
		} catch (ConstraintViolationException cve) {
			return false;
		} catch (Exception e) {
			throw e;
		}
	}

}
