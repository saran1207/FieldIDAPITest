package com.n4systems.fieldid.actions.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import rfid.ejb.entity.AssetSerialExtension;
import rfid.ejb.entity.AssetStatus;

import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.actions.helpers.MultiAddProductCrudHelper;
import com.n4systems.fieldid.actions.helpers.ProductExtensionValueInput;
import com.n4systems.fieldid.actions.helpers.AssetTypeLister;
import com.n4systems.fieldid.actions.helpers.UploadAttachmentSupport;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.Asset;
import com.n4systems.model.api.Listable;
import com.n4systems.model.api.Note;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.product.ProductAttachment;
import com.n4systems.model.product.ProductCleaner;
import com.n4systems.model.producttype.AutoAttributeCriteriaByProductTypeIdLoader;
import com.n4systems.security.Permissions;
import com.n4systems.services.product.ProductSaveService;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.StringListingPair;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;

@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
public class MultiAddProductCrud extends UploadAttachmentSupport {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(MultiAddProductCrud.class);
	
	private final LegacyProductSerial legacyProductManager;
	private final OrderManager orderManager;
	
	// drop down lists
	private List<Listable<Long>> employees;
	private List<AssetStatus> assetStatuses;
	private List<Listable<Long>> commentTemplates;
	private List<AssetSerialExtension> extentions;
	private AssetTypeLister assetTypeLister;
	private AutoAttributeCriteria autoAttributeCriteria;
	
	// form inputs
	private List<ProductIdentifierView> identifiers = new ArrayList<ProductIdentifierView>();
	private ProductView productView = new ProductView();
	
	
	private OwnerPicker ownerPicker;
	private String saveAndInspect;
	private Integer maxAssets;
	private List<Long> listOfIds = new ArrayList<Long>();
	
	private AssetWebModel assetWebModel = new AssetWebModel(this);
	
	public MultiAddProductCrud(PersistenceManager persistenceManager, OrderManager orderManager, LegacyProductSerial legacyProductManager) {
		super(persistenceManager);
		this.orderManager = orderManager;
		this.legacyProductManager = legacyProductManager;
	}
	
	@Override
	protected void initMemberFields() {
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
	}

	@Override
	protected void postInit() {
		super.postInit();
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), productView);
		setOwnerId(getSessionUserOwner().getId());
		overrideHelper(new MultiAddProductCrudHelper(getLoaderFactory()));
	}

	public String doForm() {
		if (getMaxAssets() == 0) {
			addActionMessageText("error.you_can_not_add_anymore_products");
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String doCreate() {
		ProductCleaner cleaner = new ProductCleaner();
		
		logger.info("Asset Multi-Add saving " + identifiers.size() + " products");
		
		logger.info("Resolving fields on base asset");
		ProductViewModeConverter converter = new ProductViewModeConverter(getLoaderFactory(), orderManager, getUser());
		
		try {
			ProductSaveService saver = new ProductSaveService(legacyProductManager, fetchCurrentUser());
			int i = 1;
			for (ProductIdentifierView productIdent: identifiers) {
				logger.info("Saving asset " + i + " of " + identifiers.size());
				
				Asset asset = converter.viewToModel(productView);
				this.assetWebModel.fillInAsset(asset);
				
				asset.setSerialNumber(productIdent.getSerialNumber());
				asset.setCustomerRefNumber(productIdent.getReferenceNumber());
				asset.setRfidNumber(productIdent.getRfidNumber());
				
				saver.setProduct(asset);
				saver.setUploadedAttachments(copyUploadedFiles());
				
				listOfIds.add(saver.create().getId());
				
				saver.clear();
			
				// make sure all persistence fields have been wiped
				cleaner.clean(asset);
				
				i++;
			}
			
			addFlashMessage(getText("message.productscreated", new String[] {String.valueOf(identifiers.size())}));
			
		} catch (Exception e) {
			logger.error("Failed to create asset.", e);
			addActionErrorText("error.productsave");
			return ERROR;
		}
	
		if(saveAndInspect!=null){
			return "saveinspect";
		}
		
		logger.info("Asset Multi-Add Complete");
		return SUCCESS;
	}
	
	
	
	private List<ProductAttachment> copyUploadedFiles() {
		List<ProductAttachment> copiedUploadedFiles = new ArrayList<ProductAttachment>();
		for (ProductAttachment uploadedFile : getUploadedFiles()) {
			ProductAttachment copiedUploadedFile = new ProductAttachment(new Note(uploadedFile.getNote()));
			copiedUploadedFiles.add(copiedUploadedFile);
		}
		
		return copiedUploadedFiles;
	}

	public List<Listable<Long>> getEmployees() {
		if (employees == null) {
			employees = getLoaderFactory().createCurrentEmployeesListableLoader().load();
		}
		return employees;
	}

	public List<AssetStatus> getAssetStatuses() {
		if (assetStatuses == null) {
			assetStatuses = getLoaderFactory().createProductStatusListLoader().load();
		}
		return assetStatuses;
	}

	public List<Listable<Long>> getCommentTemplates() {
		if (commentTemplates == null) {
			commentTemplates = getLoaderFactory().createCommentTemplateListableLoader().load();
		}
		return commentTemplates;
	}

	public List<AssetSerialExtension> getExtentions() {
		if (extentions == null) {
			extentions = getLoaderFactory().createProductSerialExtensionListLoader().load();
		}
		return extentions;
	}
	
	public AssetTypeLister getAssetTypes() {
		if (assetTypeLister == null) {
			assetTypeLister = new AssetTypeLister(persistenceManager, getSecurityFilter());
		}

		return assetTypeLister;
	}
	
	public AutoAttributeCriteria getAutoAttributeCriteria() {
		if (autoAttributeCriteria == null && productView.getAssetTypeId() != null) {
			AutoAttributeCriteriaByProductTypeIdLoader loader = getLoaderFactory().createAutoAttributeCriteriaByProductTypeIdLoader();
			
			loader.setAssetTypeId(productView.getAssetTypeId());
			
			autoAttributeCriteria = loader.load();
		}
		return autoAttributeCriteria;
	}
	
	public String getIdentified() {
		return convertDate(new Date());
	}
	
	public Integer getMaxAssets() {
		if (maxAssets == null) {
			if (getLimits().isAssetsMaxed()) {
				maxAssets = 0;
			} else {
				Integer configMax = getConfigContext().getInteger(ConfigEntry.MAX_MULTI_ADD_SIZE, getTenantId());
				Integer limitMax = getLimits().getAssetsMax().intValue() - getLimits().getAssetsUsed().intValue();

				maxAssets = (getLimits().isAssetsUnlimited() || configMax < limitMax) ? configMax : limitMax;
			}
		}

		return maxAssets;
	}
	
	/*************** Form input get/set's go below here **********************/
	

	public void setAssignedUser(Long userId) {
		productView.setAssignedUser(userId);
		
	}
	
	public void setAssetStatus(Long statusId) {
		productView.setAssetStatus(statusId);
	}
	
	public void setAssetTypeId(Long typeId) {
		productView.setAssetTypeId(typeId);
	}
	
	
	public void setPurchaseOrder(String purchaseOrder) {
		productView.setPurchaseOrder(purchaseOrder);
	}
	
	public void setIdentified(String identified) {
		productView.setIdentified(convertDate(identified));
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
	
	public List<InfoOptionInput> getProductInfoOptions() {
		return productView.getProductInfoOptions();
	}
	
	public void setProductInfoOptions(List<InfoOptionInput> infoOptions) {
		productView.setProductInfoOptions(infoOptions);
	}
	
	public List<ProductIdentifierView> getIdentifiers() {
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
	
	public List<StringListingPair> getPublishedStates() {
		return PublishedState.getPublishedStates(this);
	}

	public void setSaveAndInspect(String saveAndInspect) {
		this.saveAndInspect = saveAndInspect;
	}

	public String getSaveAndInspect() {
		return saveAndInspect;
	}

	public List<Long> getListOfIds() {
		return listOfIds;
	}

	public AssetWebModel getAssetWebModel() {
		return assetWebModel;
	}
	
}
