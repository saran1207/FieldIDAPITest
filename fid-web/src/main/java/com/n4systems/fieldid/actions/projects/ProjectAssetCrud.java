package com.n4systems.fieldid.actions.projects;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProjectManager;
import com.n4systems.exceptions.AssetAlreadyAttachedException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.Asset;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Project;
import com.n4systems.security.Permissions;
import com.n4systems.tools.Pager;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import rfid.web.helper.Constants;

@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.Projects)
@UserPermissionFilter(userRequiresOneOf={Permissions.MANAGE_JOBS})
public class ProjectAssetCrud extends AbstractCrud {

	private static final Logger logger = Logger.getLogger(ProjectAssetCrud.class);
	private static final long serialVersionUID = 1L;

	private ProjectManager projectManager;
	private Asset asset;

	private Project project;

	private Pager<Asset> page;


	public ProjectAssetCrud(ProjectManager projectManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.projectManager = projectManager;
	}

	@Override
	protected void initMemberFields() {
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		asset = persistenceManager.find(Asset.class, uniqueId, getSecurityFilter());
		
	}

	@SkipValidation
	@UserPermissionFilter(open=true)
	public String doList() {
		if (project == null || project.isEventJob()) {
			addActionErrorText("error.noproject");
			return MISSING;
		}
		
		return SUCCESS;
	}

	@SkipValidation
	public String doAdd() {
		if (project == null || project.isEventJob()) {
			addActionErrorText("error.noproject");
			return MISSING;
		}
		return SUCCESS;
	}

	public String doCreate() {
		if (project == null || project.isEventJob()) {
			addActionErrorText("error.noproject");
			return MISSING;
		}
		
		if (asset == null) {
			addActionErrorText("error.noasset");
			return MISSING;
		}

		try {
			projectManager.attachAsset(asset, project, getSessionUser().getUniqueID());
			logger.info(getLogLinePrefix() + "asset " + asset.getIdentifier() + " attached to " + project.getProjectID());
			addFlashMessageText("message.assetattachedtoproject");
			return SUCCESS;
		} catch (AssetAlreadyAttachedException e) {
			logger.info(getLogLinePrefix() + "could not attach asset already attached to project");
			addFlashErrorText("error.assetareadyattached");
		} catch (Exception e) {
			logger.error(getLogLinePrefix() + "could not attach asset", e);
			addFlashErrorText("error.attachingprojectasset");
		}

		return ERROR;
	}

	@SkipValidation
	public String doDelete() {
		if (project == null || project.isEventJob()) {
			addActionErrorText("error.noproject");
			return MISSING;
		}
		if (asset == null) {
			addActionErrorText("error.noasset");
			return MISSING;
		}

		try {
			projectManager.detachAsset(asset, project, getSessionUser().getUniqueID());
			logger.info(getLogLinePrefix() + "asset " + asset.getIdentifier() + " detached from " + project.getProjectID());
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

	public Pager<Asset> getPage() {
		if (page == null) {
			page = projectManager.getAssetsPaged(project, getSecurityFilter(), getCurrentPage(), Constants.PAGE_SIZE);
		}
		
		return page;
	}

	public Asset getAsset() {
		return asset;
	}

    @Override
    public String getIEHeader() {
        return "EmulateIE8";
    }
}
