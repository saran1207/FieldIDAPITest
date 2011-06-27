package com.n4systems.fieldid.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.n4systems.ejb.legacy.AssetCodeMappingService;
import com.n4systems.fieldid.actions.helpers.AssetTypeLister;
import com.n4systems.model.AssetType;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.AssetCodeMapping;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.InfoFieldInput;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.security.Permissions;
import com.n4systems.util.StringListingPair;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;


@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.Integration)
@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class AssetCodeMappingCrud extends AbstractCrud {


	private static final long serialVersionUID = 1L;

	private AssetCodeMappingService assetCodeMappingServiceManager;
	
	private List<AssetCodeMapping> assetCodeMappings;
	private AssetCodeMapping assetCodeMapping;
	
	private AssetTypeLister assetTypes;
	
	private List<InfoOptionInput> assetInfoOptions;
	
	private AssetType assetType;
	
	private boolean assetTypeUpdate = false;
	
	public AssetCodeMappingCrud(AssetCodeMappingService assetCodeMappingServiceManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.assetCodeMappingServiceManager = assetCodeMappingServiceManager;
	}
	
	@Override
	protected void initMemberFields() {
		assetCodeMapping = new AssetCodeMapping();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		assetCodeMapping = assetCodeMappingServiceManager.getAssetCodeByUniqueIdAndTenant( uniqueId, getTenantId() );
	}


	@SkipValidation
	public String doEdit() {
		if( assetCodeMapping == null ) {
			addActionError("Asset Code Mapping not found.");
			return ERROR;
		}
		return INPUT;
	}
	
	
	public String doSave() {
		if (assetCodeMapping == null) {
			addActionError("Asset Code Mapping not found.");
			return ERROR;
		}

		if(assetTypeUpdate) {
			assetInfoOptions = null;
			return INPUT;
		}
		
		assetCodeMapping.setTenant(getTenant());
		
		convertInputsToInfoOptions();
		try {
			assetCodeMappingServiceManager.update(assetCodeMapping);
			addActionMessage("Data has been updated.");
			return SUCCESS;
		} catch (Exception e) {
			addActionError("Failed to update.");
			return INPUT;
		}
		
	}
	
	private void convertInputsToInfoOptions() {
		List<InfoOptionBean> options = InfoOptionInput.convertInputInfoOptionsToInfoOptions(assetInfoOptions, assetCodeMapping.getAssetInfo().getInfoFields(), getSessionUser() );
		assetCodeMapping.setInfoOptions( options );
	}
	
	
	@SkipValidation
	public String doList() {
		return SUCCESS;
	}
	
	@SkipValidation
	public String doRemove() {
		if ( assetCodeMapping == null) {
			addActionError("Asset Code Mapping not found");
			return ERROR;
		}
		try {
			assetCodeMappingServiceManager.deleteByIdAndTenant( uniqueID, getTenantId() );
		} catch ( Exception e ) {
			addActionError("Asset Code Mapping can not be removed");
			return ERROR;
		}
		addActionMessage("Asset Code Mapping has be removed");
		return SUCCESS;
	}
	
	public List<AssetCodeMapping> getAssetCodeMappings() {
		if( assetCodeMappings == null ) {
			assetCodeMappings = assetCodeMappingServiceManager.getAllAssetCodesByTenant( getTenantId() );
		}
		return assetCodeMappings;
	}

	public AssetCodeMapping getAssetCodeMapping() {
		return assetCodeMapping;
	}

	public AssetTypeLister getAssetTypes() {
		if (assetTypes == null) {
			assetTypes = new AssetTypeLister(persistenceManager, getSecurityFilter());
		}

		return assetTypes;
	}

	public void setAssetCodeMapping(AssetCodeMapping assetCodeMapping) {
		this.assetCodeMapping = assetCodeMapping;
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "", key = "error.assetcoderequired")
	public String getAssetCode() {
		return assetCodeMapping.getAssetCode();
	}

	public void setAssetCode(String assetCode) {
		this.assetCodeMapping.setAssetCode( assetCode );
	}
	
	public String getCustomerRefNumber() {
		return assetCodeMapping.getCustomerRefNumber();
	}

	public void setCustomerRefNumber(String customerRefNumber) {
		assetCodeMapping.setCustomerRefNumber(customerRefNumber);
	}
	
	public AssetType getAssetTypeBean() {
		if( assetType == null && assetCodeMapping.getAssetInfo() != null ) {
			assetType = assetCodeMapping.getAssetInfo();
		}
		return assetType;
	}
	
	@RequiredFieldValidator(type = ValidatorType.FIELD, message = "", key = "error.assettyperequired")
	public Long getAssetType() {
		return (assetCodeMapping.getAssetInfo() != null ) ? assetCodeMapping.getAssetInfo().getId() : null ;
	}
	
	public Collection<InfoFieldBean> getAssetInfoFields() {
		if( assetCodeMapping.getAssetInfo() != null ) {
			return assetCodeMapping.getAssetInfo().getInfoFields();
		}
		return new ArrayList<InfoFieldBean>(); 
	}

	public void setAssetType(Long assetTypeId ) {
		AssetType assetType = null ;
		if( assetTypeId != null ) {
			assetType =  getLoaderFactory().createAssetTypeLoader().setId(assetTypeId).setStandardPostFetches().load();
		}
		this.assetCodeMapping.setAssetInfo(assetType);
	}

	public List<InfoOptionInput> getAssetInfoOptions() {
		if( assetInfoOptions == null ) {
			assetInfoOptions = new ArrayList<InfoOptionInput>();
			if( assetCodeMapping.getAssetInfo() != null ) {
				assetInfoOptions = InfoOptionInput.convertInfoOptionsToInputInfoOptions( assetCodeMapping.getInfoOptions(), assetCodeMapping.getAssetInfo().getInfoFields(), getSessionUser() );
			}
		}
		return assetInfoOptions;
	}
	
	public void setAssetInfoOptions(List<InfoOptionInput> infoOptions) {
		this.assetInfoOptions = infoOptions;
	}

	public boolean isAssetTypeUpdate() {
		return assetTypeUpdate;
	}

	public void setAssetTypeUpdate(boolean assetTypeUpdate) {
		this.assetTypeUpdate = assetTypeUpdate;
	}

	public List<StringListingPair> getComboBoxInfoOptions( InfoFieldBean field, InfoOptionInput inputOption ) {
		return InfoFieldInput.getComboBoxInfoOptions( field, inputOption  );
	}
	
}
