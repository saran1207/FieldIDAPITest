package com.n4systems.fieldid.service.user;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserQueryHelper;
import com.n4systems.security.UserType;
import com.n4systems.util.StringUtils;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;

@Transactional
public class UserService extends FieldIdPersistenceService {

    public List<User> getUsers(boolean registered, boolean includeSystem) {
		QueryBuilder<User> builder = createUserSecurityBuilder(User.class);

        if (registered) {
            UserQueryHelper.applyRegisteredFilter(builder);
        } else {
            UserQueryHelper.applyFullyActiveFilter(builder);
        }

        if (!includeSystem) {
            builder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.NE, "userType", UserType.SYSTEM));
        }

        return persistenceService.findAll(builder);
    }

	public User getUser(Long userId) {
		QueryBuilder<User> builder = createUserSecurityBuilder(User.class);
        builder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "id", userId));
        return persistenceService.find(builder);		
	}
	
	//TODO: This should test for locked users/failed login attempts
	public User authenticateUserByPassword(String tenantName, String userId, String password) {
		QueryBuilder<User> builder = new QueryBuilder<User>(User.class, new OpenSecurityFilter());
		UserQueryHelper.applyFullyActiveFilter(builder);
		
		builder.addWhere(WhereClauseFactory.createCaseInsensitive("tenant.name", tenantName));
		builder.addWhere(WhereClauseFactory.createCaseInsensitive("userID", userId));
		builder.addWhere(WhereClauseFactory.create("hashPassword", User.hashPassword(password)));
		
		User user = persistenceService.find(builder);
		return user;
	}

	public User authenticateUserBySecurityCard(String tenantName, String cardNumber) {
		if (StringUtils.isEmpty(cardNumber)) {
			return null;
		}

		QueryBuilder<User> builder = new QueryBuilder<User>(User.class, new OpenSecurityFilter());
		UserQueryHelper.applyFullyActiveFilter(builder);
		
		builder.addWhere(WhereClauseFactory.createCaseInsensitive("tenant.name", tenantName));
		builder.addWhere(WhereClauseFactory.create("hashSecurityCardNumber", User.hashSecurityCardNumber(cardNumber)));

		List<User> users = persistenceService.findAll(builder);
		return (users.size() != 1) ? null : users.get(0);
	}
	
}
