package com.n4systems.model.tenant.extendedfeatures;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.JobSite;
import com.n4systems.model.TenantOrganization;
import com.n4systems.util.persistence.QueryBuilder;

public class JobSiteSwitch extends ExtendedFeatureSwitch {

	protected JobSiteSwitch(TenantOrganization tenant, PersistenceManager persistenceManager) {
		super(tenant, persistenceManager, ExtendedFeature.JobSites);
	}

	@Override
	protected void featureSetup() {
		if (anyJobSitesDefined()) {
			createDefaultJobSite();
		}
	}

	private boolean anyJobSitesDefined() {
		QueryBuilder<JobSite> queryBuilder = new QueryBuilder<JobSite>(JobSite.class);
		queryBuilder.addSimpleWhere("tenant.id", tenant.getId());
	
		return (0L < persistenceManager.findCount(queryBuilder));
	}
		
	private void createDefaultJobSite() {
		JobSite defaultJobSite = buildDefaultJobSite();
		persistenceManager.save(defaultJobSite);
	}

	private JobSite buildDefaultJobSite() {
		JobSite defaultJobSite = new JobSite();
		
		defaultJobSite.setName("General Store");
		defaultJobSite.setTenant(tenant);
		
		return defaultJobSite;
	}

	@Override
	protected void featureTearDown() {
		// TODO:  remove all job sites from system.
		
	}

}
