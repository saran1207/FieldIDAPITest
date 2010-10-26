package com.n4systems.fieldid.actions.asset;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.model.product.AssetCleaner;
import org.apache.log4j.Logger;

import rfid.ejb.entity.AssetSerialExtension;
import rfid.ejb.entity.AssetStatus;

import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.actions.helpers.MultiAddProductCrudHelper;
import com.n4systems.fieldid.actions.helpers.AssetExtensionValueInput;
import com.n4systems.fieldid.actions.helpers.AssetTypeLister;
import com.n4systems.fieldid.actions.helpers.UploadAttachmentSupport;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.Asset;
import com.n4systems.model.api.Listable;
import com.n4systems.model.api.Note;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.product.AssetAttachment;
import com.n4systems.model.producttype.AutoAttributeCriteriaByProductTypeIdLoader;
import com.n4systems.security.Permissions;
import com.n4systems.services.product.ProductSaveService;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.StringListingPair;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;

@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
public class MultiAddAssetCrud extends UploadAttachmentSupport {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(MultiAddAssetCrud.class);
	
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
	private List<AssetIdentifierView> identifiers = new ArrayList<AssetIdentifierView>();
	private AssetView assetView = new AssetView();
	
	
	private OwnerPicker ownerPicker;
	private String saveAndInspect;
	private Integer maxAssets;
	private List<Long> listOfIds = new ArrayList<Long>();
	
	private AssetWebModel assetWebModel = new AssetWebModel(this);
	
	public MultiAddAssetCrud(PersistenceManager persistenceManager, OrderManager orderManager, LegacyProductSerial legacyProductManager) {
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
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), assetView);
		setOwnerId(getSessionUserOwner().getId());
		overrideHelper(new MultiAddProductCrudHelper(getLoaderFactory()));
	}

	public String doForm() {
		if (getMaxAssets() == 0) {
			addActionMessageText("error.you_can_not_add_anymore_assets");
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String doCreate() {
		AssetCleaner cleaner = new AssetCleaner();
		
		logger.info("Asset Multi-Add saving " + identifiers.size() + " products");
		
		logger.info("Resolving fields on base asset");
		AssetViewModeConverter converter = new AssetViewModeConverter(getLoaderFactory(), orderManager, getUser());
		
		try {
			ProductSaveService saver = new ProductSaveService(legacyProductManager, fetchCurrentUser());
			int i = 1;
			for (AssetIdentifierView assetIdent : identifiers) {
				logger.info("Saving asset " + i + " of " + identifiers.size());
				
				Asset asset = converter.viewToModel(assetView);
				this.assetWebModel.fillInAsset(asset);
				
				asset.setSerialNumber(assetIdent.getSerialNumber());
				asset.setCustomerRefNumber(assetIdent.getReferenceNumber());
				asset.setRfidNumber(assetIdent.getRfidNumber());
				
				saver.setProduct(asset);
				saver.setUploadedAttachments(copyUploadedFiles());
				
				listOfIds.add(saver.create().getId());
				
				saver.clear();
			
				// make sure all persistence fields have been wiped
				cleaner.clean(asset);
				
				i++;
			}
			
			addFlashMessage(getText("message.assetscreated", new String[] {String.valueOf(identifiers.size())}));
			
		} catch (Exception e) {
			logger.error("Failed to create asset.", e);
			addActionErrorText("error.assetsave");
			return ERROR;
		}
	
		if(saveAndInspect!=null){
			return "saveinspect";
		}
		
		logger.info("Asset Multi-Add Complete");
		return SUCCESS;
	}
	
	
	
	private List<AssetAttachment> copyUploadedFiles() {
		List<AssetAttachment> copiedUploadedFiles = new ArrayList<AssetAttachment>();
		for (AssetAttachment uploadedFile : getUploadedFiles()) {
			AssetAttachment copiedUploadedFile = new AssetAttachment(new Note(uploadedFile.getNote()));
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
		if (autoAttributeCriteria == null && assetView.getAssetTypeId() != null) {
			AutoAttributeCriteriaByProductTypeIdLoader loader = getLoaderFactory().createAutoAttributeCriteriaByProductTypeIdLoader();
			
			loader.setAssetTypeId(assetView.getAssetTypeId());
			
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
		assetView.setAssignedUser(userId);
		
	}
	
	public void setAssetStatus(Long statusId) {
		assetView.setAssetStatus(statusId);
	}
	
	public void setAssetTypeId(Long typeId) {
		assetView.setAssetTypeId(typeId);
	}
	
	
	public void setPurchaseOrder(String purchaseOrder) {
		assetView.setPurchaseOrder(purchaseOrder);
	}
	
	public void setIdentified(String identified) {
		assetView.setIdentified(convertDate(identified));
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
	
	public List<InfoOptionInput> getAssetInfoOptions() {
		return assetView.getAssetInfoOptions();
	}
	
	public void setAssetInfoOptions(List<InfoOptionInput> infoOptions) {
		assetView.setAssetInfoOptions(infoOptions);
	}
	
	public List<AssetIdentifierView> getIdentifiers() {
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
