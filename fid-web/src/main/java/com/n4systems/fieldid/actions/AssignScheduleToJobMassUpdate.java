package com.n4systems.fieldid.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.n4systems.fieldid.viewhelpers.EventScheduleSearchContainer;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.search.EventScheduleAction;
import com.n4systems.fieldid.actions.search.EventScheduleJobAssignment;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.viewhelpers.EventSearchContainer;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Project;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.security.Permissions;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.search.BaseSearchDefiner;

@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.Projects)
@UserPermissionFilter(userRequiresOneOf={Permissions.ManageJobs})
public class AssignScheduleToJobMassUpdate extends MassUpdate {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(AssignScheduleToJobMassUpdate.class);

	
	private EventScheduleSearchContainer scheduleCriteria;
	private EventSearchContainer reportCriteria;
	private Project job;
	private List<ListingPair> jobs;
	
	public AssignScheduleToJobMassUpdate(MassUpdateManager massUpdateManager, PersistenceManager persistenceManager) {
		super(massUpdateManager, persistenceManager);
		setSelect(new HashMap<String, Boolean>());
		getSelect().put("job",true);
	}
	
	@SkipValidation
	public String doEdit() {
		if (!findScheduleCriteria()) {
			addFlashErrorText("error.searchexpired");
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String doSave() {
		if (!findScheduleCriteria()) {
			addFlashErrorText("error.searchexpired");
			return ERROR;
		}
		
		try {
			List<Long> scheduleIds = scheduleCriteria.getMultiIdSelection().getSelectedIds();
			Long results = massUpdateManager.assignToJob(scheduleIds, job, getSessionUserId());
			List<String> messageArgs = new ArrayList<String>();
			messageArgs.add(results.toString());
			if (job != null) {
				addFlashMessage(getText("message.eventscheduleassignedtojobsuccessfully", messageArgs));
			} else {
				addFlashMessage(getText("message.eventscheduleremovedfromjobsuccessfully", messageArgs));
			}
			return SUCCESS;
		} catch (Exception e) {
			logger.error("failed to run a mass assignment of schedules to a job", e);
		}

		addActionError(getText("error.failedtomassassignupdate"));
		return INPUT;
		
	}

	@SkipValidation
	public String doEditEvents() {
		if (!findReportCriteria()) {
			addFlashErrorText("error.searchexpired");
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String doSaveEvents() {
		if (!findReportCriteria()) {
			addFlashErrorText("error.searchexpired");
			return ERROR;
		}
		
		try {
			List<Long> eventIds = reportCriteria.getMultiIdSelection().getSelectedIds();
			List<Long> scheduleIds = massUpdateManager.createSchedulesForEvents(eventIds, getSessionUserId());
			Long results = massUpdateManager.assignToJob(scheduleIds, job, getSessionUserId());
			List<String> messageArgs = new ArrayList<String>();
			messageArgs.add(results.toString());
			addFlashMessage(getText("message.eventscheduleassignedtojobsuccessfully", messageArgs));
			return SUCCESS;
		} catch (Exception e) {
			logger.error("failed to run a mass assignment of schedules to a job", e);
		}

		addActionError(getText("error.failedtomassassignupdate"));
		return INPUT;
		
	}
	
	private boolean findScheduleCriteria() {
		if (getScheduleCriteria(EventScheduleAction.SCHEDULE_CRITERIA)) {
			return true;
		} 
		
		return getScheduleCriteria(EventScheduleJobAssignment.SCHEDULE_CRITERIA);
	}

	private boolean getScheduleCriteria(String scheduleCriteriaKey) {
		if (getSession().containsKey(scheduleCriteriaKey) && getSession().get(scheduleCriteriaKey) != null) {
			scheduleCriteria = (EventScheduleSearchContainer)getSession().get(scheduleCriteriaKey);
		} 
		return !(scheduleCriteria == null || searchId == null || !searchId.equals(scheduleCriteria.getSearchId()));
	}
	
	private boolean findReportCriteria() {
		
		reportCriteria = getSession().getReportCriteria();

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
