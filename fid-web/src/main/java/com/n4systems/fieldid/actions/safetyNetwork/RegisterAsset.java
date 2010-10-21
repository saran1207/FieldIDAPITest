package com.n4systems.fieldid.actions.safetyNetwork;

import java.util.Collection;
import java.util.List;

import com.n4systems.fieldid.actions.helpers.AssetTypeLister;
import com.n4systems.model.Asset;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.AssetStatus;

import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.actions.helpers.ProductExtensionValueInput;
import com.n4systems.fieldid.actions.product.AssetWebModel;
import com.n4systems.fieldid.actions.product.ProductIdentifierView;
import com.n4systems.fieldid.actions.product.ProductView;
import com.n4systems.fieldid.actions.product.ProductViewModeConverter;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.viewhelpers.ProductCrudHelper;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.api.Listable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.producttype.AutoAttributeCriteriaByProductTypeIdLoader;
import com.n4systems.services.product.ProductSaveService;
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
	
	private ProductManager productManager;
	private LegacyProductSerial legacyProductManager;
	private OrderManager orderManager;
	
	//Drop down lists
	private AssetTypeLister assetTypeLister;
	private List<Listable<Long>> employees;
	private List<AssetStatus> assetStatuses;
	private List<Listable<Long>> commentTemplates;
	private OwnerPicker ownerPicker;
	private AutoAttributeCriteria autoAttributeCriteria;

    //Form Inputs
	private ProductIdentifierView identifiers;
	private ProductView productView;
	private AssetWebModel assetWebModel = new AssetWebModel(this);

	public RegisterAsset(PersistenceManager persistenceManager, ProductManager productManager,
			OrderManager orderManager, LegacyProductSerial legacyProductManager) {
		super(persistenceManager);
		this.productManager = productManager;
		this.orderManager = orderManager;
		this.legacyProductManager = legacyProductManager;
	}

	@Override
	protected void initMemberFields() {
		productView = new ProductView();
		identifiers = new ProductIdentifierView();
		linkedAsset = lookUpLinkedProduct(linkedAssetId);
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		productView = new ProductView();
		identifiers = new ProductIdentifierView();
		linkedAsset = lookUpLinkedProduct(uniqueId);
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), productView);
		if(linkedAsset != null) {
			identifiers.setSerialNumber(linkedAsset.getSerialNumber());
			identifiers.setRfidNumber(linkedAsset.getRfidNumber());
			// set the default asset id.
			getAssetTypes();
			Long productId = assetTypeLister.getAssetTypes().iterator().next().getId();
			setAssetTypeId(productId);
			setOwnerId(getSessionUser().getOwner().getId());
			productView.setIdentified(DateHelper.getToday());
		}
	}
	
	private Asset lookUpLinkedProduct(Long uniqueId) {
		return getLoaderFactory().createSafetyNetworkProductLoader().withAllFields().setProductId(uniqueId).load();
	}

	@Override
	protected void postInit() {
		super.postInit();
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), productView);
		overrideHelper(new ProductCrudHelper(getLoaderFactory()));
	}
	
	@SkipValidation
	public String doAdd(){
		return SUCCESS;
	}

	public String doSave(){
		
		ProductViewModeConverter converter = new ProductViewModeConverter(getLoaderFactory(), orderManager, getUser());
		
		Asset assetToSave = converter.viewToModel(productView);
		assetToSave.setSerialNumber(identifiers.getSerialNumber());
		assetToSave.setCustomerRefNumber(identifiers.getReferenceNumber());
		assetToSave.setRfidNumber(identifiers.getRfidNumber());
		assetToSave.setLinkedAsset(linkedAsset);
		assetWebModel.fillInAsset(assetToSave);
		
		ProductSaveService saver = new ProductSaveService(legacyProductManager, fetchCurrentUser());
		saver.setProduct(assetToSave);
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
			assetStatuses = getLoaderFactory().createProductStatusListLoader().load();
		}
		return assetStatuses;
	}

	public Asset getParentAsset() {
		if (parentAsset == null) {
			parentAsset = productManager.parentProduct(linkedAsset);
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
		if (autoAttributeCriteria == null && productView.getAssetTypeId() != null) {
			AutoAttributeCriteriaByProductTypeIdLoader loader = getLoaderFactory().createAutoAttributeCriteriaByProductTypeIdLoader();
			
			loader.setAssetTypeId(productView.getAssetTypeId());
			
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
		productView.setAssignedUser(userId);
		
	}
	
	public void setAssetStatus(Long statusId) {
		productView.setAssetStatus(statusId);
	}
	
	public void setAssetTypeId(Long typeId) {
		productView.setAssetTypeId(typeId);
	}
	
	@RequiredFieldValidator(type = ValidatorType.FIELD, message = "", key = "error.producttyperequired")
	public Long getAssetTypeId() {
		return productView.getAssetTypeId();
	}
	
	public void setPurchaseOrder(String purchaseOrder) {
		productView.setPurchaseOrder(purchaseOrder);
	}
	
	public void setIdentified(String identified) {
		productView.setIdentified(convertDate(identified));
	}
	
	@RequiredStringValidator(message = "", key = "error.dateidentifiedrequired")
	@CustomValidator(type = "n4systemsDateValidator", message = "", key = "error.mustbeadate")
	public String getIdentified() {
		return convertDate(productView.getIdentified());
	}
	
	public void setNonIntegrationOrderNumber(String orderNumber) {
		productView.setNonIntegrationOrderNumber(orderNumber);
	}
	
	public void setComments(String comments) {
		productView.setComments(comments);
	}
	
	public List<ProductExtensionValueInput> getProductExtentionValues() {
		return productView.getProductExtentionValues();
	}
	
	@CustomValidator(type = "requiredInfoFields", message = "", key = "error.attributesrequired")
	public List<InfoOptionInput> getProductInfoOptions() {
		return productView.getProductInfoOptions();
	}
	
	public void setProductInfoOptions(List<InfoOptionInput> infoOptions) {
		productView.setProductInfoOptions(infoOptions);
	}
	
	public ProductIdentifierView getIdentifiers() {
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
