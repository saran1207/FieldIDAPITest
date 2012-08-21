package com.n4systems.model.location;

import com.n4systems.model.api.Archivable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.UserSecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.TimeZone;

public class PredefinedLocationListLoader extends ListLoader<PredefinedLocation> {

	private boolean parentFirstOrdering;
	private boolean archivedState;
	private UserSecurityFilter filter;
	
	public PredefinedLocationListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<PredefinedLocation> load(EntityManager em, SecurityFilter filter) {
        final BaseOrg owner = filter.getOwner();
		this.filter = new UserSecurityFilter(owner, filter.getUserId(), TimeZone.getDefault() );
				
		QueryBuilder<PredefinedLocation> builder = createQueryBuilder(filter);
		
		if (parentFirstOrdering) {
			builder.addOrder("id");
		}
		
		return builder.getResultList(em);
	}
	
	protected QueryBuilder<PredefinedLocation> createQueryBuilder(SecurityFilter filter) {
        QueryBuilder<PredefinedLocation> query = new QueryBuilder<PredefinedLocation>(PredefinedLocation.class, filter);
        query.addSimpleWhere("state", Archivable.EntityState.ACTIVE);
        return query;
	}
	

	public PredefinedLocationListLoader withParentFirstOrder() {
		parentFirstOrdering = true;
		return this;
	}

	public PredefinedLocationListLoader withArchivedState() {
		archivedState = true;
		return this;
	}
	

}
