package com.n4systems.fieldid.actions.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.n4systems.ejb.legacy.AssetCodeMappingService;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.util.AssetRemovalSummary;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.AddProductHistoryBean;
import rfid.ejb.entity.AssetCodeMapping;
import rfid.ejb.entity.AssetSerialExtension;
import rfid.ejb.entity.AssetSerialExtensionValue;
import rfid.ejb.entity.AssetStatus;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.ProjectManager;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.ejb.legacy.LegacyProductType;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.exceptions.UsedOnMasterInspectionException;
import com.n4systems.fieldid.actions.helpers.AllInspectionHelper;
import com.n4systems.fieldid.actions.helpers.InfoFieldInput;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.actions.helpers.ProductExtensionValueInput;
import com.n4systems.fieldid.actions.helpers.AssetTypeLister;
import com.n4systems.fieldid.actions.helpers.UploadAttachmentSupport;
import com.n4systems.fieldid.actions.product.helpers.ProductLinkedHelper;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.ui.OptionLists;
import com.n4systems.fieldid.viewhelpers.ProductCrudHelper;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.Inspection;
import com.n4systems.model.LineItem;
import com.n4systems.model.Order;
import com.n4systems.model.Project;
import com.n4systems.model.api.Listable;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.product.ProductAttachment;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;
import com.n4systems.services.product.ProductSaveService;
import com.n4systems.util.DateHelper;
import com.n4systems.util.StringListingPair;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SimpleListable;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

public class ProductCrud extends UploadAttachmentSupport {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(ProductCrud.class);

	// drop down lists
	private Collection<AssetStatus> productStatuses;
	private List<Listable<Long>> commentTemplates;
	private AssetType assetType;
	private Collection<AssetSerialExtension> extentions;
	private AutoAttributeCriteria autoAttributeCriteria;

	private List<Asset> assets;
	private List<Listable<Long>> employees;

	protected List<ProductAttachment> assetAttachments;

	// form inputs
	private List<InfoOptionInput> productInfoOptions;
	private List<ProductExtensionValueInput> productExtentionValues;
	protected Asset asset;
	private AddProductHistoryBean addProductHistory;
	private String search;
	private String identified;
	private LineItem lineItem;
	private OwnerPicker ownerPicker;
	
	private boolean refreshRegirstation = false;

	/**
	 * Set only when coming from searchOrder.action, when attached a customer
	 * order through editWithCustomerOrder()
	 */
	private Order customerOrder;

	/**
	 * Set only when coming from searchOrder.action, and used to recreate the
	 * URL going back to search
	 */
	private Long tagOptionId;

	// viewextras
	private AllInspectionHelper allInspectionHelper;
	private List<Project> projects;

	private Asset parentAsset;
	private boolean lookedUpParent = false;

	// save buttons.z
	private String save;
	private String saveAndInspect;
	private String saveAndPrint;
	private String saveAndSchedule;

	private AssetRemovalSummary removalSummary;

	// managers
	private LegacyProductType productTypeManager;
	private LegacyProductSerial legacyProductSerialManager;

	private AssetCodeMappingService assetCodeMappingServiceManager;
	protected InspectionScheduleManager inspectionScheduleManager;
	private ProductManager productManager;
	private OrderManager orderManager;
	private ProjectManager projectManager;
	private AssetTypeLister assetTypes;
	private ProductSaveService productSaverService;
	private Long excludeId;
	
	protected List<Asset> linkedAssets;
	protected Map<Long, List<ProductAttachment>> linkedAssetAttachments;
	
	protected AssetWebModel assetWebModel = new AssetWebModel(this);
	
	// XXX: this needs access to way to many managers to be healthy!!! AA
	public ProductCrud(LegacyProductType productTypeManager, LegacyProductSerial legacyProductSerialManager, PersistenceManager persistenceManager,
			AssetCodeMappingService assetCodeMappingServiceManager, ProductManager productManager, OrderManager orderManager,
			ProjectManager projectManager, InspectionScheduleManager inspectionScheduleManager) {
		super(persistenceManager);
		this.productTypeManager = productTypeManager;
		this.legacyProductSerialManager = legacyProductSerialManager;
		this.assetCodeMappingServiceManager = assetCodeMappingServiceManager;
		this.productManager = productManager;
		this.orderManager = orderManager;
		this.projectManager = projectManager;
		this.inspectionScheduleManager = inspectionScheduleManager;

	}

	@Override
	protected void initMemberFields() {
		asset = new Asset();
		asset.setPublished(getPrimaryOrg().isAutoPublish());
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		try {
			if (!isInVendorContext()) {
				asset = productManager.findAssetAllFields(uniqueId, getSecurityFilter());
			} else {
				asset = getLoaderFactory().createSafetyNetworkProductLoader().withAllFields().setProductId(uniqueId).load();
			}
			
		} catch(Exception e) {
			logger.error("Unable to load asset", e);
		}
		assetWebModel.match(asset);
		
	}
	
	@Override
	protected void postInit() {
		super.postInit();
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), asset);
		overrideHelper(new ProductCrudHelper(getLoaderFactory()));
	}
	

	private void loadAddProductHistory() {
		addProductHistory = legacyProductSerialManager.getAddProductHistory(getSessionUser().getUniqueID());

	}

	private void applyDefaults() {
		refreshRegirstation = true;
		
		asset.setIdentified(DateHelper.getToday());

		loadAddProductHistory();
		if (addProductHistory != null) {
			
			setOwnerId(addProductHistory.getOwner() != null ? addProductHistory.getOwner().getId() : null);

			// we need to make sure we load the producttype with its info fields
			setAssetTypeId(addProductHistory.getProductType().getId());

			asset.setAssetStatus(addProductHistory.getProductStatus());
			asset.setPurchaseOrder(addProductHistory.getPurchaseOrder());
			asset.setAdvancedLocation(addProductHistory.getLocation());
			asset.setInfoOptions(new TreeSet<InfoOptionBean>(addProductHistory.getInfoOptions()));

		} else {
			// set the default asset id.
			getAssetTypes();
			Long productId = assetTypes.getAssetTypes().iterator().next().getId();
			setAssetTypeId(productId);
			setOwnerId(getSessionUser().getOwner().getId());
		}
		
		assetWebModel.match(asset);

		
	}

	private void convertInputsToInfoOptions() {

		List<InfoOptionBean> options = InfoOptionInput.convertInputInfoOptionsToInfoOptions(productInfoOptions, asset.getType().getInfoFields());

		asset.setInfoOptions(new TreeSet<InfoOptionBean>(options));
	}

	// XXX - This logic has been duplicated in ProductViewModeConverter.
	// ProductCrud should be refactored to use that.
	private void convertInputsToExtensionValues() {
		if (productExtentionValues != null) {
			Set<AssetSerialExtensionValue> newExtensionValues = new TreeSet<AssetSerialExtensionValue>();
			for (ProductExtensionValueInput input : productExtentionValues) {
				if (input != null) { // some of the inputs can be null due to
					// the retired info fields.
					for (AssetSerialExtension extension : getExtentions()) {
						if (extension.getUniqueID().equals(input.getExtensionId())) {
							AssetSerialExtensionValue extensionValue = input.convertToExtensionValueBean(extension, asset);
							if (extensionValue != null) {
								newExtensionValues.add(extensionValue);
							}
						}
					}
				}
			}
			asset.setAssetSerialExtensionValues(newExtensionValues);
		}
	}

	@SkipValidation
	public String doList() {
		// if no search param came just show the form.
		if (search != null && search.length() > 0) {
			
			try {
				
				if (!isInVendorContext()) {
					assets = productManager.findProductByIdentifiers(getSecurityFilter(), search, assetType);
					
					// remove the asset given. ( this is for asset merging, you
					// don't want to merge the asset with itself.)
					if (excludeId != null) {
						Asset excludeAsset = new Asset();
						excludeAsset.setId(excludeId);
						assets.remove(excludeAsset);
					}
					
				} else {
					assets = getLoaderFactory().createSafetyNetworkSmartSearchLoader().setVendorOrgId(getVendorContext()).setSearchText(search).load();
				}

				// if there is only one forward. directly to the group view
				// screen.
				if (assets.size() == 1) {
					asset = assets.get(0);
					uniqueID = asset.getId();
					
					// if we're in a vendor context we go to the tracebility tab instead
					return (isInVendorContext()) ? "oneFoundVendorContext" : "oneFound";
				}
			} catch (Exception e) {
				logger.error("Failed to look up Products", e);
				addActionErrorText("error.failedtoload");
				return ERROR;
			}
		}

		return SUCCESS;
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
	public String doAddNoHistory() {
		asset.setIdentified(DateHelper.getToday());
		return SUCCESS;
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
	public String doAdd() {
		applyDefaults();
		return SUCCESS;
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
	public String doAddWithOrder() {

		if (lineItem == null || lineItem.getId() == null) {
			addActionErrorText("error.noorder");
			return MISSING;
		}

		// find the asset code mapping by asset code. This will return the
		// default asset code if one could not be found.
		AssetCodeMapping assetCodeMapping = assetCodeMappingServiceManager.getProductCodeByProductCodeAndTenant(lineItem.getAssetCode(), getTenantId());

		if (assetCodeMapping.getAssetInfo() != null) {
			setAssetTypeId(assetCodeMapping.getAssetInfo().getId());
		}
		if (asset.getType() == null) {
			applyDefaults();
		}

		if (assetCodeMapping.getInfoOptions() != null) {
			asset.setInfoOptions(new TreeSet<InfoOptionBean>(assetCodeMapping.getInfoOptions()));
		}

		asset.setCustomerRefNumber(assetCodeMapping.getCustomerRefNumber());
		asset.setShopOrder(lineItem);
		asset.setOwner(lineItem.getOrder().getOwner());
		asset.setPurchaseOrder(lineItem.getOrder().getPoNumber());

		return SUCCESS;
	}

	@SkipValidation
	public String doShow() {
		testExistingAsset();

		loadAttachments();
		return SUCCESS;
	}

	// TODO this shouldn't be in this class this is not really about the asset
	// - AA
	@SkipValidation
	public String doInspections() {
		setPageType("product", "inspections");
		testExistingAsset();

		return SUCCESS;
	}

	private void loadAttachments() {
		setAttachments(getLoaderFactory().createProductAttachmentListLoader().setProduct(asset).load());
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
	public String doEdit() {
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
	
	

	@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
	public String doCreate() {
		testAsset();

		

		try {
			
			prepareAssetToBeSaved();
			
			
			// we only set identified by on save
			asset.setIdentifiedBy(fetchCurrentUser());
			
			ProductSaveService saver = getProductSaveService();
			saver.setUploadedAttachments(getUploadedFiles());
			saver.setProduct(asset);

			asset = saver.create();

			uniqueID = asset.getId();
			addFlashMessageText("message.productcreated");

		} catch (Exception e) {
			addActionErrorText("error.productsave");
			logger.error("failed to save Asset", e);
			return INPUT;
		}

		if (saveAndInspect != null) {
			getSession().put("productSerialId", uniqueID);
			return "saveinspect";
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

	private void prepareAssetToBeSaved() {
		asset.setTenant(getTenant());
		asset.setIdentified(convertDate(identified));
		
		convertInputsToInfoOptions();
		convertInputsToExtensionValues();
		processOrderMasters();
		
		assetWebModel.fillInAsset(asset);
	}
	
	@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
	public String doUpdate() {
		testAsset();
		makeEmployeesIncludeCurrentAssignedUser();
		
		try {
			prepareAssetToBeSaved();
			
		
			// on edit, we need to know if the asset type has changed
			AssetType oldType = productTypeManager.findProductTypeForProduct(asset.getId());

			// if the new asset type is not equal to the old then the type
			// has changed
			if (!asset.getType().equals(oldType)) {
				inspectionScheduleManager.removeAllSchedulesFor(asset);
			}

			ProductSaveService saver = getProductSaveService();
			saver.setUploadedAttachments(getUploadedFiles());
			saver.setExistingAttachments(getAttachments());
			saver.setProduct(asset);

			asset = saver.update();

			addFlashMessageText("message.productupdated");

		} catch (Exception e) {
			addActionErrorText("error.productsave");
			logger.error("failed to save Asset", e);
			return INPUT;
		}

		if (saveAndInspect != null) {
			getSession().put("productSerialId", uniqueID);
			return "saveinspect";
		}

		return "saved";
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
	public String doConnectToShopOrder() {
		testExistingAsset();

		if (lineItem == null || lineItem.getId() == null) {
			addActionErrorText("error.noorder");
			return ERROR;
		}

		processOrderMasters();

		// update the asset
		try {
			ProductSaveService saver = getProductSaveService();
			saver.setProduct(asset).update();
			addFlashMessageText("message.productupdated");
		} catch (Exception e) {
			logger.error("Failed connecting shop order to asset", e);
			addActionErrorText("error.productsave");
			return ERROR;
		}

		return SUCCESS;
	}

	protected void testAsset() {
		if (asset == null) {
			addActionErrorText("error.noproduct");
			throw new MissingEntityException();
		}
	}

	protected void testExistingAsset() {
		testAsset();
		if (asset.isNew()) {
			addActionErrorText("error.noproduct");
			throw new MissingEntityException();
		}
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
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
			getProductSaveService().setProduct(asset).update();
			String updateMessage = getText("message.productupdated.customer", Arrays.asList(asset.getSerialNumber(), asset.getOwner().getName()));
			addFlashMessage(updateMessage);

		} catch (Exception e) {
			logger.error("Failed connecting customer order to asset", e);
			addActionErrorText("error.productsave");
			return ERROR;
		}

		return SUCCESS;
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
	public String doConfirmDelete() {
		testExistingAsset();
		try {
			removalSummary = productManager.testArchive(asset);
		} catch (Exception e) {
			return ERROR;
		}
		return SUCCESS;
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
	public String doDelete() {
		testExistingAsset();
		try {
			productManager.archive(asset, fetchCurrentUser());
			addFlashMessageText("message.productdeleted");
			return SUCCESS;
		} catch (UsedOnMasterInspectionException e) {
			addFlashErrorText("error.deleteusedonmasterinspection");
		} catch (Exception e) {
			logger.error("failed to archive an asset", e);
			addFlashErrorText("error.deleteproduct");
		}

		return INPUT;
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
	public String doProductTypeChange() {
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
	public List<InfoOptionInput> getProductInfoOptions() {
		if (productInfoOptions == null) {
			productInfoOptions = new ArrayList<InfoOptionInput>();
			if (asset.getType() != null) {
				List<InfoOptionBean> tmpOptions = new ArrayList<InfoOptionBean>();
				if (asset.getInfoOptions() != null) {
					tmpOptions.addAll(asset.getInfoOptions());
				}
				productInfoOptions = InfoOptionInput.convertInfoOptionsToInputInfoOptions(tmpOptions, getProductInfoFields());
			}
		}
		return productInfoOptions;
	}

	private void processOrderMasters() {
		if (lineItem != null) {
			asset.setShopOrder(lineItem);
		}

		if (customerOrder != null) {
			asset.setCustomerOrder(customerOrder);
		}
	}

	public void setProductInfoOptions(List<InfoOptionInput> productInfoOptions) {
		this.productInfoOptions = productInfoOptions;
	}

	public Collection<AssetStatus> getAssetStatuses() {
		if (productStatuses == null) {
			productStatuses = getLoaderFactory().createProductStatusListLoader().load();
		}
		return productStatuses;
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

	public void setRfidNumber(String rfidNumber) {
		asset.setRfidNumber(rfidNumber);
	}

	@RequiredStringValidator(message = "", key = "error.identifiedrequired")
	@CustomValidator(type = "n4systemsDateValidator", message = "", key = "error.mustbeadate")
	public void setIdentified(String identified) {
		this.identified = identified;
	}

	public String getIdentified() {
		if (identified == null) {
			return convertDate(asset.getIdentified());
		}

		return identified;
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

	public void setComments(String comments) {
		asset.setComments(comments);
	}

	public String getCustomerRefNumber() {
		return asset.getCustomerRefNumber();
	}

	public void setCustomerRefNumber(String customerRefNumber) {
		asset.setCustomerRefNumber(customerRefNumber);
	}

	

	public Long getAssetStatus() {
		return (asset.getAssetStatus() != null) ? asset.getAssetStatus().getUniqueID() : null;
	}

	public void setAssetStatus(Long assetStatusId) {
		AssetStatus assetStatus = null;
		if (assetStatusId != null) {
			assetStatus = legacyProductSerialManager.findAssetStatus(assetStatusId, getTenantId());
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

	@RequiredFieldValidator(type = ValidatorType.FIELD, message = "", key = "error.producttyperequired")
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
			query.addPostFetchPaths("infoFields", "inspectionTypes", "attachments", "subTypes");
			assetType = persistenceManager.find(query);
		}
		this.asset.setType(assetType);
	}

	public Collection<InfoFieldBean> getProductInfoFields() {
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

	public void setSaveAndInspect(String saveAndInspect) {
		this.saveAndInspect = saveAndInspect;
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

	public List<ProductExtensionValueInput> getProductExtentionValues() {
		if (productExtentionValues == null) {
			productExtentionValues = new ArrayList<ProductExtensionValueInput>();
			for (AssetSerialExtension extention : getExtentions()) {
				ProductExtensionValueInput input = null;
				if (asset.getAssetSerialExtensionValues() != null) {
					for (AssetSerialExtensionValue value : asset.getAssetSerialExtensionValues()) {
						if (extention.getUniqueID().equals(value.getAssetSerialExtension().getUniqueID())) {
							input = new ProductExtensionValueInput(value);
						}
					}
				}
				if (input == null) {
					input = new ProductExtensionValueInput();
					input.setExtensionId(extention.getUniqueID());
				}
				productExtentionValues.add(input);
			}
		}
		return productExtentionValues;
	}

	public void setProductExtentionValues(List<ProductExtensionValueInput> productExtentionValues) {
		this.productExtentionValues = productExtentionValues;
	}

	@SuppressWarnings("deprecation")
	public Collection<AssetSerialExtension> getExtentions() {
		if (extentions == null) {
			extentions = legacyProductSerialManager.getAssetSerialExtensions(getTenantId());
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

	public List<Asset> getAssets() {
		return assets;
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
			parentAsset = productManager.parentAsset(asset);
			lookedUpParent = true;
		}

		return parentAsset;
	}

	public List<Listable<Long>> getEmployees() {
		if (employees == null) {
			employees = getLoaderFactory().createCurrentEmployeesListableLoader().load();
			
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

	public int getIdentifiedProductCount(LineItem lineItem) {
		return orderManager.countProductsTagged(lineItem);
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
			if (asset.getShopOrder() != null) {
				return asset.getShopOrder().getOrder().getOrderNumber();
			}
		}

		return null;
	}

	public void setNonIntegrationOrderNumber(String nonIntegrationOrderNumber) {
		if (nonIntegrationOrderNumber != null) {
			String orderNumber = nonIntegrationOrderNumber.trim();
			// only do this for customers without integration
			if (!getSecurityGuard().isIntegrationEnabled()) {
				// if the asset doesn't have a shop order, we need to create
				// one
				if (asset.getShopOrder() == null) {
					asset.setShopOrder(orderManager.createNonIntegrationShopOrder(orderNumber, getTenantId()));
				} else {
					// otherwise we'll just change the order number
					Order order = asset.getShopOrder().getOrder();
					order.setOrderNumber(orderNumber);
					persistenceManager.update(order);
				}
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

	public AllInspectionHelper getAllInspectionHelper() {
		if (allInspectionHelper == null)
			allInspectionHelper = new AllInspectionHelper(legacyProductSerialManager, asset, getSecurityFilter());
		return allInspectionHelper;
	}

	public Long getInspectionCount() {
		return getAllInspectionHelper().getInspectionCount();
	}

	public List<Inspection> getInspections() {
		return getAllInspectionHelper().getInspections();
	}

	public Inspection getLastInspection() {
		return getAllInspectionHelper().getLastInspection();
	}

	public Long getExcludeId() {
		return excludeId;
	}

	public void setExcludeId(Long excludeId) {
		this.excludeId = excludeId;
	}

	public ProductSaveService getProductSaveService() {
		if (productSaverService == null) {
			productSaverService = new ProductSaveService(legacyProductSerialManager, fetchCurrentUser());
		}
		return productSaverService;
	}

	public List<ProductAttachment> getLinkedAssetAttachments(Long linkedAssetId) {
		return linkedAssetAttachments.get(linkedAssetId);
	}
	
	public List<ProductAttachment> getAssetAttachments() {
		if (assetAttachments == null) {
			assetAttachments = getLoaderFactory().createProductAttachmentListLoader().setProduct(asset).load();
		}
		return assetAttachments;
	}

	public Long getOwnerId() {
		return ownerPicker.getOwnerId();
	}

	public void setOwnerId(Long id) {
		ownerPicker.setOwnerId(id);
	}
	
	@RequiredFieldValidator(message="", key="error.owner_required")
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
			asset.setLinkedAsset(getLoaderFactory().createSafetyNetworkProductLoader().setProductId(id).load());
		}
	}
	
	
	
	public List<Asset> getLinkedAssets() {
		return linkedAssets;
	}
	
	public boolean isLinked() {
		return ProductLinkedHelper.isLinked(asset, getLoaderFactory());
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
	
	
}
