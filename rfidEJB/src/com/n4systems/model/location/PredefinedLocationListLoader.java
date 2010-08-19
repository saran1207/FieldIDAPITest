package com.n4systems.model.location;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class PredefinedLocationListLoader extends ListLoader<PredefinedLocation> {

	private boolean parentFirstOrdering;
	private boolean archivedState;
	private TenantOnlySecurityFilter archivedFilter;
	
	public PredefinedLocationListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<PredefinedLocation> load(EntityManager em, SecurityFilter filter) {
		archivedFilter = new TenantOnlySecurityFilter(filter);
				
		if(archivedState){
			archivedFilter.enableShowArchived();
		}
		
		QueryBuilder<PredefinedLocation> builder = createQueryBuilder(archivedFilter);
		
		if (parentFirstOrdering) {
			builder.addOrder("id");
		}
		
		return builder.getResultList(em);
	}
	
	protected QueryBuilder<PredefinedLocation> createQueryBuilder(TenantOnlySecurityFilter filter) {
		return new QueryBuilder<PredefinedLocation>(PredefinedLocation.class, filter);
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
