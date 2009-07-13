package com.n4systems.fieldid.actions.search;

import org.apache.log4j.Logger;

import rfid.ejb.session.LegacyProductSerial;
import rfid.ejb.session.LegacyProductType;
import rfid.ejb.session.User;

import com.n4systems.ejb.CustomerManager;
import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.Project;
import com.n4systems.util.persistence.QueryBuilder;

public class InspectionScheduleJobAssignment extends InspectionScheduleAction {
	public static final String SCHEDULE_CRITERIA = "scheduleCriteriaJob";
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(InspectionScheduleJobAssignment.class);
	
	private Project job;
	
	public InspectionScheduleJobAssignment(CustomerManager customerManager, LegacyProductType productTypeManager, LegacyProductSerial productSerialManager, PersistenceManager persistenceManager,
			InspectionManager inspectionManager, User userManager, ProductManager productManager, InspectionScheduleManager inspectionScheduleManager) {
		super(SCHEDULE_CRITERIA, InspectionScheduleJobAssignment.class, customerManager, productTypeManager, productSerialManager, persistenceManager, inspectionManager, userManager, productManager, inspectionScheduleManager);
	}

	private void testRequiredEntities() {
		if (job == null && getContainer().getJobAndNullId() == null) {
			addFlashErrorText("error.nojob");
			logger.warn("no job was found for assign schedules to.");
			throw new MissingEntityException();
		} 
	}
	
	public String doStartSearch() {
		testRequiredEntities();
		getContainer().setJobAndNullId(job.getId());
		getContainer().setJobSite((job.getJobSite() != null) ? job.getJobSite().getId() : null);
		getContainer().setCustomer((job.getCustomer() != null) ? job.getCustomer().getId() : null);
		getContainer().setDivision((job.getDivision() != null) ? job.getDivision().getId() : null);
		return doCreateSearch();
	}
	
	public String doCreateSearch() {
		testRequiredEntities();
		setJobId(getContainer().getJobAndNullId());
		return super.doCreateSearch();
	}
	
	
	public String doSearch() {
		testRequiredEntities();
		setJobId(getContainer().getJobAndNullId());
		return super.doSearch();
	}
	
	public Long getJobId() {
		return (job != null) ? job.getId() : null;
	}
	
	public Long getProjectId() {
		return getJobId();
	}
	
	public void setJobId(Long jobId) {
		if (jobId == null) {
			job = null;
		} else if (job == null || !jobId.equals(job.getId())) {
			QueryBuilder<Project> eventJob = new QueryBuilder<Project>(Project.class, getSecurityFilter().setDefaultTargets());
			eventJob.addSimpleWhere("eventJob",true).addSimpleWhere("id", jobId);
			job = persistenceManager.find(eventJob);
		}
	}
	
	public Project getJob() {
		return job;
	}
	
	public Project getProject() {
		return job;
	}
	
	public Long getJobForSchedule(String scheduleId) {
		QueryBuilder<Long> jobId = new QueryBuilder<Long>(InspectionSchedule.class, getSecurityFilter().setDefaultTargets());
		jobId.setSimpleSelect("project.id").addLeftJoin("project", "project").addSimpleWhere("id", Long.valueOf(scheduleId));
		return persistenceManager.find(jobId);
	}
	


}
