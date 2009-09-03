package com.n4systems.model.tenant.extendedfeatures;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.JobSite;
import com.n4systems.model.jobsites.JobSiteSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.persistence.QueryBuilder;

public class JobSiteSwitch extends ExtendedFeatureSwitch {

	protected JobSiteSwitch(PrimaryOrg primaryOrg) {
		super(primaryOrg, ExtendedFeature.JobSites);
	}

	@Override
	protected void featureSetup(Transaction transaction) {
		if (anyJobSitesDefined()) {
			createDefaultJobSite();
		}
	}

	private boolean anyJobSitesDefined() {
		QueryBuilder<JobSite> queryBuilder = new QueryBuilder<JobSite>(JobSite.class);
		queryBuilder.addSimpleWhere("tenant.id", primaryOrg.getTenant().getId());
	
		return PersistenceManager.entityExists(queryBuilder);
	}
		
	private void createDefaultJobSite() {
		JobSite defaultJobSite = buildDefaultJobSite();
		
		JobSiteSaver saver = new JobSiteSaver();
		saver.save(defaultJobSite);
	}

	private JobSite buildDefaultJobSite() {
		JobSite defaultJobSite = new JobSite();
		
		defaultJobSite.setName("General Store");
		defaultJobSite.setTenant(primaryOrg.getTenant());
		
		return defaultJobSite;
	}

	@Override
	protected void featureTearDown(Transaction transaction) {
		// TODO:  remove all job sites from system.
	}

}
