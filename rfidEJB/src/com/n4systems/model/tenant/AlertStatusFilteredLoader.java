package com.n4systems.model.tenant;

import javax.persistence.EntityManager;

import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class AlertStatusFilteredLoader extends SecurityFilteredLoader<AlertStatus> {

	public AlertStatusFilteredLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected AlertStatus load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<AlertStatus> builder = new QueryBuilder<AlertStatus>(AlertStatus.class, filter.prepareFor(AlertStatus.class));
		
		AlertStatus status = builder.getSingleResult(em);
		return status;
	}

}
