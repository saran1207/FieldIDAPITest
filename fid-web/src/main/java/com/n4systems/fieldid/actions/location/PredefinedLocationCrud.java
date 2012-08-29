package com.n4systems.fieldid.actions.location;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.fieldid.viewhelpers.PredefinedLocationCrudHelper;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.location.PredefinedLocationSaver;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.security.Permissions;
import com.n4systems.uitags.views.HierarchicalNode;
import com.n4systems.util.ServiceLocator;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validation;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import java.util.List;

@Validation
@UserPermissionFilter(userRequiresOneOf = { Permissions.ManageSystemConfig })
public class PredefinedLocationCrud extends AbstractCrud implements HasDuplicateValueValidator {

	private Logger logger = Logger.getLogger(PredefinedLocationCrud.class);
	private static final long serialVersionUID = 1L;
	public PredefinedLocation predefinedLocation;
	private PredefinedLocation parentNode;
	private String name;
	private Long parentId;
	private Long nodeId;
	private PredefinedLocationSaver saver;
    private BaseOrg owner;
    private Long ownerId;
    private OwnerPicker ownerPicker;

    public PredefinedLocationCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
		predefinedLocation = new PredefinedLocation(getTenant(), null, null );
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		predefinedLocation = loadLocation(uniqueId);
	}

	@Override
	protected void postInit() {
		super.postInit();
		saver = new PredefinedLocationSaver();
        ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), predefinedLocation);
        if (predefinedLocation!=null && predefinedLocation.getOwner()!=null) {
            ownerPicker.updateOwner(predefinedLocation.getOwner());
        }
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
	public String doDelete(){
		testDependencies();
		try {
			saver.remove(predefinedLocation);

			addFlashMessageText("message.location_removed");
		} catch (Exception e) {
			logger.error("could not remove predefined location", e);
			addActionErrorText("error.removing_predefined_location");
			return ERROR;
		}
		return SUCCESS;
	}
	
	
	@SkipValidation
	public String doEdit() {
		testDependencies();
		name = predefinedLocation.getName();
		return SUCCESS;
	}

	public String doCreate() {
		try {
			predefinedLocation.setParent(getParentNode(getParentId()));
			predefinedLocation.setName(getName());
			predefinedLocation.setTenant(getTenant());
            if (predefinedLocation.getParent()==null) {
                predefinedLocation.setOwner(getUser().getOwner());
            } else {
                predefinedLocation.setOwner(predefinedLocation.getTopLevelOwner());
            }

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

            ServiceLocator.getPredefinedLocationManager().updateChildrenOwner(getSecurityFilter(), predefinedLocation);

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

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "", key = "error.titlerequired")
	@CustomValidator(type = "uniqueValue", message = "", key = "error.titleunique")
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

	public PredefinedLocation getParentNode() {
		return parentNode;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public PredefinedLocation getPredefinedLocation() {
		return predefinedLocation;
	}

	private void testDependencies() {
		if (predefinedLocation == null || predefinedLocation.isNew()) {
			addActionErrorText("error.no_predefinedlocation");
			throw new MissingEntityException();
		}
	}

	@Override
	public boolean duplicateValueExists(String name) {
		List<HierarchicalNode> siblings = getLocationCrudHelper().findSiblingsByParent(parentId);

        Long id = getPredefinedLocation().getId();
		for (HierarchicalNode node : siblings) {
			if (!node.getId().equals(id) && name.equals(node.getName())) {
				return true;
			}
		}
		return false;
	}

	private PredefinedLocationCrudHelper getLocationCrudHelper() {
		return (PredefinedLocationCrudHelper) getHelper();
	}


    public Long getOwnerId() {
        return ownerPicker.getOwnerId();
    }

    public void setOwnerId(Long ownerId) {
        ownerPicker.setOwnerId(ownerId);
    }

    public BaseOrg getOwner() {
        return ownerPicker.getOwner();
    }

    public void setOwner(BaseOrg owner) {
        ownerPicker.setOwnerId(owner.getId());
    }

}
