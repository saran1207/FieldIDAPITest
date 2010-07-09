package com.n4systems.fieldid.actions.location;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
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
	}

	@Override
	protected void postInit() {
		super.postInit();
		overrideHelper(new PredefinedLocationCrudHelper(getLoaderFactory()));
	}

	@SkipValidation
	public String doEdit() {
		return SUCCESS;
	}

	@SkipValidation
	public String doList() {
		return SUCCESS;
	}

	public String doUpdate() {
		// testRequiredEntities(false);
		try {
			saver = new PredefinedLocationSaver();
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
			parentNode = getLoaderFactory().createFilteredIdLoader(PredefinedLocation.class).setId(parentId).load();
		}
		return parentNode;
	}
	
	private List<PredefinedLocation> getEmpty(){
		return new ArrayList<PredefinedLocation>();
	}

}
