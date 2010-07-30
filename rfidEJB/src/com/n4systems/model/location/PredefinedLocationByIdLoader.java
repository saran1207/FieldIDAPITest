package com.n4systems.model.location;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class PredefinedLocationByIdLoader extends SecurityFilteredLoader<PredefinedLocation> {

	private Long id;
	private boolean showArchived = false;
	
	public PredefinedLocationByIdLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected PredefinedLocation load(EntityManager em, SecurityFilter filter) {
		
		TenantOnlySecurityFilter securityFilter = new TenantOnlySecurityFilter(filter);
		if (showArchived) {
			securityFilter.toggleShowArchived();
		}
		
		QueryBuilder<PredefinedLocation> query = new QueryBuilder<PredefinedLocation>(PredefinedLocation.class, securityFilter);
		query.addSimpleWhere("id", id);

		return query.getSingleResult(em);
	}
	
	public PredefinedLocationByIdLoader setId(Long id) {
		this.id = id;
		return this;
	}
	
	public PredefinedLocationByIdLoader setShowArchived(boolean showArchived) {
		this.showArchived = showArchived;
		return this;
	}
	

}
