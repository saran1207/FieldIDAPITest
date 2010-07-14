package com.n4systems.fieldid.actions.location;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.viewhelpers.PredefinedLocationCrudHelper;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.location.PredefinedLocationSaver;
import com.n4systems.security.Permissions;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

@UserPermissionFilter(userRequiresOneOf = { Permissions.ManageSystemConfig })
public class PredefinedLocationCrud extends AbstractCrud {

	private Logger logger = Logger.getLogger(PredefinedLocationCrud.class);
	private static final long serialVersionUID = 1L;
	private PredefinedLocation predefinedLocation;
	private PredefinedLocation parentNode;
	private String name;
	private Long parentId;
	private Long nodeId;
	private PredefinedLocationSaver saver;

	public PredefinedLocationCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
		predefinedLocation = new PredefinedLocation();
	
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		predefinedLocation = loadLocation(uniqueId);
	}

	@Override
	protected void postInit() {
		super.postInit();
		saver = new PredefinedLocationSaver();
		overrideHelper(new PredefinedLocationCrudHelper(getLoaderFactory()));
	}

	@SkipValidation
	public String doList() {
		return SUCCESS;
	}

	@SkipValidation
	public String doAdd() {
		return SUCCESS;
	}
	
	@SkipValidation
	public String doEdit(){
		testDependencies();
		name = predefinedLocation.getName();
		return SUCCESS;
	}

	public String doCreate() {
		try {
			predefinedLocation.setParent(getParentNode(getParentId()));
			predefinedLocation.setName(getName());
			predefinedLocation.setTenant(getTenant());

			saver.save(predefinedLocation);

			addFlashMessageText("message.location_saved");
		} catch (Exception e) {
			logger.error("could not save predefined location", e);
			addActionErrorText("error.saving_predefined_location");
			return ERROR;
		}
		return SUCCESS;
	}

	public String doUpdate() {
		testDependencies();
		try {
			predefinedLocation.setName(getName());
			predefinedLocation = saver.update(predefinedLocation);

			addFlashMessageText("message.location_updated");
		} catch (Exception e) {
			logger.error("could not update predefined location", e);
			addActionErrorText("error.updating_predefined_location");
			return ERROR;
		}
		return SUCCESS;
	}

	private PredefinedLocation loadLocation(Long nodeId) {
		try {
			return getLoaderFactory().createFilteredIdLoader(PredefinedLocation.class).setId(nodeId).load();
		} catch (Exception e) {
			logger.error("Could Not Load Predefined Location", e);
		}
		return null;
	}

	@RequiredStringValidator(message = "", key = "error.titlerequired")
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public PredefinedLocation getParentNode(Long parentId) {
		if (parentId == null) {
			parentNode = null;
		} else {
			parentNode = loadLocation(parentId);
		}
		return parentNode;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public Long getNodeId() {
		return nodeId;
	}

	private void testDependencies(){
		if (predefinedLocation == null || predefinedLocation.isNew()) {
			addActionErrorText("error.nopredefinedlocation");
			throw new MissingEntityException();
		}
	}
	
}
