package com.n4systems.model.orgs;

import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class EntityByIdIncludingArchivedLoader<T extends AbstractEntity> extends FilteredIdLoader<T> {

	public EntityByIdIncludingArchivedLoader(SecurityFilter filter, Class <T> clazz) {
		super(filter, clazz);
	}

	@Override
	protected QueryBuilder<T> createQueryBuilder(SecurityFilter filter) {
		return new QueryBuilder<T>(clazz, new TenantOnlySecurityFilter(filter).setShowArchived(true));
	}

}
