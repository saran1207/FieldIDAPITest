package com.n4systems.fieldid.actions.safetyNetwork;

import java.util.Collection;
import java.util.List;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.fieldid.actions.asset.AssetView;
import com.n4systems.fieldid.actions.asset.AssetViewModeConverter;
import com.n4systems.fieldid.actions.asset.AssetWebModel;
import com.n4systems.fieldid.actions.helpers.AssetExtensionValueInput;
import com.n4systems.fieldid.actions.helpers.AssetTypeLister;
import com.n4systems.model.Asset;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.AssetStatus;

import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.actions.asset.AssetIdentifierView;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.viewhelpers.AssetCrudHelper;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.api.Listable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.assettype.AutoAttributeCriteriaByAssetTypeIdLoader;
import com.n4systems.services.asset.AssetSaveService;
import com.n4systems.util.DateHelper;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

public class RegisterAsset extends AbstractCrud{
	
	Logger logger = Logger.getLogger(RegisterAsset.class);

	private Asset linkedAsset;
	private Long linkedAssetId;
	private Asset parentAsset;
	private Asset newAsset;
	
	private AssetManager assetManager;
	private LegacyAsset legacyProductManager;
	private OrderManager orderManager;
	
	//Drop down lists
	private AssetTypeLister assetTypeLister;
	private List<Listable<Long>> employees;
	private List<AssetStatus> assetStatuses;
	private List<Listable<Long>> commentTemplates;
	private OwnerPicker ownerPicker;
	private AutoAttributeCriteria autoAttributeCriteria;

    //Form Inputs
	private AssetIdentifierView identifiers;
	private AssetView assetView;
	private AssetWebModel assetWebModel = new AssetWebModel(this);

	public RegisterAsset(PersistenceManager persistenceManager, AssetManager assetManager,
			OrderManager orderManager, LegacyAsset legacyProductManager) {
		super(persistenceManager);
		this.assetManager = assetManager;
		this.orderManager = orderManager;
		this.legacyProductManager = legacyProductManager;
	}

	@Override
	protected void initMemberFields() {
		assetView = new AssetView();
		identifiers = new AssetIdentifierView();
		linkedAsset = lookUpLinkedProduct(linkedAssetId);
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		assetView = new AssetView();
		identifiers = new AssetIdentifierView();
		linkedAsset = lookUpLinkedProduct(uniqueId);
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), assetView);
		if(linkedAsset != null) {
			identifiers.setSerialNumber(linkedAsset.getSerialNumber());
			identifiers.setRfidNumber(linkedAsset.getRfidNumber());
			// set the default asset id.
			getAssetTypes();
			Long productId = assetTypeLister.getAssetTypes().iterator().next().getId();
			setAssetTypeId(productId);
			setOwnerId(getSessionUser().getOwner().getId());
			assetView.setIdentified(DateHelper.getToday());
		}
	}
	
	private Asset lookUpLinkedProduct(Long uniqueId) {
		return getLoaderFactory().createSafetyNetworkAssetLoader().withAllFields().setAssetId(uniqueId).load();
	}

	@Override
	protected void postInit() {
		super.postInit();
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), assetView);
		overrideHelper(new AssetCrudHelper(getLoaderFactory()));
	}
	
	@SkipValidation
	public String doAdd(){
		return SUCCESS;
	}

	public String doSave(){
		
		AssetViewModeConverter converter = new AssetViewModeConverter(getLoaderFactory(), orderManager, getUser());
		
		Asset assetToSave = converter.viewToModel(assetView);
		assetToSave.setSerialNumber(identifiers.getSerialNumber());
		assetToSave.setCustomerRefNumber(identifiers.getReferenceNumber());
		assetToSave.setRfidNumber(identifiers.getRfidNumber());
		assetToSave.setLinkedAsset(linkedAsset);
		assetWebModel.fillInAsset(assetToSave);
		
		AssetSaveService saver = new AssetSaveService(legacyProductManager, fetchCurrentUser());
		saver.setAsset(assetToSave);
		newAsset = saver.create();
			
		logger.info("Registered : " + newAsset);
		return SUCCESS;
	}

	public Asset getLinkedAsset() {
		return linkedAsset;
	}
	
	public Asset getNewAsset() {
		return newAsset;
	}
	
	public AssetTypeLister getAssetTypes() {
		if (assetTypeLister == null) {
			assetTypeLister = new AssetTypeLister(persistenceManager, getSecurityFilter());
		}

		return assetTypeLister;
	}
	
	public Collection<AssetStatus> getAssetStatuses() {
		if (assetStatuses == null) {
			assetStatuses = getLoaderFactory().createAssetStatusListLoader().load();
		}
		return assetStatuses;
	}

	public Asset getParentAsset() {
		if (parentAsset == null) {
			parentAsset = assetManager.parentAsset(linkedAsset);
		}
		return parentAsset;
	}
	
	public List<Listable<Long>> getEmployees() {
		if (employees == null) {
			employees = getLoaderFactory().createCurrentEmployeesListableLoader().load();
			
		}
		return employees;
	}
			
	public AutoAttributeCriteria getAutoAttributeCriteria() {
		if (autoAttributeCriteria == null && assetView.getAssetTypeId() != null) {
			AutoAttributeCriteriaByAssetTypeIdLoader loader = getLoaderFactory().createAutoAttributeCriteriaByAssetTypeIdLoader();
			
			loader.setAssetTypeId(assetView.getAssetTypeId());
			
			autoAttributeCriteria = loader.load();
		}
		return autoAttributeCriteria;
	}
		
	public List<Listable<Long>> getCommentTemplates() {
		if (commentTemplates == null) {
			commentTemplates = getLoaderFactory().createCommentTemplateListableLoader().load();
		}
		return commentTemplates;
	}
	
	public Long getCommentTemplate() {
		return 0L;
	}
	
	/* Form setters and getters */
	
	public void setAssignedUser(Long userId) {
		assetView.setAssignedUser(userId);
		
	}
	
	public void setAssetStatus(Long statusId) {
		assetView.setAssetStatus(statusId);
	}
	
	public void setAssetTypeId(Long typeId) {
		assetView.setAssetTypeId(typeId);
	}
	
	@RequiredFieldValidator(type = ValidatorType.FIELD, message = "", key = "error.assettyperequired")
	public Long getAssetTypeId() {
		return assetView.getAssetTypeId();
	}
	
	public void setPurchaseOrder(String purchaseOrder) {
		assetView.setPurchaseOrder(purchaseOrder);
	}
	
	public void setIdentified(String identified) {
		assetView.setIdentified(convertDate(identified));
	}
	
	@RequiredStringValidator(message = "", key = "error.dateidentifiedrequired")
	@CustomValidator(type = "n4systemsDateValidator", message = "", key = "error.mustbeadate")
	public String getIdentified() {
		return convertDate(assetView.getIdentified());
	}
	
	public void setNonIntegrationOrderNumber(String orderNumber) {
		assetView.setNonIntegrationOrderNumber(orderNumber);
	}
	
	public void setComments(String comments) {
		assetView.setComments(comments);
	}
	
	public List<AssetExtensionValueInput> getAssetExtentionValues() {
		return assetView.getAssetExtentionValues();
	}
	
	@CustomValidator(type = "requiredInfoFields", message = "", key = "error.attributesrequired")
	public List<InfoOptionInput> getAssetInfoOptions() {
		return assetView.getAssetInfoOptions();
	}
	
	public void setAssetInfoOptions(List<InfoOptionInput> infoOptions) {
		assetView.setAssetInfoOptions(infoOptions);
	}
	
	public AssetIdentifierView getIdentifiers() {
		return identifiers;
	}

	@RequiredFieldValidator(message="", key="error.owner_required")
	public BaseOrg getOwner() {
		return ownerPicker.getOwner();
	}

	public Long getOwnerId() {
		return ownerPicker.getOwnerId();
	}

	public void setOwnerId(Long id) {
		ownerPicker.setOwnerId(id);
	}

	public void setSerialNumber(String serialNumber) {
		identifiers.setSerialNumber(serialNumber);
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "", key = "error.serialnumberrequired")
	@StringLengthFieldValidator(type = ValidatorType.FIELD, message = "", key = "error.serial_number_length", maxLength = "50")
	public String getSerialNumber() {
		return identifiers.getSerialNumber();
	}

	public void setRfidNumber(String rfidNumber) {
		identifiers.setRfidNumber(rfidNumber);
	}

	public String getRfidNumber() {
		return identifiers.getRfidNumber();
	}

	public void setReferenceNumber(String referenceNumber) {
		identifiers.setReferenceNumber(referenceNumber);
	}
	
	public String getReferenceNumber() {
		return identifiers.getReferenceNumber();
	}
	
	public AssetWebModel getAssetWebModel() {
		return assetWebModel;
	}	
	
	public void setLinkedAssetId(Long linkedAssetId) {
		this.linkedAssetId = linkedAssetId;
	}
}
