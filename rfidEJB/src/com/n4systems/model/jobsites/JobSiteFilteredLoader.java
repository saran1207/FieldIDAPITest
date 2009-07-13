package com.n4systems.model.jobsites;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.JobSite;
import com.n4systems.persistence.loaders.legacy.SecuredLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

/**
 * @deprecated Use {@link FilteredIdLoader} instead.
 */
@Deprecated
public class JobSiteFilteredLoader extends SecuredLoader<JobSite> {
	private Long id;
	
	public JobSiteFilteredLoader(PersistenceManager pm, SecurityFilter filter) {
		super(pm, filter);
	}

	public JobSiteFilteredLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected JobSite load(PersistenceManager pm, SecurityFilter filter) {
		QueryBuilder<JobSite> builder = new QueryBuilder<JobSite>(JobSite.class, filter.prepareFor(JobSite.class));
		
		builder.addSimpleWhere("id", id);
		
		JobSite jobSite = pm.find(builder);
		
	    return jobSite;
	}
	
	public JobSiteFilteredLoader setId(Long id) {
		this.id = id;
		return this;
	}

}
