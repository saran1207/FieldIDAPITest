package com.n4systems.fieldid.actions.projects;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.web.helper.Constants;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProjectManager;
import com.n4systems.fieldid.actions.helpers.UploadFileSupport;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Project;
import com.n4systems.security.Permissions;
import com.n4systems.tools.Pager;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.Projects)
@UserPermissionFilter(userRequiresOneOf={Permissions.ManageJobs})
public class ProjectNoteCrud extends UploadFileSupport {

	private static final Logger logger = Logger.getLogger(ProjectNoteCrud.class);
	private static final long serialVersionUID = 1L;

	private ProjectManager projectManager;

	private FileAttachment note;
	private Project project;

	private Pager<FileAttachment> page;


	public ProjectNoteCrud(ProjectManager projectManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.projectManager = projectManager;
		note = new FileAttachment();
	}

	@Override
	protected void initMemberFields() {
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		note.setId(uniqueId);
		
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={})
	public String doList() {
		if (project == null) {
			addActionErrorText("error.noproject");
			return MISSING;
		}

		page = projectManager.getNotesPaged(project, getCurrentPage(), Constants.PAGE_SIZE);
		return SUCCESS;
	}

	@SkipValidation
	public String doAdd() {
		if (project == null) {
			addActionErrorText("error.noproject");
			return MISSING;
		}
		return SUCCESS;
	}

	public String doCreate() {
		if (project == null) {
			addActionErrorText("error.noproject");
			return MISSING;
		}

		try {
			note = projectManager.attachNote(note, project, getSessionUser().getUniqueID());
			logger.info(getLogLinePrefix() + "note attached to " + project.getProjectID());
			addFlashMessageText("message.noteadded");
		} catch (Exception e) {
			logger.error(getLogLinePrefix() + "could not attach the file.", e);
			addActionErrorText("error.projectnotecreate");
			return ERROR;
		}
		return SUCCESS;
	}

	@SkipValidation
	public String doDelete() {
		if (project == null) {
			addActionErrorText("error.noproject");
			return MISSING;
		}

		try {
			projectManager.detachNote(note, project, getSessionUser().getUniqueID());
			logger.info(getLogLinePrefix() + "note detached to " + project.getProjectID());
			addFlashMessageText("message.notedeleted");
		} catch (Exception e) {
			logger.error(getLogLinePrefix() + "could not detach the file.", e);
			addFlashErrorText("error.projectnotedeleted");
			return ERROR;
		}

		return SUCCESS;
	}

	public FileAttachment getNote() {
		return note;
	}

	public Project getJob() {
		return project;
	}
	
	public Project getProject() {
		return project;
	}

	public void setProjectId(Long projectId) {
		if (projectId == null) {
			project = null;
		} else if (project == null || !projectId.equals(project.getId())) {
			project = persistenceManager.find(Project.class, projectId, getSecurityFilter());

		}
	}
	public Long getProjectId() {
		return (project != null) ? project.getId() : null; 
	}

	public Pager<FileAttachment> getPage() {
		return page;
	}

	public String getComments() {
		return note.getComments();
	}

	public String getFileName() {
		return note.getFileName();
	}

	@RequiredStringValidator(message = "", key = "error.noterequired")
	public void setComments(String comments) {
		note.setComments(comments);
	}

	@CustomValidator(type = "fileSecurityValidator", message = "", key = "error.invalidfile")
	public void setFileName(String fileName) {
		note.setFileName(fileName);
	}

	
}
