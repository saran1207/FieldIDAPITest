package com.n4systems.model.user;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.security.UserType;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;

import java.util.List;

import javax.persistence.EntityManager;

public class UserListLoader extends ListLoader<User> {

    private boolean registered;
    private boolean includeSystem;
	
	public UserListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<User> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<User> builder = new QueryBuilder<User>(User.class, filter);

        if (registered) {
            UserQueryHelper.applyRegisteredFilter(builder);
        } else {
            UserQueryHelper.applyFullyActiveFilter(builder);
        }

        if (!includeSystem) {
            builder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.NE, "userType", UserType.SYSTEM));
        }

		return builder.getResultList(em);
	}

    public UserListLoader registered(boolean registered) {
        this.registered = registered;
        return this;
    }

    public UserListLoader includeSystemUser(boolean includeSystemUser) {
        this.includeSystem = includeSystemUser;
        return this;
    }

}
