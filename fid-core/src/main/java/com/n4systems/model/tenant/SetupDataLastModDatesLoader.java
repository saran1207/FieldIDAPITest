package com.n4systems.model.tenant;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;

public class SetupDataLastModDatesLoader extends SecurityFilteredLoader<SetupDataLastModDates> {

	public SetupDataLastModDatesLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected SetupDataLastModDates load(EntityManager em, SecurityFilter filter) {
		return em.find(SetupDataLastModDates.class, filter.getTenantId());
	}

}
