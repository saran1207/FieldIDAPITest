package com.n4systems.fieldid.actions.asset;

import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.AssetStatus;
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
	private List<AssetStatus> archivedAssetStatuses;
	
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
		saveAssetStatus(assetStatus);		
		addActionMessage("Data has been updated.");
		
		return SUCCESS;
	}

	private void saveAssetStatus(AssetStatus assetStatus) {
		assetStatus.setModifiedBy( getUser() );
		if ( assetStatus.getId() == null ) {
			assetStatus.setTenant(getTenant());
			
			persistenceManager.saveAny(assetStatus);
		} else {
			persistenceManager.updateAny(assetStatus);
		}
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
		addActionMessage("Asset Status has been removed");
		return SUCCESS;
	}
	
	@SkipValidation
	public String doArchive() {
		if (assetStatus == null) {
			addActionError("Asset Status not found");
			return ERROR;
		} else {
			assetStatus.archiveEntity();
			saveAssetStatus(assetStatus);
			addActionMessage("Asset Status has been archived");

			return SUCCESS;
		}
	}
	
	@SkipValidation
	public String doUnarchive() {
		if (assetStatus == null) {
			addActionError("Asset Status not found");
			return ERROR;
		} else {
			assetStatus.activateEntity();
			saveAssetStatus(assetStatus);
			addActionMessage("Asset Status has been unarchived");
			return SUCCESS;
		}
	}
	
	public List<AssetStatus> getAssetStatuses() {
		if( assetStatuses == null ) {
			assetStatuses = getLoaderFactory().createAssetStatusListLoader().setPostFetchFields("modifiedBy", "createdBy").load();
		}
		return assetStatuses;
	}
	
	public List<AssetStatus> getArchivedAssetStatuses() {
		if( archivedAssetStatuses == null ) {
			archivedAssetStatuses = getLoaderFactory().createAssetStatusListLoader().setPostFetchFields("modifiedBy", "createdBy").archivedOnly().load();
		}
		return archivedAssetStatuses;
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
		return getLoaderFactory().createAssetStatusForNameExistsLoader().setName(formValue).setId(uniqueID).load();
	}

}
