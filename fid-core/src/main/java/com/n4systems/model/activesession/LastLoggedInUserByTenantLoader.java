package com.n4systems.model.activesession;

import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.services.config.ConfigService;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;

import javax.persistence.EntityManager;
import java.util.List;

public class LastLoggedInUserByTenantLoader extends Loader<User> {
	
	private Long tenantId;
	private boolean excludeN4User = false;
	
	@Override
	public User load(EntityManager em) {
		QueryBuilder<User> builder = new QueryBuilder<User>(User.class, new TenantOnlySecurityFilter(tenantId));

        if(excludeN4User)
            builder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.NE, "userID", ConfigService.getInstance().getString(ConfigEntry.SYSTEM_USER_USERNAME)));

		builder.addOrder("lastLogin", false);
        builder.setLimit(1);
		
		List<User> results = builder.getResultList(em);
		if (results == null || results.isEmpty()) {
			return null;
		} else {
			return results.get(0);
		}
	}
	
	public LastLoggedInUserByTenantLoader setTenant(Long tenantId) {
		this.tenantId = tenantId;
		return this;
	}
	
	public LastLoggedInUserByTenantLoader excludeN4User() {
		this.excludeN4User = true;
		return this;
	}

	

}
