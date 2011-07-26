package com.n4systems.fieldid.actions.search;

import com.n4systems.ejb.EventManager;
import com.n4systems.fieldid.service.search.columns.EventToJobColumnsService;
import com.n4systems.model.search.ReportConfiguration;
import org.apache.log4j.Logger;

import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.AssetManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.Project;
import com.n4systems.security.Permissions;
import com.n4systems.util.persistence.QueryBuilder;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageJobs})
public class EventScheduleJobAssignment extends EventScheduleAction {
	public static final String SCHEDULE_CRITERIA = "scheduleCriteriaJob";
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(EventScheduleJobAssignment.class);
	
	private Project job;
	
	public EventScheduleJobAssignment(PersistenceManager persistenceManager, EventManager eventManager, AssetManager assetManager, EventScheduleManager eventScheduleManager) {
		super(SCHEDULE_CRITERIA, EventScheduleJobAssignment.class, persistenceManager, eventManager, assetManager, eventScheduleManager);
	}

    @Override
    protected ReportConfiguration loadReportConfiguration() {
        return new EventToJobColumnsService().getReportConfiguration(getSecurityFilter());
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
		getContainer().setOwner(job.getOwner());
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
			QueryBuilder<Project> eventJob = new QueryBuilder<Project>(Project.class, getSecurityFilter());
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
		QueryBuilder<Long> jobId = new QueryBuilder<Long>(EventSchedule.class, getSecurityFilter());
		jobId.setSimpleSelect("project.id").addLeftJoin("project", "project").addSimpleWhere("id", Long.valueOf(scheduleId));
		return persistenceManager.find(jobId);
	}
	


}
