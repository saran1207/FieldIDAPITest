package com.n4systems.model.location;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class PredefinedLocationSaver extends Saver<PredefinedLocation> {

	@Override
	protected void save(EntityManager em, PredefinedLocation entity) {
		super.save(em, entity);
		// See the PredefinedLocation entity for why this is here
		entity.rebuildSearchIds(em);
	}

	@Override
	protected void remove(EntityManager em, PredefinedLocation location) {
		location.archiveEntity();
		update(em, location);
		
		QueryBuilder<PredefinedLocation> builder = new QueryBuilder<PredefinedLocation>(PredefinedLocation.class, createTenantOnlySecurityFilter(location));
		builder.addWhere(WhereClauseFactory.create("parent", location));
		
		SecurityFilter tenantFilter = createTenantOnlySecurityFilter(location);
		PredefinedLocationChildLoader childLoader = new PredefinedLocationChildLoader(tenantFilter);
		
		for (PredefinedLocation loc: childLoader.setParentId(location.getId()).load(em, tenantFilter)) {
			remove(em, loc);
		}
	}

	private TenantOnlySecurityFilter createTenantOnlySecurityFilter(PredefinedLocation location) {
		return new TenantOnlySecurityFilter(location.getTenant().getId());
	}
	

}
