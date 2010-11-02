package com.n4systems.fieldid.actions.projects;

import java.util.List;

import com.n4systems.model.Asset;
import com.n4systems.model.EventSchedule;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.web.helper.Constants;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProjectManager;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Project;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.utils.CompressedScheduleStatus;
import com.n4systems.security.Permissions;
import com.n4systems.services.JobListService;
import com.n4systems.tools.Pager;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidationParameter;

@ExtendedFeatureFilter(requiredFeature = ExtendedFeature.Projects)
@UserPermissionFilter(userRequiresOneOf = { Permissions.ManageJobs })
public class ProjectCrud extends AbstractCrud implements HasDuplicateValueValidator {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ProjectCrud.class);

	private ProjectManager projectManager;
	private JobResourcesCrud jobResources;

	private OwnerPicker ownerPicker;

	private Project project;
	private boolean justAssignedOn = false;
	private Pager<Project> page;

	private String actualCompletion;
	private String estimatedCompletion;
	private String started;
	private Pager<FileAttachment> notesPaged;
	private Pager<Asset> assetsPaged;
	private Pager<EventSchedule> schedulesPaged;

	private String searchID;

	public ProjectCrud(PersistenceManager persistenceManager, ProjectManager projectManager) {
		super(persistenceManager);
		this.projectManager = projectManager;
		jobResources = new JobResourcesCrud(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
		project = new Project();
		project.setOwner(getSessionUserOwner());
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), project);
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		project = persistenceManager.find(Project.class, uniqueId, getSecurityFilter());
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), project);
	}

	private void testRequiredEntities(boolean notNew) {
		if (project == null || (notNew && project.isNew())) {
			addActionErrorText("error.noproject");
			throw new MissingEntityException();
		}
	}

	@SkipValidation
	@UserPermissionFilter(open = true)
	public String doShow() {
		testRequiredEntities(true);
		jobResources.setJobId(uniqueID);
		return SUCCESS;
	}

	@SkipValidation
	@UserPermissionFilter(open = true)
	public String doSearch() {
		try {
			JobListService jobListService = new JobListService(persistenceManager, getSecurityFilter(), Constants.PAGE_SIZE);
			jobListService.setPageNumber(getCurrentPage());
			page = jobListService.getList(justAssignedOn, false, searchID);
		} catch (InvalidQueryException iqe) {
			logger.error(getLogLinePrefix() + "bad search query", iqe);
			return ERROR;
		}

		return jobFound() ? SUCCESS : ERROR;
	}

	private boolean jobFound() {
		/* If size == 1 search was successful and found exactly one match */
		if (page.getList().size() == 1) {
			uniqueID = page.getList().get(0).getID();
			return true;
		} else {
			return false;
		}
	}

	@SkipValidation
	@UserPermissionFilter(open = true)
	public String doList() {
		try {
			JobListService jobListService = new JobListService(persistenceManager, getSecurityFilter(), Constants.PAGE_SIZE);
			jobListService.setPageNumber(getCurrentPage());
			page = jobListService.getList(justAssignedOn, false, null);

		} catch (InvalidQueryException iqe) {
			logger.error(getLogLinePrefix() + "bad search query", iqe);
			return ERROR;
		}
		return SUCCESS;
	}

	@SkipValidation
	public String doAdd() {
		testRequiredEntities(false);
		return SUCCESS;
	}

	public String doCreate() {
		testRequiredEntities(false);
		processDates();
		project.setTenant(getTenant());
		try {
			uniqueID = persistenceManager.save(project, getSessionUser().getUniqueID());
			logger.info(getLogLinePrefix() + "project " + project.getProjectID() + " created");
			addFlashMessageText("message.projectsaved");
		} catch (Exception e) {
			logger.error(getLogLinePrefix() + "could not save project ", e);
			addActionErrorText("error.savingproject");
			return ERROR;
		}

		return SUCCESS;
	}

	@SkipValidation
	public String doEdit() {
		testRequiredEntities(true);
		convertDatesForWeb();
		return SUCCESS;
	}

	private void convertDatesForWeb() {
		started = convertDateTime(project.getStarted());
		actualCompletion = convertDateTime(project.getActualCompletion());
		estimatedCompletion = convertDateTime(project.getEstimatedCompletion());
	}

	public String doUpdate() {
		testRequiredEntities(true);

		processDates();
		try {
			project = persistenceManager.update(project, getSessionUser().getUniqueID());
			logger.info(getLogLinePrefix() + "project " + project.getProjectID() + " updated");
			addFlashMessageText("message.projectsaved");
		} catch (Exception e) {
			logger.error(getLogLinePrefix() + "could not save project ", e);
			addActionErrorText("error.savingproject");
			return ERROR;
		}

		return SUCCESS;
	}

	private void processDates() {
		project.setActualCompletion(convertDateTime(actualCompletion));
		project.setStarted(convertDateTime(started));
		project.setEstimatedCompletion(convertDateTime(estimatedCompletion));
	}

	@SkipValidation
	public String doDelete() {
		testRequiredEntities(true);

		project.setRetired(true);
		try {
			project = persistenceManager.update(project, getSessionUser().getUniqueID());
			logger.info(getLogLinePrefix() + "project " + project.getProjectID() + " deleted");
			addFlashMessageText("message.projectdeleted");
		} catch (Exception e) {
			logger.error(getLogLinePrefix() + "could not delete project ", e);
			addActionErrorText("error.deletingproject");
			return ERROR;
		}

		return SUCCESS;
	}

	public Project getProject() {
		return project;
	}

	public Project getJob() {
		return project;
	}

	public String getActualCompletion() {
		return actualCompletion;
	}

	public String getDuration() {
		return project.getDuration();
	}

	public String getEstimatedCompletion() {
		return estimatedCompletion;
	}

	public String getName() {
		return project.getName();
	}

	public String getProjectID() {
		return project.getProjectID();
	}

	public String getStarted() {
		return started;
	}

	public String getStatus() {
		return project.getStatus();
	}

	public String getDescription() {
		return project.getDescription();
	}

	public String getWorkPerformed() {
		return project.getWorkPerformed();
	}

	public String getPoNumber() {
		return project.getPoNumber();
	}

	public boolean isOpen() {
		return project.isOpen();
	}

	@CustomValidator(type = "n4systemsDateValidator", message = "", key = "error.mustbeadatetime", parameters = { @ValidationParameter(name = "usingTime", value = "true") })
	public void setActualCompletion(String actualCompletion) {
		this.actualCompletion = actualCompletion;
	}

	public void setSearchID(String searchID) {
		if (searchID.equals("")) {
			this.searchID = null;
		} else {
			this.searchID = searchID;
		}
	}

	public void setDuration(String duration) {
		project.setDuration(duration);
	}

	@CustomValidator(type = "n4systemsDateValidator", message = "", key = "error.mustbeadatetime", parameters = { @ValidationParameter(name = "usingTime", value = "true") })
	public void setEstimatedCompletion(String estimatedCompletion) {
		this.estimatedCompletion = estimatedCompletion;
	}

	@RequiredStringValidator(message = "", key = "error.titlerequired")
	public void setName(String name) {
		project.setName(name);
	}

	@RequiredStringValidator(message = "", key = "error.projectidrequired")
	@CustomValidator(type = "uniqueValue", message = "", key = "errors.projectidduplicated")
	public void setProjectID(String projectID) {
		project.setProjectID(projectID);
	}

	@CustomValidator(type = "n4systemsDateValidator", message = "", key = "error.mustbeadatetime", parameters = { @ValidationParameter(name = "usingTime", value = "true") })
	public void setStarted(String started) {
		this.started = started;
	}

	public void setStatus(String status) {
		project.setStatus(status);
	}

	public void setDescription(String description) {
		project.setDescription(description);
	}

	public void setWorkPerformed(String workPerformed) {
		project.setWorkPerformed(workPerformed);
	}

	public void setPoNumber(String poNumber) {
		project.setPoNumber(poNumber);
	}

	public void setOpen(boolean open) {
		project.setOpen(open);
	}

	public Pager<Project> getPage() {
		return page;
	}

	public boolean duplicateValueExists(String assetId) {
		QueryBuilder<Project> query = new QueryBuilder<Project>(Project.class, new OpenSecurityFilter());
		query.setCountSelect().addWhere(Comparator.EQ, "projectID", "projectID", assetId, WhereParameter.IGNORE_CASE);
		query.addSimpleWhere("tenant", getTenant());
		if (uniqueID != null) {
			query.addWhere(Comparator.NE, "id", "id", project.getId());
		}

		try {
			if (persistenceManager.findCount(query) == 0) {
				return false;
			}
		} catch (InvalidQueryException e) {
			logger.error("invalid duplicate test query", e);
		}
		return true;
	}

	public List<FileAttachment> getNotes() {
		if (notesPaged == null) {
			notesPaged = projectManager.getNotesPaged(project, 1, Constants.SUMMARY_SIZE);
		}
		return notesPaged.getList();
	}

	public List<Asset> getAssets() {
		if (assetsPaged == null) {
			assetsPaged = projectManager.getAssetsPaged(project, getSecurityFilter(), 1, Constants.SUMMARY_SIZE);
		}
		return assetsPaged.getList();
	}

	public List<EventSchedule> getSchedules() {
		if (schedulesPaged == null) {
			schedulesPaged = projectManager.getSchedulesPaged(project, getSecurityFilter(), 1, Constants.SUMMARY_SIZE, CompressedScheduleStatus.ALL.getScheduleStatuses());
		}
		return schedulesPaged.getList();
	}

	public boolean isEventJob() {
		return project.isEventJob();
	}

	public void setEventJob(boolean eventJob) {
		project.setEventJob(eventJob);
	}

	public Long getCountOfIncompleteSchedules() {
		return projectManager.getIncompleteSchedules(project, getSecurityFilter());
	}

	public Long getCountOfCompleteSchedules() {
		return projectManager.getCompleteSchedules(project, getSecurityFilter());
	}

	public JobResourcesCrud getJobResources() {
		return jobResources;
	}

	public boolean isJustAssignedOn() {
		return justAssignedOn;
	}

	public void setJustAssignedOn(boolean justAssignedOn) {
		this.justAssignedOn = justAssignedOn;
	}

	@RequiredFieldValidator(message = "", key = "error.owner_required")
	public BaseOrg getOwner() {
		return ownerPicker.getOwner();
	}

	public Long getOwnerId() {
		return ownerPicker.getOwnerId();
	}

	public void setOwnerId(Long id) {
		ownerPicker.setOwnerId(id);
	}
}
