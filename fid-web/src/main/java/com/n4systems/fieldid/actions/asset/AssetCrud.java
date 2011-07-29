package com.n4systems.fieldid.actions.asset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.AddAssetHistory;
import rfid.ejb.entity.AssetCodeMapping;
import rfid.ejb.entity.AssetExtension;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProjectManager;
import com.n4systems.ejb.legacy.AssetCodeMappingService;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.ejb.legacy.LegacyAssetType;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.exceptions.UsedOnMasterEventException;
import com.n4systems.fieldid.actions.asset.helpers.AssetLinkedHelper;
import com.n4systems.fieldid.actions.event.WebEventSchedule;
import com.n4systems.fieldid.actions.event.viewmodel.ScheduleToWebEventScheduleConverter;
import com.n4systems.fieldid.actions.event.viewmodel.WebEventScheduleToScheduleConverter;
import com.n4systems.fieldid.actions.helpers.AllEventHelper;
import com.n4systems.fieldid.actions.helpers.AssetTypeLister;
import com.n4systems.fieldid.actions.helpers.AssignedToUserGrouper;
import com.n4systems.fieldid.actions.helpers.InfoFieldInput;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.actions.helpers.UploadAttachmentSupport;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.ui.OptionLists;
import com.n4systems.fieldid.viewhelpers.AssetCrudHelper;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.model.LineItem;
import com.n4systems.model.Order;
import com.n4systems.model.Project;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.api.Listable;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.model.eventschedule.NextEventScheduleLoader;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;
import com.n4systems.services.asset.AssetSaveService;
import com.n4systems.tools.Pager;
import com.n4systems.util.AssetRemovalSummary;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.DateHelper;
import com.n4systems.util.StringListingPair;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SimpleListable;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

public class AssetCrud extends UploadAttachmentSupport {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(AssetCrud.class);

	// drop down lists
	private List<Listable<Long>> commentTemplates;
	private AssetType assetType;
	private Collection<AssetExtension> extentions;
	private AutoAttributeCriteria autoAttributeCriteria;

	private List<Listable<Long>> employees;
	protected List<AssetAttachment> assetAttachments;

	// form inputs
	private List<InfoOptionInput> assetInfoOptions;
	protected Asset asset;
	private AddAssetHistory addAssetHistory;
	private String search;
	private LineItem lineItem;
	private OwnerPicker ownerPicker;

	private AssignedToUserGrouper userGrouper;

	private boolean refreshRegirstation = false;
	private boolean useAjaxPagination = false;
	private boolean usePagination = true;
	/**
	 * Set only when coming from searchOrder.action, when attached a customer order through editWithCustomerOrder()
	 */
	private Order customerOrder;

	/**
	 * Set only when coming from searchOrder.action, and used to recreate the URL going back to search
	 */
	private Long tagOptionId;

	// viewextras
	private AllEventHelper allEventHelper;
	private List<Project> projects;

	private Asset parentAsset;
	private boolean lookedUpParent = false;

	// save buttons.z
	private String save;
	private String saveAndStartEvent;
	private String saveAndPrint;
	private String saveAndSchedule;

	private AssetRemovalSummary removalSummary;

	// managers
	private LegacyAssetType assetTypeManager;
	private LegacyAsset legacyAssetManager;

	private AssetCodeMappingService assetCodeMappingServiceManager;
	protected EventScheduleManager eventScheduleManager;
	private AssetManager assetManager;
	private OrderManager orderManager;
	private ProjectManager projectManager;
	private AssetTypeLister assetTypes;
	private AssetSaveService assetSaverService;
	private Long excludeId;

	protected List<Asset> linkedAssets;
	protected Map<Long, List<AssetAttachment>> linkedAssetAttachments;
	private List<WebEventSchedule> webEventSchedules;

	protected AssetWebModel assetWebModel = new AssetWebModel(this);

	private Pager<Asset> page;
	private List<Asset> assets;
	private boolean isEditing;
	
	private String sortColumn;
	private String sortDirection;

	// XXX: this needs access to way to many managers to be healthy!!! AA
	public AssetCrud(LegacyAssetType assetTypeManager, LegacyAsset legacyAssetManager, PersistenceManager persistenceManager, AssetCodeMappingService assetCodeMappingServiceManager,
			AssetManager assetManager, OrderManager orderManager, ProjectManager projectManager, EventScheduleManager eventScheduleManager) {
		super(persistenceManager);
		this.assetTypeManager = assetTypeManager;
		this.legacyAssetManager = legacyAssetManager;
		this.assetCodeMappingServiceManager = assetCodeMappingServiceManager;
		this.assetManager = assetManager;
		this.orderManager = orderManager;
		this.projectManager = projectManager;
		this.eventScheduleManager = eventScheduleManager;

	}

	@Override
	protected void initMemberFields() {
		asset = new Asset();
		isEditing = false;
		asset.setPublished(getPrimaryOrg().isAutoPublish());
		webEventSchedules = new ArrayList<WebEventSchedule>();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		try {
			if (!isInVendorContext()) {
				asset = assetManager.findAssetAllFields(uniqueId, getSecurityFilter());
			} else {
				asset = getLoaderFactory().createSafetyNetworkAssetLoader().withAllFields().setAssetId(uniqueId).load();
			}

		} catch (Exception e) {
			logger.error("Unable to load asset", e);
		}
		if (asset != null) {
            assetType = asset.getType();
			assetWebModel.match(asset);
			webEventSchedules = new ArrayList<WebEventSchedule>();
		}
	}

	@Override
	protected void postInit() {
		super.postInit();
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), asset);
		overrideHelper(new AssetCrudHelper(getLoaderFactory()));
	}

	private void loadAddAssetHistory() {
		addAssetHistory = legacyAssetManager.getAddAssetHistory(getSessionUser().getUniqueID());
	}

	private void applyDefaults() {
		refreshRegirstation = true;

		asset.setIdentified(DateHelper.getToday());

		loadAddAssetHistory();
		if (addAssetHistory != null) {

			setOwnerId(addAssetHistory.getOwner() != null ? addAssetHistory.getOwner().getId() : null);

			// we need to make sure we load the assettype with its info fields
			setAssetTypeId(addAssetHistory.getAssetType().getId());

			asset.setAssetStatus(addAssetHistory.getAssetStatus());
			asset.setPurchaseOrder(addAssetHistory.getPurchaseOrder());
			asset.setAdvancedLocation(addAssetHistory.getLocation());
			asset.setInfoOptions(new TreeSet<InfoOptionBean>(addAssetHistory.getInfoOptions()));

		} else {
			// set the default asset id.
			getAssetTypes();
			Long assetTypeId = assetTypes.getAssetTypes().iterator().next().getId();
			setAssetTypeId(assetTypeId);
			setOwnerId(getSessionUser().getOwner().getId());
		}

		assetWebModel.match(asset);		
		setAutoEventSchedules(getAutoEventSchedules());
	}

	private void convertInputsToInfoOptions() {
		List<InfoOptionBean> options = InfoOptionInput.convertInputInfoOptionsToInfoOptions(assetInfoOptions, asset.getType().getInfoFields(), getSessionUser());

		asset.setInfoOptions(new TreeSet<InfoOptionBean>(options));
	}

	@SkipValidation
	public String doList() {
		// if no search param came just show the form.
		if (search != null && search.length() > 0) {

			try {
				if (isUsePagination()) {

					retrievePagedAssets();

					// if there is only one forward directly to the group view screen.
					if (page.getTotalResults() == 1 && !page.getList().isEmpty()) {
						asset = page.getList().get(0);
						uniqueID = asset.getId();

						return "oneFound";
					}
					
				/*Keeping this branch for the time being as the sub-component and sub-event pages take
				 * a list of assets. Once they're rewritten we'll get rid of this. */
				} else {
					
					retrievedListedAssets();
					
					if (assets.size() == 1) {
						asset = assets.get(0);
						uniqueID = asset.getId();
					
						return "oneFound";
					}
				}
			} catch (Exception e) {
				logger.error("Failed to look up Assets", e);
				addActionErrorText("error.failedtoload");
				return ERROR;
			}
		}

		return SUCCESS;
	}

	private void retrievePagedAssets() {
		page = getLoaderFactory().createSmartSearchPagedLoader().setSearchText(getSearch()).setAssetType(getAssetTypeId()).setPage(getCurrentPage()).load();

		// remove the asset given. ( this is for asset merging, you
		// don't want to merge the asset with itself.)
		if (excludeId != null) {
			Asset excludeAsset = new Asset();
			excludeAsset.setId(excludeId);
			page.getList().remove(excludeAsset);
		}
	}

	private void retrievedListedAssets() {
		assets = assetManager.findAssetByIdentifiers(getSecurityFilter(), search, assetType);

		// remove the asset given. ( this is for asset merging, you
		// don't want to merge the asset with itself.)
		if (excludeId != null) {
			Asset excludeAsset = new Asset();
			excludeAsset.setId(excludeId);
			assets.remove(excludeAsset);
		}
	}

	public Pager<Asset> getPage() {
		return page;
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf = { Permissions.Tag })
	public String doAddNoHistory() {
		asset.setIdentified(DateHelper.getToday());
		return SUCCESS;
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf = { Permissions.Tag })
	public String doAdd() {
		applyDefaults();
		return SUCCESS;
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf = { Permissions.Tag })
	public String doAddWithOrder() {

		if (lineItem == null || lineItem.getId() == null) {
			addActionErrorText("error.noorder");
			return MISSING;
		}

		// find the asset code mapping by asset code. This will return the
		// default asset code if one could not be found.
		AssetCodeMapping assetCodeMapping = assetCodeMappingServiceManager.getAssetCodeByAssetCodeAndTenant(lineItem.getAssetCode(), getTenantId());

		if (assetCodeMapping.getAssetInfo() != null && !assetCodeMapping.getAssetInfo().getName().equals(ConfigEntry.DEFAULT_PRODUCT_TYPE_NAME.getDefaultValue())) {
			setAssetTypeId(assetCodeMapping.getAssetInfo().getId());

			if (assetCodeMapping.getInfoOptions() != null && !assetCodeMapping.getInfoOptions().isEmpty()) {
				asset.setInfoOptions(new TreeSet<InfoOptionBean>(assetCodeMapping.getInfoOptions()));
			}
		}
		if (asset.getType() == null) {
			applyDefaults();
		}

		asset.setCustomerRefNumber(assetCodeMapping.getCustomerRefNumber());
		asset.setShopOrder(lineItem);
		asset.setOwner(lineItem.getOrder().getOwner());
		asset.setPurchaseOrder(lineItem.getOrder().getPoNumber());

		return SUCCESS;
	}

	@SkipValidation
	public String doShow() {
		try {
			testExistingAsset();
		}catch (MissingEntityException e){
			addActionErrorText("error.noasset");
			return ERROR;
		}

		loadAttachments();
		return SUCCESS;
	}

	// TODO this shouldn't be in this class this is not really about the asset
	// - AA
	@SkipValidation
	public String doEvents() {
		setPageType("asset", "events");
		testExistingAsset();

		return SUCCESS;
	}

	private void loadAttachments() {
		setAttachments(getLoaderFactory().createAssetAttachmentListLoader().setAsset(asset).load());
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf = { Permissions.Tag })
	public String doEdit() {
		isEditing  = true;
		makeEmployeesIncludeCurrentAssignedUser();
		testExistingAsset();
		setAssetTypeId(asset.getType().getId());
		loadAttachments();
		return SUCCESS;
	}

	private void makeEmployeesIncludeCurrentAssignedUser() {
		Listable<Long> assignedUserListable = asset.getAssignedUser() != null ? new SimpleListable<Long>(asset.getAssignedUser()) : null;

		OptionLists.includeInList(getEmployees(), assignedUserListable);
	}

	@UserPermissionFilter(userRequiresOneOf = { Permissions.Tag })
	public String doCreate() {
		testAsset();

		try {

			prepareAssetToBeSaved();

			// we only set identified by on save
			asset.setIdentifiedBy(fetchCurrentUser());

			AssetSaveService saver = getAssetSaveService();
			saver.setUploadedAttachments(getUploadedFiles());
			saver.setAsset(asset);

			asset = saver.create();

			scheduleEvents(asset);
			
			uniqueID = asset.getId();
			addFlashMessageText("message.assetcreated", Long.toString(uniqueID), createLinkToAsset(uniqueID));

		} catch (Exception e) {
			addActionErrorText("error.assetsave");
			logger.error("failed to save Asset", e);
			return INPUT;
		}

		if (saveAndStartEvent != null) {
			return "savestartevent";
		}

		if (saveAndPrint != null) {
			// only forward to the print call if the asset type has save and
			// print.
			if (asset.getType().isHasManufactureCertificate()) {
				return "saveprint";
			} else {
				addFlashMessageText("message.nomanufacturercert");
			}
		}

		if (saveAndSchedule != null) {
			return "saveschedule";
		}

		if (lineItem != null) {
			return "savedWithOrder";
		}

		return "saved";
	}

	private String createLinkToAsset(Long uniqueID) {
		return "<a href='asset.action?uniqueID="+uniqueID+"'>" + getText("message.view.this.asset") + "</a>";
	}

	private void scheduleEvents(Asset asset) {
		WebEventScheduleToScheduleConverter converter = new WebEventScheduleToScheduleConverter(getLoaderFactory(), getSessionUser().createUserDateConverter());
		
		for (WebEventSchedule schedule: getNextSchedules()) {
			if(schedule != null) {
				EventSchedule eventSchedule = converter.convert(schedule, asset);
				eventScheduleManager.update( eventSchedule );
			}
		}
	}

	public List<WebEventSchedule> getNextSchedules() {
		return webEventSchedules;
	}
		
	public void setNextSchedules(List<WebEventSchedule> eventSchedules) {
		this.webEventSchedules = eventSchedules;
	}
	
	public void setAutoEventSchedules(List<EventSchedule> eventSchedules) {
		ScheduleToWebEventScheduleConverter converter = new ScheduleToWebEventScheduleConverter(getSessionUser().createUserDateConverter());
		for (EventSchedule schedule: eventSchedules) {
			webEventSchedules.add(converter.convert(schedule)); 
		}
	}
	
	public List<EventSchedule> getAutoEventSchedules() {
		return eventScheduleManager.getAutoEventSchedules(asset);
	}
	
	private void prepareAssetToBeSaved() {
		asset.setTenant(getTenant());

		convertInputsToInfoOptions();
		processOrderMasters();

		assetWebModel.fillInAsset(asset);
	}

	@UserPermissionFilter(userRequiresOneOf = { Permissions.Tag })
	public String doUpdate() {
		testAsset();
		makeEmployeesIncludeCurrentAssignedUser();

		try {
			prepareAssetToBeSaved();

			// on edit, we need to know if the asset type has changed
			AssetType oldType = assetTypeManager.findAssetTypeForAsset(asset.getId());

			// if the new asset type is not equal to the old then the type
			// has changed
			if (!asset.getType().equals(oldType)) {
				eventScheduleManager.removeAllSchedulesFor(asset);
			}

			AssetSaveService saver = getAssetSaveService();
			saver.setUploadedAttachments(getUploadedFiles());
			saver.setExistingAttachments(getAttachments());
			saver.setAsset(asset);

			asset = saver.update();

			addFlashMessageText("message.assetupdated");

		} catch (Exception e) {
			addActionErrorText("error.assetsave");
			logger.error("failed to save Asset", e);
			return INPUT;
		}

		if (saveAndStartEvent != null) {
			return "savestartevent";
		}

		return "saved";
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf = { Permissions.Tag })
	public String doConnectToShopOrder() {
		testExistingAsset();

		if (lineItem == null || lineItem.getId() == null) {
			addActionErrorText("error.noorder");
			return ERROR;
		}

		processOrderMasters();

		// update the asset
		try {
			AssetSaveService saver = getAssetSaveService();
			saver.setAsset(asset).update();
			addFlashMessageText("message.assetupdated");
		} catch (Exception e) {
			logger.error("Failed connecting shop order to asset", e);
			addActionErrorText("error.assetsave");
			return ERROR;
		}

		return SUCCESS;
	}

	protected void testAsset() {
		if (asset == null) {
			throw new MissingEntityException();
		}
	}

	protected void testExistingAsset() {
		testAsset();
		if (asset.isNew()) {
			throw new MissingEntityException();
		}
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf = { Permissions.Tag })
	public String doConnectToCustomerOrder() {
		testExistingAsset();

		if (customerOrder == null || customerOrder.getId() == null) {
			addActionErrorText("error.noorder");
			throw new MissingEntityException();
		}

		asset.setCustomerOrder(customerOrder);
		asset.setPurchaseOrder(customerOrder.getPoNumber());
		asset.setOwner(customerOrder.getOwner());

		processOrderMasters();

		// update the asset
		try {
			getAssetSaveService().setAsset(asset).update();
			String updateMessage = getText("message.assetupdated.customer", Arrays.asList(asset.getSerialNumber(), asset.getOwner().getName()));
			addFlashMessage(updateMessage);

		} catch (Exception e) {
			logger.error("Failed connecting customer order to asset", e);
			addActionErrorText("error.assetsave");
			return ERROR;
		}

		return SUCCESS;
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf = { Permissions.Tag })
	public String doConfirmDelete() {
		testExistingAsset();
		try {
			removalSummary = assetManager.testArchive(asset);
		} catch (Exception e) {
			return ERROR;
		}
		return SUCCESS;
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf = { Permissions.Tag })
	public String doDelete() {
		testExistingAsset();
		try {
			assetManager.archive(asset, fetchCurrentUser());
			addFlashMessageText("message.assetdeleted");
			return SUCCESS;
		} catch (UsedOnMasterEventException e) {
			addFlashErrorText("error.deleteusedonmasterevent");
		} catch (Exception e) {
			logger.error("failed to archive an asset", e);
			addFlashErrorText("error.deleteasset");
		}

		return INPUT;
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf = { Permissions.Tag })
	public String doAssetTypeChange() {
		webEventSchedules.clear();
		setAutoEventSchedules(getAutoEventSchedules());		
		return SUCCESS;
	}
		
	@SkipValidation
	public String doUpdateAutoSchedule() {
		webEventSchedules.clear();
		setAutoEventSchedules(getAutoEventSchedules());		
		return SUCCESS;
	}

	@SkipValidation
	public String doPrint() {
		return SUCCESS;
	}

	public List<StringListingPair> getPublishedStates() {
		return PublishedState.getPublishedStates(this);
	}

	@CustomValidator(type = "requiredInfoFields", message = "", key = "error.attributesrequired")
	public List<InfoOptionInput> getAssetInfoOptions() {
		if (assetInfoOptions == null) {
			assetInfoOptions = new ArrayList<InfoOptionInput>();
			if (asset.getType() != null) {
				List<InfoOptionBean> tmpOptions = new ArrayList<InfoOptionBean>();
				if (asset.getInfoOptions() != null) {
					tmpOptions.addAll(asset.getInfoOptions());
				}
				assetInfoOptions = InfoOptionInput.convertInfoOptionsToInputInfoOptions(tmpOptions, getAssetInfoFields(), getSessionUser());
			}
		}
		return assetInfoOptions;
	}

	private void processOrderMasters() {
		if (lineItem != null) {
			asset.setShopOrder(lineItem);
		}

		if (customerOrder != null) {
			asset.setCustomerOrder(customerOrder);
		}
	}

	public void setAssetInfoOptions(List<InfoOptionInput> assetInfoOptions) {
		this.assetInfoOptions = assetInfoOptions;
	}

	public Collection<AssetStatus> getAssetStatuses() {
		Collection<AssetStatus> assetStatuses = getLoaderFactory().createAssetStatusListLoader().load();
		
		if(isEditing && !assetStatuses.contains(asset.getAssetStatus())) {
			assetStatuses.add(asset.getAssetStatus());
		}
		
		return assetStatuses;
	}

	public Asset getAsset() {
		return asset;
	}

	public String getSerialNumber() {
		return asset.getSerialNumber();
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "", key = "error.serialnumberrequired")
	@StringLengthFieldValidator(type = ValidatorType.FIELD, message = "", key = "error.serial_number_length", maxLength = "50")
	public void setSerialNumber(String serialNumber) {
		asset.setSerialNumber(serialNumber);
	}

	public String getRfidNumber() {
		return asset.getRfidNumber();
	}

	@StringLengthFieldValidator(type = ValidatorType.FIELD, message = "", key = "error.rfid_number_length", maxLength = "50")
	public void setRfidNumber(String rfidNumber) {
		asset.setRfidNumber(rfidNumber);
	}	

	@RequiredStringValidator(message = "", key = "error.identifiedrequired")
	@CustomValidator(type = "n4systemsDateValidator", message = "", key = "error.mustbeadate")
	public void setIdentified(String identified) {
		asset.setIdentified(convertDate(identified));
	}

	public String getIdentified() {
		return convertDate(asset.getIdentified());
	}

	public String getPurchaseOrder() {
		return asset.getPurchaseOrder();
	}

	public void setPurchaseOrder(String purchaseOrder) {
		asset.setPurchaseOrder(purchaseOrder);
	}

	public String getComments() {
		return asset.getComments();
	}

	@StringLengthFieldValidator(type = ValidatorType.FIELD, message = "", key = "error.comments_length", maxLength = "3000")
	public void setComments(String comments) {
		asset.setComments(comments);
	}

	@StringLengthFieldValidator(type = ValidatorType.FIELD, message = "", key = "error.customer_ref_number_length", maxLength = "300")
	public String getCustomerRefNumber() {
		return asset.getCustomerRefNumber();
	}

	public void setCustomerRefNumber(String customerRefNumber) {
		asset.setCustomerRefNumber(customerRefNumber);
	}

	public Long getAssetStatus() {
		return (asset.getAssetStatus() != null) ? asset.getAssetStatus().getId() : null;
	}

	public void setAssetStatus(Long assetStatusId) {
		AssetStatus assetStatus = null;
		if (assetStatusId != null) {
			assetStatus = legacyAssetManager.findAssetStatus(assetStatusId, getTenantId());
		}
		this.asset.setAssetStatus(assetStatus);
	}

	public AutoAttributeCriteria getAutoAttributeCriteria() {
		if (autoAttributeCriteria == null) {
			if (assetType != null && assetType.getAutoAttributeCriteria() != null) {
				String[] fetches = { "inputs" };
				autoAttributeCriteria = persistenceManager.find(AutoAttributeCriteria.class, assetType.getAutoAttributeCriteria().getId(), getTenant(), fetches);
			}
		}
		return autoAttributeCriteria;
	}

	@RequiredFieldValidator(type = ValidatorType.FIELD, message = "", key = "error.assettyperequired")
	public Long getAssetTypeId() {
		return (asset.getType() != null) ? asset.getType().getId() : null;
	}

	public AssetType getAssetType() {
		return asset.getType();
	}

	public void setAssetTypeId(Long assetTypeId) {
		assetType = null;
		if (assetTypeId != null) {
			QueryBuilder<AssetType> query = new QueryBuilder<AssetType>(AssetType.class, new OpenSecurityFilter());
			query.addSimpleWhere("tenant", getTenant()).addSimpleWhere("id", assetTypeId).addSimpleWhere("state", EntityState.ACTIVE);
			query.addPostFetchPaths("infoFields", "eventTypes", "attachments", "subTypes");
			assetType = persistenceManager.find(query);
		}
		this.asset.setType(assetType);
	}

	public Collection<InfoFieldBean> getAssetInfoFields() {
		if (getAssetTypeId() != null) {
			if (asset.getId() == null) {
				return assetType.getAvailableInfoFields();
			} else {
				return assetType.getInfoFields();
			}
		}
		return null;
	}

	public void setSave(String save) {
		this.save = save;
	}

	public String getSave() {
		return save;
	}

	public void setSaveAndStartEvent(String saveAndStartEvent) {
		this.saveAndStartEvent = saveAndStartEvent;
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

	@SuppressWarnings("deprecation")
	public Collection<AssetExtension> getExtentions() {
		if (extentions == null) {
			extentions = legacyAssetManager.getAssetExtensions(getTenantId());
		}
		return extentions;
	}

	public void setSaveAndPrint(String saveAndPrint) {
		this.saveAndPrint = saveAndPrint;
	}

	public void setSaveAndSchedule(String saveAndSchedule) {
		this.saveAndSchedule = saveAndSchedule;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		if (search != null) {
			search = search.trim();
		}
		this.search = search;
	}

	public List<StringListingPair> getComboBoxInfoOptions(InfoFieldBean field, InfoOptionInput inputOption) {
		return InfoFieldInput.getComboBoxInfoOptions(field, inputOption);
	}

	public Long getTagOptionId() {
		return tagOptionId;
	}

	public void setTagOptionId(Long tagOptionId) {
		this.tagOptionId = tagOptionId;
	}

	public Asset getParentAsset() {
		if (!lookedUpParent && !asset.isNew()) {
			parentAsset = assetManager.parentAsset(asset);
			lookedUpParent = true;
		}

		return parentAsset;
	}

	public List<Listable<Long>> getEmployees() {
		if (employees == null) {
			employees = getLoaderFactory().createCombinedUserListableLoader().load();
		}
		return employees;
	}

	public Long getAssignedUser() {
		return (asset.getAssignedUser() != null) ? asset.getAssignedUser().getId() : null;
	}

	public void setAssignedUser(Long user) {
		if (user == null) {
			asset.setAssignedUser(null);
		} else if (asset.getAssignedUser() == null || !user.equals(asset.getAssignedUser().getId())) {
			asset.setAssignedUser(persistenceManager.find(User.class, user, getTenantId()));
		}
	}

	public Long getLineItemId() {
		return (lineItem != null) ? lineItem.getId() : null;
	}

	public void setLineItemId(Long lineItemId) {
		if (lineItem == null && lineItemId != null) {
			setLineItem(persistenceManager.find(LineItem.class, lineItemId));
		}
	}

	public void setLineItem(LineItem lineItem) {
		this.lineItem = lineItem;
	}

	public LineItem getLineItem() {
		return lineItem;
	}

	public int getIdentifiedAssetCount(LineItem lineItem) {
		return orderManager.countAssetsTagged(lineItem);
	}

	public Order getCustomerOrder() {
		return customerOrder;
	}

	public void setCustomerOrder(Order customerOrder) {
		this.customerOrder = customerOrder;
	}

	public Long getCustomerOrderId() {
		return (customerOrder != null) ? customerOrder.getId() : null;
	}

	public void setCustomerOrderId(Long customerOrderId) {
		if (customerOrder == null && customerOrderId != null) {
			setCustomerOrder(persistenceManager.find(Order.class, customerOrderId));
		}
	}

	public String getNonIntegrationOrderNumber() {
		if (!getSecurityGuard().isIntegrationEnabled()) {
			return asset.getNonIntergrationOrderNumber();
		}

		return null;
	}

	public void setNonIntegrationOrderNumber(String nonIntegrationOrderNumber) {
		if (nonIntegrationOrderNumber != null) {
			// only do this for customers without integration
			if (!getSecurityGuard().isIntegrationEnabled()) {
				asset.setNonIntergrationOrderNumber(nonIntegrationOrderNumber.trim());
			}
		}
	}

	public AssetRemovalSummary getRemovalSummary() {
		return removalSummary;
	}

	public List<Project> getProjects() {
		if (projects == null) {
			projects = projectManager.getProjectsForAsset(asset, getSecurityFilter());
		}
		return projects;
	}

	public AssetTypeLister getAssetTypes() {
		if (assetTypes == null) {
			assetTypes = new AssetTypeLister(persistenceManager, getSecurityFilter());
		}

		return assetTypes;
	}

	public AllEventHelper getAllEventHelper() {
		if (allEventHelper == null)
			allEventHelper = new AllEventHelper(legacyAssetManager, asset, getSecurityFilter());
		return allEventHelper;
	}

	public Long getEventCount() {
		return getAllEventHelper().getEventCount();
	}

	public Pager<Event> getPagedEvents() {
		return getAllEventHelper().getPaginatedEvents(getCurrentPage(), sortColumn, sortDirection!=null ? sortDirection.equals("asc") : true);
	}

	public List<EventType> getEventTypes() {
		return new ArrayList<EventType>(assetType.getEventTypes());
	}
	
	public Event getLastEvent() {
		return getAllEventHelper().getLastEvent();
	}
	
	public EventSchedule getNextEvent() {
		return new NextEventScheduleLoader().setAssetId(uniqueID).load();
	}

	public Long getExcludeId() {
		return excludeId;
	}

	public void setExcludeId(Long excludeId) {
		this.excludeId = excludeId;
	}

	public AssetSaveService getAssetSaveService() {
		if (assetSaverService == null) {
			assetSaverService = new AssetSaveService(legacyAssetManager, fetchCurrentUser());
		}
		return assetSaverService;
	}

	public List<AssetAttachment> getLinkedAssetAttachments(Long linkedAssetId) {
		return linkedAssetAttachments.get(linkedAssetId);
	}

	public List<AssetAttachment> getAssetAttachments() {
		if (assetAttachments == null) {
			assetAttachments = getLoaderFactory().createAssetAttachmentListLoader().setAsset(asset).load();
		}
		return assetAttachments;
	}

	public Long getOwnerId() {
		return ownerPicker.getOwnerId();
	}

	public void setOwnerId(Long ownerId) {
		ownerPicker.setOwnerId(ownerId);
	}

	@RequiredFieldValidator(message = "", key = "error.owner_required")
	public BaseOrg getOwner() {
		return ownerPicker.getOwner();
	}

	public String getPublishedState() {
		return PublishedState.resolvePublishedState(asset.isPublished()).name();
	}

	public void setPublishedState(String stateName) {
		asset.setPublished(PublishedState.valueOf(stateName).isPublished());
	}

	public String getPublishedStateLabel() {
		return PublishedState.resolvePublishedState(asset.isPublished()).getPastTenseLabel();
	}

	public Long getLinkedAsset() {
		return (asset.getLinkedAsset() != null) ? asset.getLinkedAsset().getId() : null;
	}

	public void setLinkedAsset(Long id) {
		if (id == null) {
			asset.setLinkedAsset(null);
		} else if (asset.getLinkedAsset() == null || !asset.getLinkedAsset().getId().equals(id)) {
			asset.setLinkedAsset(getLoaderFactory().createSafetyNetworkAssetLoader().setAssetId(id).load());
		}
	}

	public List<Asset> getLinkedAssets() {
		return linkedAssets;
	}

	public boolean isLinked() {
		return AssetLinkedHelper.isLinked(asset, getLoaderFactory());
	}

	public boolean isRefreshRegistration() {
		return refreshRegirstation;
	}

	public void setAssetWebModel(AssetWebModel assetWebModel) {
		this.assetWebModel = assetWebModel;
	}

	public AssetWebModel getAssetWebModel() {
		return assetWebModel;
	}

	public AssignedToUserGrouper getUserGrouper() {
		if (userGrouper == null) {
			userGrouper = new AssignedToUserGrouper(new TenantOnlySecurityFilter(getSecurityFilter()), getEmployees(), getSessionUser());
		}
		return userGrouper;
	}

	public boolean isUseAjaxPagination() {
		return useAjaxPagination;
	}

	public void setUseAjaxPagination(boolean useAjaxPagination) {
		this.useAjaxPagination = useAjaxPagination;
	}

	public List<Asset> getAssets() {
		return assets;
	}

	public void setUsePagination(boolean usePagination) {
		this.usePagination = usePagination;
	}

	public boolean isUsePagination() {
		return usePagination;
	}

	public List<Listable<Long>> getJobs() {
		return getLoaderFactory().createEventJobListableLoader().orderByName().load();
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public String getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}	
	
	public String convertTimestamp(String timestamp, boolean includeTime) {
		Date date = new Date(Long.parseLong(timestamp));
		if(includeTime)
			return convertDateTime(date);
		else
			return convertDate(date);
	}

    @Override
    public String getIdentifierLabel() {
        if (assetType != null && assetType.isIdentifierOverridden()) {
            return assetType.getIdentifierLabel();
        }
        return super.getIdentifierLabel();
    }

}
