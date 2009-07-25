package com.n4systems.model.tenant;

import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.persistence.loaders.legacy.ListLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class AllAlertStatusLoader extends ListLoader<AlertStatus> {

	public AllAlertStatusLoader(PersistenceManager pm) {
		super(pm, null);
	}

	public AllAlertStatusLoader() {
		super(null);
	}

	@Override
	protected List<AlertStatus> load(PersistenceManager pm, SecurityFilter filter) {
		List<AlertStatus> stats = pm.findAll(new QueryBuilder<AlertStatus>(AlertStatus.class));
		
		return stats;
	}

}
