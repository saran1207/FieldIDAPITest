package com.n4systems.model.savedreports;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.EntityManager;
import java.util.List;

public class SavedReportListLoader extends ListLoader<SavedReport> {

	public SavedReportListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<SavedReport> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<SavedReport> builder = new QueryBuilder<SavedReport>(SavedReport.class, filter);
		List<SavedReport> savedReports = builder.getResultList(em);
		return savedReports;
		
	}

}
