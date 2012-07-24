package com.n4systems.fieldid.actions.projects;

import com.n4systems.model.Event;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.web.helper.Constants;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProjectManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.Project;
import com.n4systems.model.utils.CompressedScheduleStatus;
import com.n4systems.security.Permissions;
import com.n4systems.tools.Pager;

@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.Projects)
@UserPermissionFilter(userRequiresOneOf={Permissions.ManageJobs})
public class ProjectEventCrud extends AbstractCrud {

	private static final Logger logger = Logger.getLogger(ProjectEventCrud.class);
	private static final long serialVersionUID = 1L;

	private ProjectManager projectManager;
	private Event openEvent;

	private Project project;

	private Pager<Event> page;

	private CompressedScheduleStatus searchStatuses = CompressedScheduleStatus.ALL;

	public ProjectEventCrud(ProjectManager projectManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.projectManager = projectManager;
	}

	@Override
	protected void initMemberFields() {
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		openEvent = persistenceManager.find(Event.class, uniqueId, getSecurityFilter());
	}
	
	@SkipValidation
	@UserPermissionFilter(open=true)
	public String doList() {
		if (project == null || !project.isEventJob()) {
			addActionErrorText("error.noproject");
			return MISSING;
		}
		
		return SUCCESS;
	}

	@SkipValidation
	public String doDelete() {
		if (project == null || !project.isEventJob()) {
			addActionErrorText("error.noproject");
			return MISSING;
		}
		if (openEvent == null) {
			addActionErrorText("error.noschedule");
			return MISSING;
		}

		try {
			openEvent.setProject(null);
			persistenceManager.update(openEvent, getSessionUser().getId());
			logger.info(getLogLinePrefix() + "schedule " + openEvent.getId() + " detached from " + project.getProjectID());
			addFlashMessageText("message.assetdetachedfromproject");
			return SUCCESS;
		} catch (Exception e) {
			logger.error(getLogLinePrefix() + "could not detach asset", e);
			addFlashErrorText("error.detachingprojectasset");
		}

		return ERROR;
	}

	public Project getProject() {
		return project;
	}
	
	public Project getJob() {
		return project;
	}
	
	public Long getProjectId() {
		return (project != null) ? project.getId() : null;
	}

	public void setProjectId(Long projectId) {
		if (projectId == null) {
			project = null;
		} else if (project == null || !projectId.equals(project.getId())) {
			project = persistenceManager.find(Project.class, projectId, getSecurityFilter(), "assets");
		}
	}

	public Pager<Event> getPage() {
		if (page == null) {
			page = projectManager.getSchedulesPaged(project, getSecurityFilter(), getCurrentPage(), Constants.PAGE_SIZE, searchStatuses.getEventStates());
		}

		return page;
	}

	public Event getSchedule() {
		return openEvent;
	}

	public String getSearchStatuses() {
		return searchStatuses.name();
	}

	public void setSearchStatuses(String searchStatuses) {
		this.searchStatuses = CompressedScheduleStatus.valueOf(searchStatuses);
	}

	public CompressedScheduleStatus[] getScheduleStatuses() {
		return CompressedScheduleStatus.values();
	}
	
	public Long getCountOfIncompleteSchedules() {
		return projectManager.getIncompleteSchedules(project, getSecurityFilter());
	}
	
	public Long getCountOfCompleteSchedules() {
		return projectManager.getCompleteSchedules(project, getSecurityFilter());
	}
}
