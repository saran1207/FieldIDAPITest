package com.n4systems.model.orgs;

import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

@Deprecated
public class LegacyFindOrCreateDivisionOrgHandler extends FindOrCreateDivisionOrgHandler {
	private final PersistenceManager persistenceManager;
	
	public LegacyFindOrCreateDivisionOrgHandler(PersistenceManager persistenceManager) {
		super(null, null);
		this.persistenceManager = persistenceManager;
	}

	@Override
	protected List<DivisionOrg> loadDivisionList() {
		QueryBuilder<DivisionOrg> allDivisionsBuilder = new QueryBuilder<DivisionOrg>(DivisionOrg.class, new TenantOnlySecurityFilter(getCustomer().getTenant()));
		allDivisionsBuilder.addSimpleWhere("parent.id", getCustomer().getId());
		
		return persistenceManager.findAll(allDivisionsBuilder);
	}

	@Override
	protected void persistDivision(DivisionOrg division) {
		persistenceManager.save(division);
	}

}
