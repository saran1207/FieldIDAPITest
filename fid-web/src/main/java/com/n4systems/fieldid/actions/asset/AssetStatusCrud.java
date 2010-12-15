package com.n4systems.fieldid.actions.asset;

import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.AssetStatus;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.AssetType;
import com.n4systems.model.StateSet;
import com.n4systems.security.Permissions;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class AssetStatusCrud extends AbstractCrud implements HasDuplicateValueValidator {

	private static final long serialVersionUID = 1L;
	
	private LegacyAsset assetManager;
	private AssetStatus assetStatus;
	
	private List<AssetStatus> assetStatuses;
	
	public AssetStatusCrud(LegacyAsset assetManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.assetManager = assetManager;
	}
	
	@Override
	protected void initMemberFields() {
		assetStatus = new AssetStatus();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		assetStatus = assetManager.findAssetStatus( uniqueID, getTenantId() );
	}
	
	
	@SkipValidation
	public String doList() {
		return SUCCESS;
	}

	@SkipValidation
	public String doLoadEdit() {
		if ( assetStatus == null ) {
			addActionError("Asset Status not found");
			return ERROR;
		}
		
		return INPUT;
	}
		
	public String doSave() {
		assetStatus.setModifiedBy( getSessionUser().getName() );
		if ( assetStatus.getUniqueID() == null ) {
			assetStatus.setTenant(getTenant());
			
			persistenceManager.saveAny(assetStatus);
		} else {
			persistenceManager.updateAny(assetStatus);
		}
		
		addActionMessage("Data has been updated.");
		
		return SUCCESS;
	}
	
	@SkipValidation
	public String doRemove() {
		if (assetStatus == null) {
			addActionError("Asset Status not found");
			return ERROR;
		}
		try {
			persistenceManager.deleteAny(assetStatus);
		} catch ( Exception e ) {
			addActionError("Asset Status can not be removed");
			return ERROR;
		}
		addActionMessage("Asset Status has be removed");
		return SUCCESS;
	}
	
	public List<AssetStatus> getAssetStatuses() {
		if( assetStatuses == null ) {
			assetStatuses = getLoaderFactory().createAssetStatusListLoader().load();
		}
		return assetStatuses;
	}

	public AssetStatus getAssetStatus() {
		return assetStatus;
	}
	
	
	@RequiredStringValidator(type=ValidatorType.FIELD, message = "", key="error.required")
	@CustomValidator(type = "uniqueValue", message = "", key = "error.assetstatusduplicate")
	public void setName(String name) {
		assetStatus.setName(name);
	}
	
	public String getName() {
		return assetStatus.getName();
	}

	public boolean duplicateValueExists( String formValue ) {
		return !persistenceManager.uniqueAssetStatusNameAvailable(AssetStatus.class, formValue, uniqueID, getTenantId());
	}

}
