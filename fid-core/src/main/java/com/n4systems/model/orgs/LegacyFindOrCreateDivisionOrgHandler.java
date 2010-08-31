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
	protected List<DivisionOrg> loadOrgList() {
		QueryBuilder<DivisionOrg> allDivisionsBuilder = new QueryBuilder<DivisionOrg>(DivisionOrg.class, new TenantOnlySecurityFilter(getParent().getTenant()));
		allDivisionsBuilder.addSimpleWhere("parent.id", getParent().getId());
		
		return persistenceManager.findAll(allDivisionsBuilder);
	}

	@Override
	protected void saveOrg(DivisionOrg division) {
		persistenceManager.save(division);
	}

}
