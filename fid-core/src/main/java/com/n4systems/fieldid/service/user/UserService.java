package com.n4systems.fieldid.service.user;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserQueryHelper;
import com.n4systems.security.UserType;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class UserService extends FieldIdPersistenceService {

    public List<User> getUsers(boolean registered, boolean includeSystem) {
		QueryBuilder<User> builder = new QueryBuilder<User>(User.class, userSecurityFilter);

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

}
