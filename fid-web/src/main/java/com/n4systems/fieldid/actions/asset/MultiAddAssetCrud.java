package com.n4systems.fieldid.actions.asset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import rfid.ejb.entity.AddAssetHistory;
import rfid.ejb.entity.AssetExtension;
import rfid.ejb.entity.AssetStatus;
import rfid.ejb.entity.InfoFieldBean;

import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.fieldid.actions.helpers.AssetTypeLister;
import com.n4systems.fieldid.actions.helpers.InfoFieldInput;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.actions.helpers.MultiAddAssetCrudHelper;
import com.n4systems.fieldid.actions.helpers.UploadAttachmentSupport;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.api.Listable;
import com.n4systems.model.api.Note;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.model.asset.AssetCleaner;
import com.n4systems.model.assettype.AssetTypeLoader;
import com.n4systems.model.assettype.AutoAttributeCriteriaByAssetTypeIdLoader;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.security.Permissions;
import com.n4systems.services.asset.AssetSaveService;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.DateHelper;
import com.n4systems.util.StringListingPair;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;

@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
public class MultiAddAssetCrud extends UploadAttachmentSupport {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(MultiAddAssetCrud.class);
	
	private final LegacyAsset legacyAssetManager;
	private final OrderManager orderManager;
	
	// drop down lists
	private List<Listable<Long>> employees;
	private List<AssetStatus> assetStatuses;
	private List<Listable<Long>> commentTemplates;
	private List<AssetExtension> extentions;
	private AssetTypeLister assetTypeLister;
	private AutoAttributeCriteria autoAttributeCriteria;
	
	// form inputs
	private List<AssetIdentifierView> identifiers = new ArrayList<AssetIdentifierView>();
	private AssetView assetView = new AssetView();
	
	private OwnerPicker ownerPicker;
	private String saveAndStartEvent;
	private Integer maxAssets;
	private List<Long> listOfIds = new ArrayList<Long>();
	
	private AssetWebModel assetWebModel = new AssetWebModel(this);
	
	public MultiAddAssetCrud(PersistenceManager persistenceManager, OrderManager orderManager, LegacyAsset legacyAssetManager) {
		super(persistenceManager);
		this.orderManager = orderManager;
		this.legacyAssetManager = legacyAssetManager;
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
		overrideHelper(new MultiAddAssetCrudHelper(getLoaderFactory()));
	}

	public String doForm() {
		if (getMaxAssets() == 0) {
			addActionMessageText("error.you_can_not_add_anymore_assets");
			return ERROR;
		}
		applyDefaults();
		return SUCCESS;
	}
	
	private void applyDefaults() {
		assetView.setIdentified(DateHelper.getToday());

		AddAssetHistory addAssetHistory = loadAddAssetHistory();
		if (addAssetHistory != null) {
			
			setOwnerId(addAssetHistory.getOwner() != null ? addAssetHistory.getOwner().getId() : null);

			// we need to make sure we load the assettype with its info fields
			setAssetTypeId(addAssetHistory.getAssetType().getId());

			assetView.setAssetStatus(addAssetHistory.getAssetStatus() != null ? addAssetHistory.getAssetStatus().getUniqueID(): null);
			assetView.setPurchaseOrder(addAssetHistory.getPurchaseOrder());
			assetWebModel.getLocation().setFreeformLocation(addAssetHistory.getLocation().getFreeformLocation());
			
			AssetType assetType = getAssetType(addAssetHistory.getAssetType().getId());
			
			List<InfoOptionInput> assetInfoOptions = InfoOptionInput.convertInfoOptionsToInputInfoOptions(addAssetHistory.getInfoOptions(), assetType.getInfoFields());
			assetView.setAssetInfoOptions(assetInfoOptions);			
			
		} else {
			// set the default asset id.
			getAssetTypes();
			Long assetTypeId = assetTypeLister.getAssetTypes().iterator().next().getId();
			setAssetTypeId(assetTypeId);
			setOwnerId(getSessionUser().getOwner().getId());
		}
	}
	
	private AssetType getAssetType(Long id) {
		return new AssetTypeLoader(getSecurityFilter()).setId(id).setStandardPostFetches().load();
	}
	
	private AddAssetHistory loadAddAssetHistory() {
		return legacyAssetManager.getAddAssetHistory(getSessionUser().getUniqueID());
	}
	
	public String doCreate() {
		AssetCleaner cleaner = new AssetCleaner();
		
		logger.info("Asset Multi-Add saving " + identifiers.size() + " assets");
		
		logger.info("Resolving fields on base asset");
		AssetViewModeConverter converter = new AssetViewModeConverter(getLoaderFactory(), orderManager, getUser());
		
		try {
			AssetSaveService saver = new AssetSaveService(legacyAssetManager, fetchCurrentUser());
			int i = 1;
			for (AssetIdentifierView assetIdent : identifiers) {
				logger.info("Saving asset " + i + " of " + identifiers.size());
				
				Asset asset = converter.viewToModel(assetView);
				this.assetWebModel.fillInAsset(asset);
				
				asset.setSerialNumber(assetIdent.getSerialNumber());
				asset.setCustomerRefNumber(assetIdent.getReferenceNumber());
				asset.setRfidNumber(assetIdent.getRfidNumber());
				
				saver.setAsset(asset);
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
	
		if(saveAndStartEvent !=null){
			return "savestartevent";
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
			assetStatuses = getLoaderFactory().createAssetStatusListLoader().load();
		}
		return assetStatuses;
	}

	public List<Listable<Long>> getCommentTemplates() {
		if (commentTemplates == null) {
			commentTemplates = getLoaderFactory().createCommentTemplateListableLoader().load();
		}
		return commentTemplates;
	}

	public List<AssetExtension> getExtentions() {
		if (extentions == null) {
			extentions = getLoaderFactory().createAssetExtensionListLoader().load();
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
			AutoAttributeCriteriaByAssetTypeIdLoader loader = getLoaderFactory().createAutoAttributeCriteriaByAssetTypeIdLoader();
			
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
	
	public Long getAssetStatus() {
		return assetView.getAssetStatus();
	}
	
	public void setAssetTypeId(Long typeId) {
		assetView.setAssetTypeId(typeId);
	}
	
	public Long getAssetTypeId() {
		return assetView.getAssetTypeId();
	}
		
	public void setPurchaseOrder(String purchaseOrder) {
		assetView.setPurchaseOrder(purchaseOrder);
	}
	
	public String getPurchaseOrder() {
		return assetView.getPurchaseOrder();
	}
	
	public void setIdentified(String identified) {
		assetView.setIdentified(convertDate(identified));
	}
	
	public void setNonIntegrationOrderNumber(String orderNumber) {
		assetView.setNonIntegrationOrderNumber(orderNumber);
	}
	
	public String getNonIntegrationOrderNumber() {
		return assetView.getNonIntegrationOrderNumber();
	}
	
	public void setComments(String comments) {
		assetView.setComments(comments);
	}
	
	public List<InfoOptionInput> getAssetInfoOptions() {
		return assetView.getAssetInfoOptions();
	}
	
	public Collection<InfoFieldBean> getAssetInfoFields() {
		if (getAssetTypeId() != null) {
				return getAssetType(getAssetTypeId()).getAvailableInfoFields();
		}
		return null;
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

	public void setSaveAndStartEvent(String saveAndStartEvent) {
		this.saveAndStartEvent = saveAndStartEvent;
	}

	public String getSaveAndStartEvent() {
		return saveAndStartEvent;
	}

	public List<Long> getListOfIds() {
		return listOfIds;
	}

	public AssetWebModel getAssetWebModel() {
		return assetWebModel;
	}
	

	public List<StringListingPair> getComboBoxInfoOptions(InfoFieldBean field, InfoOptionInput inputOption) {
		return InfoFieldInput.getComboBoxInfoOptions(field, inputOption);
	}
	
	
}
