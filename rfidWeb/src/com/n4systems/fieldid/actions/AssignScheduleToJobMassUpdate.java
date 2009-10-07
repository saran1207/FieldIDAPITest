package com.n4systems.fieldid.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.CustomerManager;
import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.search.InspectionReportAction;
import com.n4systems.fieldid.actions.search.InspectionScheduleAction;
import com.n4systems.fieldid.actions.search.InspectionScheduleJobAssignment;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.fieldid.viewhelpers.InspectionScheduleSearchContainer;
import com.n4systems.fieldid.viewhelpers.InspectionSearchContainer;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Project;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;

@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.Projects)
public class AssignScheduleToJobMassUpdate extends MassUpdate {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(AssignScheduleToJobMassUpdate.class);

	
	private InspectionScheduleSearchContainer scheduleCriteria;
	private InspectionSearchContainer reportCriteria;
	private Project job;
	private List<ListingPair> jobs;
	
	public AssignScheduleToJobMassUpdate(CustomerManager customerManager, MassUpdateManager massUpdateManager, PersistenceManager persistenceManager) {
		super(customerManager, massUpdateManager, persistenceManager);
		setSelect(new HashMap<String, Boolean>());
		getSelect().put("job",true);
	}
	
	@SkipValidation
	public String doEdit() {
		if (!findScheduleCriteria()) {
			addFlashError(getText("error.searchexpired"));
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String doSave() {
		if (!findScheduleCriteria()) {
			addFlashError(getText("error.searchexpired"));
			return ERROR;
		}
		
		try {
			List<Long> scheduleIds = persistenceManager.idSearch(scheduleCriteria, scheduleCriteria.getSecurityFilter());
			Long results = massUpdateManager.assignToJob(scheduleIds, job, getSessionUserId());
			List<String> messageArgs = new ArrayList<String>();
			messageArgs.add(results.toString());
			if (job != null) {
				addFlashMessage(getText("message.inspectionscheduleassignedtojobsuccessfully", messageArgs));
			} else {
				addFlashMessage(getText("message.inspectionscheduleremovedfromjobsuccessfully", messageArgs));
			}
			return SUCCESS;
		} catch (Exception e) {
			logger.error("failed to run a mass assignment of schedules to a job", e);
		}

		addActionError(getText("error.failedtomassassignupdate"));
		return INPUT;
		
	}
	
	
	@SkipValidation
	public String doEditInspections() {
		if (!findReportCriteria()) {
			addFlashError(getText("error.searchexpired"));
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String doSaveInspections() {
		if (!findReportCriteria()) {
			addFlashError(getText("error.searchexpired"));
			return ERROR;
		}
		
		try {
			List<Long> inspectionIds = persistenceManager.idSearch(reportCriteria, reportCriteria.getSecurityFilter());
			List<Long> scheduleIds = massUpdateManager.createSchedulesForInspections(inspectionIds, getSessionUserId());
			Long results = massUpdateManager.assignToJob(scheduleIds, job, getSessionUserId());
			List<String> messageArgs = new ArrayList<String>();
			messageArgs.add(results.toString());
			addFlashMessage(getText("message.inspectionscheduleassignedtojobsuccessfully", messageArgs));
			return SUCCESS;
		} catch (Exception e) {
			logger.error("failed to run a mass assignment of schedules to a job", e);
		}

		addActionError(getText("error.failedtomassassignupdate"));
		return INPUT;
		
	}
	
	private boolean findScheduleCriteria() {
		if (getScheduleCriteria(InspectionScheduleAction.SCHEDULE_CRITERIA)) {
			return true;
		} 
		
		return getScheduleCriteria(InspectionScheduleJobAssignment.SCHEDULE_CRITERIA);
	}

	private boolean getScheduleCriteria(String scheduleCriteriaKey) {
		if (getSession().containsKey(scheduleCriteriaKey) && getSession().get(scheduleCriteriaKey) != null) {
			scheduleCriteria = (InspectionScheduleSearchContainer)getSession().get(scheduleCriteriaKey);
		} 
		return !(scheduleCriteria == null || searchId == null || !searchId.equals(scheduleCriteria.getSearchId()));
	}
	
	private boolean findReportCriteria() {
		if (getSession().containsKey(InspectionReportAction.REPORT_CRITERIA) && getSession().get(InspectionReportAction.REPORT_CRITERIA) != null) {
			reportCriteria = (InspectionSearchContainer)getSession().get(InspectionReportAction.REPORT_CRITERIA);
		}

		return !(reportCriteria == null || searchId == null || !searchId.equals(reportCriteria.getSearchId()));
	}

	public Long getJob() {
		return (job != null) ? job.getId() : null;
	}

	public void setJob(Long jobId) {
		if (jobId == null) {
			job = null;
		} else if (job == null || !jobId.equals(job.getId())) {
			job = persistenceManager.find(Project.class, jobId, getSecurityFilter());
		}
	}
	
	public List<ListingPair> getJobs() {
		if (jobs == null) {
			QueryBuilder<ListingPair> query = new QueryBuilder<ListingPair>(Project.class, getSecurityFilter());
			query.addSimpleWhere("eventJob", true).addSimpleWhere("retired", false);
			jobs = persistenceManager.findAllLP(query, "name");
		}
		return jobs;
	}
}
