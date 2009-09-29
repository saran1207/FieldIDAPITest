package com.n4systems.model.safetynetwork;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.PaginatedLoader;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class ConnectionListLoader extends PaginatedLoader<BaseOrg> {

	private final ConfigContext configContext;
	
	public ConnectionListLoader(SecurityFilter filter, ConfigContext configContext) {
		super(filter);
		this.configContext = configContext;
	}

	@Override
	protected QueryBuilder<BaseOrg> createBuilder(SecurityFilter filter) {
		QueryBuilder<BaseOrg> query = new QueryBuilder<BaseOrg>(BaseOrg.class, new OpenSecurityFilter());
		query.addWhere(Comparator.EQ, "tenant_name", "tenant.name", configContext.getString(ConfigEntry.HOUSE_ACCOUNT_NAME));
		
		return query;
	}

}
