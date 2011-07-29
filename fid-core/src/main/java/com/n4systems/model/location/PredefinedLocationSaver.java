package com.n4systems.model.location;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.savers.Saver;

public class PredefinedLocationSaver extends Saver<PredefinedLocation> {

	@Override
	public void save(EntityManager em, PredefinedLocation entity) {
		super.save(em, entity);
		// See the PredefinedLocation entity for why this is here
		entity.rebuildSearchIds(em);
	}

	@Override
	public void remove(EntityManager em, PredefinedLocation location) {
		SecurityFilter tenantFilter = createTenantOnlySecurityFilter(location);
		
		location.archiveEntity();
		update(em, location);
		
		for (PredefinedLocation child: getChildren(location, em, tenantFilter)) {
			remove(em, child);
		}
	}

	protected TenantOnlySecurityFilter createTenantOnlySecurityFilter(PredefinedLocation location) {
		return new TenantOnlySecurityFilter(location.getTenant().getId());
	}
	
	protected List<PredefinedLocation> getChildren(PredefinedLocation location, EntityManager entityManager, SecurityFilter tenantFilter){
		PredefinedLocationChildLoader childLoader = getChildLoader(location, tenantFilter);
		return childLoader.setParentId(location.getId()).load(entityManager, tenantFilter);
	}
	
	protected PredefinedLocationChildLoader getChildLoader(PredefinedLocation location, SecurityFilter tenantFilter) {
		PredefinedLocationChildLoader childLoader = new PredefinedLocationChildLoader(tenantFilter);
		return childLoader;
	}
	

}
