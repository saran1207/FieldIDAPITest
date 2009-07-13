package com.n4systems.model.tenant;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.persistence.loaders.legacy.SecuredLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class SetupDataLastModDatesLoader extends SecuredLoader<SetupDataLastModDates> {

	public SetupDataLastModDatesLoader(PersistenceManager pm, SecurityFilter filter) {
		super(pm, filter);
	}

	public SetupDataLastModDatesLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected SetupDataLastModDates load(PersistenceManager pm, SecurityFilter filter) {
		QueryBuilder<SetupDataLastModDates> builder = new QueryBuilder<SetupDataLastModDates>(
				SetupDataLastModDates.class, filter.prepareFor(SetupDataLastModDates.class));
		
		SetupDataLastModDates setupModDates = pm.find(builder);
		return setupModDates;
	}

}
