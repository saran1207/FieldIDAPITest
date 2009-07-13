package com.n4systems.ejb;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.n4systems.exceptions.UpdateConatraintViolationException;
import com.n4systems.exceptions.UpdateFailureException;
import com.n4systems.fieldid.tools.reports.ReportStructure;
import com.n4systems.fieldid.tools.reports.SearchStructure;
import com.n4systems.model.Inspection;
import com.n4systems.model.JobSite;
import com.n4systems.model.Product;
import com.n4systems.util.ReportCriteria;
import com.n4systems.util.SearchCriteria;
import com.n4systems.util.SecurityFilter;

@Stateless
public class JobSiteManagerImpl implements JobSiteManager {

	@EJB
	private PersistenceManager persistenceManager;

	@EJB
	private MassUpdateManager massUpdateManager;

	public Long save(JobSite jobSite, Long userId) {
		if (userId != null) {
			return persistenceManager.save(jobSite, userId);
		}

		return persistenceManager.save(jobSite);
	}

	public JobSite update(JobSite jobSite, Long userId) throws UpdateFailureException, UpdateConatraintViolationException {
		JobSite oldJobSite = persistenceManager.find(JobSite.class, jobSite.getId());
		if ((oldJobSite.getCustomer() == null && jobSite.getCustomer() != null) || (oldJobSite.getCustomer() != null && oldJobSite.getCustomer().equals(jobSite.getCustomer()))
				|| (oldJobSite.getDivision() == null && jobSite.getDivision() != null) || (oldJobSite.getDivision() != null && oldJobSite.getDivision().equals(jobSite.getDivision()))) {
			
			Map<String, Boolean> values = new HashMap<String, Boolean>();
			values.put("jobSite", true);
			
			updateInspections(jobSite, userId, values);
			updateProducts(jobSite, userId, values);
		}

		return persistenceManager.update(jobSite, userId);

	}
		
	// XXX change this to a id query.
	private Inspection updateInspections(JobSite jobSite, Long userId, Map<String, Boolean> values) throws UpdateFailureException {
		ReportCriteria reportCriteria = new ReportCriteria(new SecurityFilter(jobSite.getTenant().getId()), ReportStructure.getReportType("inspectionSummary", true));
		reportCriteria.setJobSite(jobSite.getId());
		Inspection inspection = new Inspection();
		inspection.setJobSite(jobSite);
		massUpdateManager.updateInspections(reportCriteria, inspection, values, userId);
		return inspection;
	}

	// XXX change this to a id query.
	private void updateProducts(JobSite jobSite, Long userId, Map<String, Boolean> values) throws UpdateFailureException, UpdateConatraintViolationException {
		SearchCriteria searchCriteria = new SearchCriteria(new SecurityFilter(jobSite.getTenant().getId()), SearchStructure.getSearchStructure(false, true));
		searchCriteria.setJobSite(jobSite.getId());
		Product product = new Product();
		product.setJobSite(jobSite);
		massUpdateManager.updateProducts(searchCriteria, product, values, userId);
	}

	

}
