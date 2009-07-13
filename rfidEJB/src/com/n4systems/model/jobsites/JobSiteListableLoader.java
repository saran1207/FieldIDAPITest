package com.n4systems.model.jobsites;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.JobSite;
import com.n4systems.model.api.Listable;
import com.n4systems.persistence.loaders.legacy.ListableLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.ListableSelect;
import com.n4systems.util.persistence.QueryBuilder;

//TODO: Update this class to extend com.n4systems.persistence.loaders.ListableLoader
public class JobSiteListableLoader extends ListableLoader {

	public JobSiteListableLoader(SecurityFilter filter) {
		super(filter);
	}

	public JobSiteListableLoader(PersistenceManager pm, SecurityFilter filter) {
		super(pm, filter);
	}

	@Override
	protected QueryBuilder<Listable<Long>> createBuilder(SecurityFilter filter) {
		QueryBuilder<Listable<Long>> builder = new QueryBuilder<Listable<Long>>(JobSite.class, filter.prepareFor(JobSite.class));
		
		builder.setSelectArgument(new ListableSelect());

		return builder;
	}

}
