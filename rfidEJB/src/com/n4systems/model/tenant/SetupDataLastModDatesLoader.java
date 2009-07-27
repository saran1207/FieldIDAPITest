package com.n4systems.model.tenant;

import javax.persistence.EntityManager;

import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class SetupDataLastModDatesLoader extends SecurityFilteredLoader<SetupDataLastModDates> {

	public SetupDataLastModDatesLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected SetupDataLastModDates load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<SetupDataLastModDates> builder = new QueryBuilder<SetupDataLastModDates>(
				SetupDataLastModDates.class, filter.prepareFor(SetupDataLastModDates.class));
		
		SetupDataLastModDates setupModDates = builder.getSingleResult(em);
		return setupModDates;
	}

}
