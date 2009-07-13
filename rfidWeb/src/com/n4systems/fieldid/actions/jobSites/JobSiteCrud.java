package com.n4systems.fieldid.actions.jobSites;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.web.helper.Constants;

import com.n4systems.ejb.CustomerManager;
import com.n4systems.ejb.JobSiteManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.exceptions.UpdateConatraintViolationException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.JobSite;
import com.n4systems.tools.Pager;
import com.n4systems.util.ListingPair;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.JobSites)
public class JobSiteCrud extends AbstractCrud {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(JobSiteCrud.class);

	private CustomerManager customerManager;
	private JobSiteManager jobSiteManager;

	private JobSite jobSite;

	private Pager<JobSite> page;

	private List<ListingPair> customers;
	private List<ListingPair> divisions;


	public JobSiteCrud(PersistenceManager persistenceManager, CustomerManager customerManager, JobSiteManager jobSiteManager) {
		super(persistenceManager);
		this.customerManager = customerManager;
		this.jobSiteManager = jobSiteManager;

	}

	@Override
	protected void initMemberFields() {
		jobSite = new JobSite();

	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		jobSite = persistenceManager.find(JobSite.class, uniqueId);
	}

	private void testDependencies() throws MissingEntityException {
		if (jobSite == null) {
			addActionError(getText("error.nojobsite"));
			throw new MissingEntityException();
		}
	}

	@SkipValidation
	public String doShow() {
		try {
			testDependencies();
		} catch (MissingEntityException e) {
			return MISSING;
		}
		return SUCCESS;
	}

	@SkipValidation
	public String doAdd() {
		try {
			testDependencies();
		} catch (MissingEntityException e) {
			return MISSING;
		}
		return SUCCESS;
	}

	@SkipValidation
	public String doEdit() {
		try {
			testDependencies();
		} catch (MissingEntityException e) {
			return MISSING;
		}
		return SUCCESS;
	}

	public String doSave() {
		try {
			testDependencies();
		} catch (MissingEntityException e) {
			return MISSING;
		}

		jobSite.setTenant(getTenant());

		try {
			if (jobSite.isNew()) {
				uniqueID = jobSiteManager.save(jobSite, getSessionUser().getUniqueID());
			} else {
				jobSite = jobSiteManager.update(jobSite, getSessionUser().getUniqueID());
			}
			addFlashMessage(getText("message.jobsitesaved"));
			return SUCCESS;
		} catch (UpdateConatraintViolationException e) {
			logger.error("can't update products.", e);
			addActionError(getText("error.massupdateproductconstriantviolation"));
		} catch (Exception e) {
			logger.error("failed to save job site.", e);
			addActionError(getText("error.jobsitesave"));
		}
		return ERROR;
	}

	@SkipValidation
	public String doList() {
		QueryBuilder<JobSite> queryBuilder = new QueryBuilder<JobSite>(JobSite.class);
		SecurityFilter filter = getSecurityFilter();
		filter.setTargets("tenant.id");
		queryBuilder.setSecurityFilter(filter);
		queryBuilder.addOrder("name");
		try {
			page = persistenceManager.findAllPaged(queryBuilder, getCurrentPage().intValue(), Constants.PAGE_SIZE);
		} catch (InvalidQueryException iqe) {
			logger.error("failed to load the list of job sites.", iqe);
			return ERROR;
		}
		return SUCCESS;
	}

	@SkipValidation
	public String doDelete() {
		try {
			testDependencies();
		} catch (MissingEntityException e) {
			return MISSING;
		}
		return SUCCESS;
	}

	public JobSite getJobSite() {
		return jobSite;
	}

	public Long getCustomer() {
		return (jobSite.getCustomer() != null) ? jobSite.getCustomer().getId() : null;
	}

	public Long getDivision() {
		return (jobSite.getDivision() != null) ? jobSite.getDivision().getId() : null;
	}

	public String getName() {
		return jobSite.getName();
	}

	public void setCustomer(Long customer) {
		if (customer == null) {
			jobSite.setCustomer(null);
		} else if (jobSite.getCustomer() == null || !customer.equals(jobSite.getCustomer().getId())) {
			jobSite.setCustomer(customerManager.findCustomer(customer, getSecurityFilter()));
		}

	}

	public void setDivision(Long division) {
		if (division == null) {
			jobSite.setDivision(null);
		} else if (jobSite.getDivision() == null || !division.equals(jobSite.getDivision().getId())) {
			jobSite.setDivision(customerManager.findDivision(division, getSecurityFilter()));
		}
	}

	@RequiredStringValidator(message = "", key = "error.namerequired")
	public void setName(String name) {
		jobSite.setName(name.trim());
	}

	public Pager<JobSite> getPage() {
		return page;
	}

	public List<ListingPair> getCustomers() {
		if (customers == null) {
			customers = customerManager.findCustomersLP(getSecurityFilter().getTenantId(), getSecurityFilter());
		}
		return customers;
	}

	public List<ListingPair> getDivisions() {
		if (divisions == null) {
			divisions = customerManager.findDivisionsLP(getCustomer(), getSecurityFilter());
		}
		return divisions;
	}

	

}
