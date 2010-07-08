package com.n4systems.fieldid.actions.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.AddProductHistoryBean;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;
import rfid.ejb.entity.ProductCodeMappingBean;
import rfid.ejb.entity.ProductSerialExtensionBean;
import rfid.ejb.entity.ProductSerialExtensionValueBean;
import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.ProjectManager;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.ejb.legacy.LegacyProductType;
import com.n4systems.ejb.legacy.ProductCodeMapping;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.exceptions.UsedOnMasterInspectionException;
import com.n4systems.fieldid.actions.helpers.AllInspectionHelper;
import com.n4systems.fieldid.actions.helpers.InfoFieldInput;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.actions.helpers.ProductExtensionValueInput;
import com.n4systems.fieldid.actions.helpers.ProductTypeLister;
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
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
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
import com.n4systems.util.ProductRemovalSummary;
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
	private Collection<ProductStatusBean> productStatuses;
	private List<Listable<Long>> commentTemplates;
	private ProductType productType;
	private Collection<ProductSerialExtensionBean> extentions;
	private AutoAttributeCriteria autoAttributeCriteria;

	private List<Product> products;
	private List<Listable<Long>> employees;

	private List<ProductAttachment> productAttachments;

	// form inputs
	private List<InfoOptionInput> productInfoOptions;
	private List<ProductExtensionValueInput> productExtentionValues;
	protected Product product;
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

	private Product parentProduct;
	private boolean lookedUpParent = false;

	// save buttons.z
	private String save;
	private String saveAndInspect;
	private String saveAndPrint;
	private String saveAndSchedule;

	private ProductRemovalSummary removalSummary;

	// managers
	private LegacyProductType productTypeManager;
	private LegacyProductSerial legacyProductSerialManager;

	private ProductCodeMapping productCodeMappingManager;
	private InspectionScheduleManager inspectionScheduleManager;
	private ProductManager productManager;
	private OrderManager orderManager;
	private ProjectManager projectManager;
	private ProductTypeLister productTypes;
	private ProductSaveService productSaverService;
	private Long excludeId;
	
	protected List<Product> linkedProducts;
	protected Map<Long, List<ProductAttachment>> linkedProductAttachments;
	
	private AssetWebModel asset = new AssetWebModel(this);
	
	// XXX: this needs access to way to many managers to be healthy!!! AA
	public ProductCrud(LegacyProductType productTypeManager, LegacyProductSerial legacyProductSerialManager, PersistenceManager persistenceManager,
			ProductCodeMapping productCodeMappingManager, ProductManager productManager, OrderManager orderManager,
			ProjectManager projectManager, InspectionScheduleManager inspectionScheduleManager) {
		super(persistenceManager);
		this.productTypeManager = productTypeManager;
		this.legacyProductSerialManager = legacyProductSerialManager;
		this.productCodeMappingManager = productCodeMappingManager;
		this.productManager = productManager;
		this.orderManager = orderManager;
		this.projectManager = projectManager;
		this.inspectionScheduleManager = inspectionScheduleManager;

	}

	@Override
	protected void initMemberFields() {
		product = new Product();
		product.setPublished(getPrimaryOrg().isAutoPublish());
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		try {
			if (!isInVendorContext()) {
				product = productManager.findProductAllFields(uniqueId, getSecurityFilter());
			} else {
				product = getLoaderFactory().createSafetyNetworkProductLoader().withAllFields().setProductId(uniqueId).load();
			}
			
		} catch(Exception e) {
			logger.error("Unable to load product", e);
		}
		asset.match(product);
		
	}
	
	@Override
	protected void postInit() {
		super.postInit();
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), product);
		overrideHelper(new ProductCrudHelper(getLoaderFactory()));
	}
	

	private void loadAddProductHistory() {
		addProductHistory = legacyProductSerialManager.getAddProductHistory(getSessionUser().getUniqueID());

	}

	private void applyDefaults() {
		refreshRegirstation = true;
		
		product.setIdentified(DateHelper.getToday());

		loadAddProductHistory();
		if (addProductHistory != null) {
			
			setOwnerId(addProductHistory.getOwner() != null ? addProductHistory.getOwner().getId() : null);

			// we need to make sure we load the producttype with its info fields
			setProductTypeId(addProductHistory.getProductType().getId());

			product.setProductStatus(addProductHistory.getProductStatus());
			product.setPurchaseOrder(addProductHistory.getPurchaseOrder());
			product.setLocation(addProductHistory.getLocation());
			product.setInfoOptions(new TreeSet<InfoOptionBean>(addProductHistory.getInfoOptions()));

		} else {
			// set the default product id.
			getProductTypes();
			Long productId = productTypes.getProductTypes().iterator().next().getId();
			setProductTypeId(productId);
			setOwnerId(getSessionUser().getOwner().getId());
		}
		
		asset.match(product);

		
	}

	private void convertInputsToInfoOptions() {

		List<InfoOptionBean> options = InfoOptionInput.convertInputInfoOptionsToInfoOptions(productInfoOptions, product.getType().getInfoFields());

		product.setInfoOptions(new TreeSet<InfoOptionBean>(options));
	}

	// XXX - This logic has been duplicated in ProductViewModeConverter.
	// ProductCrud should be refactored to use that.
	private void convertInputsToExtensionValues() {
		if (productExtentionValues != null) {
			Set<ProductSerialExtensionValueBean> newExtensionValues = new TreeSet<ProductSerialExtensionValueBean>();
			for (ProductExtensionValueInput input : productExtentionValues) {
				if (input != null) { // some of the inputs can be null due to
					// the retired info fields.
					for (ProductSerialExtensionBean extension : getExtentions()) {
						if (extension.getUniqueID().equals(input.getExtensionId())) {
							ProductSerialExtensionValueBean extensionValue = input.convertToExtensionValueBean(extension, product);
							if (extensionValue != null) {
								newExtensionValues.add(extensionValue);
							}
						}
					}
				}
			}
			product.setProductSerialExtensionValues(newExtensionValues);
		}
	}

	@SkipValidation
	public String doList() {
		// if no search param came just show the form.
		if (search != null && search.length() > 0) {
			
			try {
				
				if (!isInVendorContext()) {
					products = productManager.findProductByIdentifiers(getSecurityFilter(), search, productType);
					
					// remove the product given. ( this is for product merging, you
					// don't want to merge the product with itself.)
					if (excludeId != null) {
						Product excludeProduct = new Product();
						excludeProduct.setId(excludeId);
						products.remove(excludeProduct);
					}
					
				} else {
					products = getLoaderFactory().createSafetyNetworkSmartSearchLoader().setVendorOrgId(getVendorContext()).setSearchText(search).load();
				}

				// if there is only one forward. directly to the group view
				// screen.
				if (products.size() == 1) {
					product = products.get(0);
					uniqueID = product.getId();
					
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
		product.setIdentified(DateHelper.getToday());
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

		// find the product code mapping by product code. This will return the
		// default product code if one could not be found.
		ProductCodeMappingBean productCodeMapping = productCodeMappingManager.getProductCodeByProductCodeAndTenant(lineItem.getProductCode(), getTenantId());

		if (productCodeMapping.getProductInfo() != null) {
			setProductTypeId(productCodeMapping.getProductInfo().getId());
		}
		if (product.getType() == null) {
			applyDefaults();
		}

		if (productCodeMapping.getInfoOptions() != null) {
			product.setInfoOptions(new TreeSet<InfoOptionBean>(productCodeMapping.getInfoOptions()));
		}

		product.setCustomerRefNumber(productCodeMapping.getCustomerRefNumber());
		product.setShopOrder(lineItem);
		product.setOwner(lineItem.getOrder().getOwner());
		product.setPurchaseOrder(lineItem.getOrder().getPoNumber());

		return SUCCESS;
	}

	@SkipValidation
	public String doShow() {
		testExistingProduct();

		loadAttachments();
		return SUCCESS;
	}

	// TODO this shouldn't be in this class this is not really about the product
	// - AA
	@SkipValidation
	public String doInspections() {
		testExistingProduct();

		return SUCCESS;
	}

	private void loadAttachments() {
		setAttachments(getLoaderFactory().createProductAttachmentListLoader().setProduct(product).load());
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
	public String doEdit() {
		makeEmployeesIncludeCurrentAssignedUser();
		testExistingProduct();
		setProductTypeId(product.getType().getId());
		loadAttachments();
		return SUCCESS;
	}

	private void makeEmployeesIncludeCurrentAssignedUser() {
		Listable<Long> assignedUserListable = product.getAssignedUser() != null ? new SimpleListable<Long>(product.getAssignedUser()) : null;
		
		OptionLists.includeInList(getEmployees(), assignedUserListable);
	}
	
	

	@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
	public String doCreate() {
		testProduct();

		

		try {
			
			prepareProductToBeSaved();
			
			
			// we only set identified by on save
			product.setIdentifiedBy(fetchCurrentUser());
			
			ProductSaveService saver = getProductSaveService();
			saver.setUploadedAttachments(getUploadedFiles());
			saver.setProduct(product);

			product = saver.create();

			uniqueID = product.getId();
			addFlashMessageText("message.productcreated");

		} catch (Exception e) {
			addActionErrorText("error.productsave");
			logger.error("failed to save Product", e);
			return INPUT;
		}

		if (saveAndInspect != null) {
			getSession().put("productSerialId", uniqueID);
			return "saveinspect";
		}

		if (saveAndPrint != null) {
			// only forward to the print call if the product type has save and
			// print.
			if (product.getType().isHasManufactureCertificate()) {
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

	private void prepareProductToBeSaved() {
		product.setTenant(getTenant());
		product.setIdentified(convertDate(identified));
		
		convertInputsToInfoOptions();
		convertInputsToExtensionValues();
		processOrderMasters();
		
		asset.fillInAsset(product);
	}
	
	@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
	public String doUpdate() {
		testProduct();
		makeEmployeesIncludeCurrentAssignedUser();
		
		try {
			prepareProductToBeSaved();
			
		
			// on edit, we need to know if the product type has changed
			ProductType oldType = productTypeManager.findProductTypeForProduct(product.getId());

			// if the new product type is not equal to the old then the type
			// has changed
			if (!product.getType().equals(oldType)) {
				inspectionScheduleManager.removeAllSchedulesFor(product);
			}

			ProductSaveService saver = getProductSaveService();
			saver.setUploadedAttachments(getUploadedFiles());
			saver.setExistingAttachments(getAttachments());
			saver.setProduct(product);

			product = saver.update();

			addFlashMessageText("message.productupdated");

		} catch (Exception e) {
			addActionErrorText("error.productsave");
			logger.error("failed to save Product", e);
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
		testExistingProduct();

		if (lineItem == null || lineItem.getId() == null) {
			addActionErrorText("error.noorder");
			return ERROR;
		}

		processOrderMasters();

		// update the product
		try {
			ProductSaveService saver = getProductSaveService();
			saver.setProduct(product).update();
			addFlashMessageText("message.productupdated");
		} catch (Exception e) {
			logger.error("Failed connecting shop order to product", e);
			addActionErrorText("error.productsave");
			return ERROR;
		}

		return SUCCESS;
	}

	protected void testProduct() {
		if (product == null) {
			addActionErrorText("error.noproduct");
			throw new MissingEntityException();
		}
	}

	protected void testExistingProduct() {
		testProduct();
		if (product.isNew()) {
			addActionErrorText("error.noproduct");
			throw new MissingEntityException();
		}
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
	public String doConnectToCustomerOrder() {
		testExistingProduct();

		if (customerOrder == null || customerOrder.getId() == null) {
			addActionErrorText("error.noorder");
			throw new MissingEntityException();
		}

		product.setCustomerOrder(customerOrder);
		product.setPurchaseOrder(customerOrder.getPoNumber());
		product.setOwner(customerOrder.getOwner());

		processOrderMasters();

		// update the product
		try {
			getProductSaveService().setProduct(product).update();
			String updateMessage = getText("message.productupdated.customer", Arrays.asList(product.getSerialNumber(), product.getOwner().getName()));
			addFlashMessage(updateMessage);

		} catch (Exception e) {
			logger.error("Failed connecting customer order to product", e);
			addActionErrorText("error.productsave");
			return ERROR;
		}

		return SUCCESS;
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
	public String doConfirmDelete() {
		testExistingProduct();
		try {
			removalSummary = productManager.testArchive(product);
		} catch (Exception e) {
			return ERROR;
		}
		return SUCCESS;
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
	public String doDelete() {
		testExistingProduct();
		try {
			productManager.archive(product, fetchCurrentUser());
			addFlashMessageText("message.productdeleted");
			return SUCCESS;
		} catch (UsedOnMasterInspectionException e) {
			addFlashErrorText("error.deleteusedonmasterinspection");
		} catch (Exception e) {
			logger.error("failed to archive a product", e);
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
			if (product.getType() != null) {
				List<InfoOptionBean> tmpOptions = new ArrayList<InfoOptionBean>();
				if (product.getInfoOptions() != null) {
					tmpOptions.addAll(product.getInfoOptions());
				}
				productInfoOptions = InfoOptionInput.convertInfoOptionsToInputInfoOptions(tmpOptions, getProductInfoFields());
			}
		}
		return productInfoOptions;
	}

	private void processOrderMasters() {
		if (lineItem != null) {
			product.setShopOrder(lineItem);
		}

		if (customerOrder != null) {
			product.setCustomerOrder(customerOrder);
		}
	}

	public void setProductInfoOptions(List<InfoOptionInput> productInfoOptions) {
		this.productInfoOptions = productInfoOptions;
	}

	public Collection<ProductStatusBean> getProductStatuses() {
		if (productStatuses == null) {
			productStatuses = getLoaderFactory().createProductStatusListLoader().load();
		}
		return productStatuses;
	}


	public Product getProduct() {
		return product;
	}

	public String getSerialNumber() {
		return product.getSerialNumber();
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "", key = "error.serialnumberrequired")
	@StringLengthFieldValidator(type = ValidatorType.FIELD, message = "", key = "error.serial_number_length", maxLength = "50")
	public void setSerialNumber(String serialNumber) {
		product.setSerialNumber(serialNumber);
	}

	public String getRfidNumber() {
		return product.getRfidNumber();
	}

	public void setRfidNumber(String rfidNumber) {
		product.setRfidNumber(rfidNumber);
	}

	@RequiredStringValidator(message = "", key = "error.identifiedrequired")
	@CustomValidator(type = "n4systemsDateValidator", message = "", key = "error.mustbeadate")
	public void setIdentified(String identified) {
		this.identified = identified;
	}

	public String getIdentified() {
		if (identified == null) {
			return convertDate(product.getIdentified());
		}

		return identified;
	}

	public String getPurchaseOrder() {
		return product.getPurchaseOrder();
	}

	public void setPurchaseOrder(String purchaseOrder) {
		product.setPurchaseOrder(purchaseOrder);
	}

	public String getComments() {
		return product.getComments();
	}

	public void setComments(String comments) {
		product.setComments(comments);
	}

	public String getCustomerRefNumber() {
		return product.getCustomerRefNumber();
	}

	public void setCustomerRefNumber(String customerRefNumber) {
		product.setCustomerRefNumber(customerRefNumber);
	}

	

	public Long getProductStatus() {
		return (product.getProductStatus() != null) ? product.getProductStatus().getUniqueID() : null;
	}

	public void setProductStatus(Long productStatusId) {
		ProductStatusBean productStatus = null;
		if (productStatusId != null) {
			productStatus = legacyProductSerialManager.findProductStatus(productStatusId, getTenantId());
		}
		this.product.setProductStatus(productStatus);
	}

	public AutoAttributeCriteria getAutoAttributeCriteria() {
		if (autoAttributeCriteria == null) {
			if (productType != null && productType.getAutoAttributeCriteria() != null) {
				String[] fetches = { "inputs" };
				autoAttributeCriteria = persistenceManager.find(AutoAttributeCriteria.class, productType.getAutoAttributeCriteria().getId(), getTenant(), fetches);
			}
		}
		return autoAttributeCriteria;
	}

	@RequiredFieldValidator(type = ValidatorType.FIELD, message = "", key = "error.producttyperequired")
	public Long getProductTypeId() {
		return (product.getType() != null) ? product.getType().getId() : null;
	}

	public ProductType getProductType() {
		return product.getType();
	}

	public void setProductTypeId(Long productTypeId) {
		productType = null;
		if (productTypeId != null) {
			QueryBuilder<ProductType> query = new QueryBuilder<ProductType>(ProductType.class, new OpenSecurityFilter());
			query.addSimpleWhere("tenant", getTenant()).addSimpleWhere("id", productTypeId).addSimpleWhere("state", EntityState.ACTIVE);
			query.addPostFetchPaths("infoFields", "inspectionTypes", "attachments", "subTypes");
			productType = persistenceManager.find(query);
		}
		this.product.setType(productType);
	}

	public Collection<InfoFieldBean> getProductInfoFields() {
		if (getProductTypeId() != null) {
			if (product.getId() == null) {
				return productType.getAvailableInfoFields();
			} else {
				return productType.getInfoFields();
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
			for (ProductSerialExtensionBean extention : getExtentions()) {
				ProductExtensionValueInput input = null;
				if (product.getProductSerialExtensionValues() != null) {
					for (ProductSerialExtensionValueBean value : product.getProductSerialExtensionValues()) {
						if (extention.getUniqueID().equals(value.getProductSerialExtension().getUniqueID())) {
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
	public Collection<ProductSerialExtensionBean> getExtentions() {
		if (extentions == null) {
			extentions = legacyProductSerialManager.getProductSerialExtensions(getTenantId());
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

	public List<Product> getProducts() {
		return products;
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

	public Product getParentProduct() {
		if (!lookedUpParent && !product.isNew()) {
			parentProduct = productManager.parentProduct(product);
			lookedUpParent = true;
		}

		return parentProduct;
	}

	public List<Listable<Long>> getEmployees() {
		if (employees == null) {
			employees = getLoaderFactory().createCurrentEmployeesListableLoader().load();
			
		}
		return employees;
	}

	public Long getAssignedUser() {
		return (product.getAssignedUser() != null) ? product.getAssignedUser().getId() : null;
	}

	public void setAssignedUser(Long user) {
		if (user == null) {
			product.setAssignedUser(null);
		} else if (product.getAssignedUser() == null || !user.equals(product.getAssignedUser().getId())) {
			product.setAssignedUser(persistenceManager.find(User.class, user, getTenantId()));
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
			if (product.getShopOrder() != null) {
				return product.getShopOrder().getOrder().getOrderNumber();
			}
		}

		return null;
	}

	public void setNonIntegrationOrderNumber(String nonIntegrationOrderNumber) {
		if (nonIntegrationOrderNumber != null) {
			String orderNumber = nonIntegrationOrderNumber.trim();
			// only do this for customers without integration
			if (!getSecurityGuard().isIntegrationEnabled()) {
				// if the product doesn't have a shop order, we need to create
				// one
				if (product.getShopOrder() == null) {
					product.setShopOrder(orderManager.createNonIntegrationShopOrder(orderNumber, getTenantId()));
				} else {
					// otherwise we'll just change the order number
					Order order = product.getShopOrder().getOrder();
					order.setOrderNumber(orderNumber);
					persistenceManager.update(order);
				}
			}
		}
	}

	public ProductRemovalSummary getRemovalSummary() {
		return removalSummary;
	}

	public List<Project> getProjects() {
		if (projects == null) {
			projects = projectManager.getProjectsForAsset(product, getSecurityFilter());
		}
		return projects;
	}

	public ProductTypeLister getProductTypes() {
		if (productTypes == null) {
			productTypes = new ProductTypeLister(persistenceManager, getSecurityFilter());
		}

		return productTypes;
	}

	public AllInspectionHelper getAllInspectionHelper() {
		if (allInspectionHelper == null)
			allInspectionHelper = new AllInspectionHelper(legacyProductSerialManager, product, getSecurityFilter());
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

	public List<ProductAttachment> getLinkedProductAttachments(Long linkedProductId) {
		return linkedProductAttachments.get(linkedProductId);
	}
	
	public List<ProductAttachment> getProductAttachments() {
		if (productAttachments == null) {
			productAttachments = getLoaderFactory().createProductAttachmentListLoader().setProduct(product).load();
		}
		return productAttachments;
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
		return PublishedState.resolvePublishedState(product.isPublished()).name();
	}
	
	public void setPublishedState(String stateName) {
		product.setPublished(PublishedState.valueOf(stateName).isPublished());
	}
	
	public String getPublishedStateLabel() {
		return PublishedState.resolvePublishedState(product.isPublished()).getPastTenseLabel();
	}

	public Long getLinkedProduct() {
		return (product.getLinkedProduct() != null) ? product.getLinkedProduct().getId() : null;
	}

	public void setLinkedProduct(Long id) {
		if (id == null) {
			product.setLinkedProduct(null);
		} else if (product.getLinkedProduct() == null || !product.getLinkedProduct().getId().equals(id)) {
			product.setLinkedProduct(getLoaderFactory().createSafetyNetworkProductLoader().setProductId(id).load());
		}
	}
	
	
	
	public List<Product> getLinkedProducts() {
		return linkedProducts;
	}
	
	public boolean isLinked() {
		return ProductLinkedHelper.isLinked(product, getLoaderFactory());
	}

	public boolean isRefreshRegistration() {
		return refreshRegirstation;
	}


	public void setAsset(AssetWebModel asset) {
		this.asset = asset;
	}

	public AssetWebModel getAsset() {
		return asset;
	}
	
	
}
