package com.n4systems.model.tenant;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.persistence.loaders.legacy.SecuredLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class AlertStatusFilteredLoader extends SecuredLoader<AlertStatus> {

	public AlertStatusFilteredLoader(PersistenceManager pm, SecurityFilter filter) {
		super(pm, filter);
	}

	public AlertStatusFilteredLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected AlertStatus load(PersistenceManager pm, SecurityFilter filter) {
		QueryBuilder<AlertStatus> builder = new QueryBuilder<AlertStatus>(AlertStatus.class, filter.prepareFor(AlertStatus.class));
		
		AlertStatus status = pm.find(builder);
		return status;
	}

}
