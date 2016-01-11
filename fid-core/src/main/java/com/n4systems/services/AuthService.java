package com.n4systems.services;


import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.math.BigInteger;

public class AuthService extends FieldIdPersistenceService {

	@Resource
	private PlatformTransactionManager transactionManager;

	@Transactional(readOnly = true)
	public User findUserByOAuthKey(String tokenKey, String consumerKey) {
		QueryBuilder<User> query = new QueryBuilder<>(User.class, new OpenSecurityFilter());
		query.addWhere(WhereClauseFactory.create("authToken.key", tokenKey));
		query.addWhere(WhereClauseFactory.create("tenant.settings.authConsumer.key", consumerKey));
		return persistenceService.find(query);
	}

	@Transactional(readOnly = true)
	public boolean exceededRequestLimit(String consumerKey, String tokenKey, long limit) {
		String stmt = "SELECT COUNT(*) FROM `request_log` WHERE `consumer_key` = ? AND `token_key` = ?";
		Query query = getEntityManager().createNativeQuery(stmt);
		query.setParameter(1, consumerKey);
		query.setParameter(2, tokenKey);
		Long requests = ((BigInteger) query.getSingleResult()).longValue();
		return requests > limit;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean validateRequest(String consumerKey, String tokenKey, String nonce, Long timestamp) {
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

		try {
			String stmt = "INSERT INTO `request_log` (`consumer_key`, `token_key`, `nonce`, `timestamp`) VALUES (?, ?, ?, ?)";
			Query query = getEntityManager().createNativeQuery(stmt);
			query.setParameter(1, consumerKey);
			query.setParameter(2, tokenKey);
			query.setParameter(3, nonce);
			query.setParameter(4, timestamp);
			query.executeUpdate();
			transactionManager.commit(status);
			return true;
		} catch (PersistenceException e) {
			try { transactionManager.rollback(status); } catch (TransactionException te) { throw te; }
			if (e.getCause() instanceof ConstraintViolationException) {
				return false;
			} else {
				throw e;
			}
		}
	}

	@Transactional
	public void clearRequestLog() {
		String stmt = "TRUNCATE `request_log`";
		Query query = getEntityManager().createNativeQuery(stmt);
		query.executeUpdate();
	}

}
