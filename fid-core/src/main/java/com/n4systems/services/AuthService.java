package com.n4systems.services;


import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import org.springframework.transaction.annotation.Transactional;

public class AuthService extends FieldIdPersistenceService{

	@Transactional(readOnly = true)
	public User findUserByOAuthKey(String tokenKey, String consumerKey) {
		QueryBuilder<User> query = new QueryBuilder<>(User.class, new OpenSecurityFilter());
		query.addWhere(WhereClauseFactory.create("authToken.key", tokenKey));
		query.addWhere(WhereClauseFactory.create("tenant.settings.authConsumer.key", consumerKey));
		return persistenceService.find(query);
	}

}
