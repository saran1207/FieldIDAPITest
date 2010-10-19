package com.n4systems.fieldid.actions.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import rfid.ejb.entity.ProductSerialExtensionBean;
import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.actions.helpers.MultiAddProductCrudHelper;
import com.n4systems.fieldid.actions.helpers.ProductExtensionValueInput;
import com.n4systems.fieldid.actions.helpers.ProductTypeLister;
import com.n4systems.fieldid.actions.helpers.UploadAttachmentSupport;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.Product;
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
	private List<ProductStatusBean> productStatuses;
	private List<Listable<Long>> commentTemplates;
	private List<ProductSerialExtensionBean> extentions;
	private ProductTypeLister productTypeLister;
	private AutoAttributeCriteria autoAttributeCriteria;
	
	// form inputs
	private List<ProductIdentifierView> identifiers = new ArrayList<ProductIdentifierView>();
	private ProductView productView = new ProductView();
	
	
	private OwnerPicker ownerPicker;
	private String saveAndInspect;
	private Integer maxProducts;
	private List<Long> listOfIds = new ArrayList<Long>();
	
	private AssetWebModel asset = new AssetWebModel(this);
	
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
		if (getMaxProducts() == 0) {
			addActionMessageText("error.you_can_not_add_anymore_products");
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String doCreate() {
		ProductCleaner cleaner = new ProductCleaner();
		
		logger.info("Product Multi-Add saving " + identifiers.size() + " products");
		
		logger.info("Resolving fields on base product");
		ProductViewModeConverter converter = new ProductViewModeConverter(getLoaderFactory(), orderManager, getUser());
		
		try {
			ProductSaveService saver = new ProductSaveService(legacyProductManager, fetchCurrentUser());
			int i = 1;
			for (ProductIdentifierView productIdent: identifiers) {
				logger.info("Saving product " + i + " of " + identifiers.size());
				
				Product product = converter.viewToModel(productView);
				asset.fillInAsset(product);
				
				product.setSerialNumber(productIdent.getSerialNumber());
				product.setCustomerRefNumber(productIdent.getReferenceNumber());
				product.setRfidNumber(productIdent.getRfidNumber());
				
				saver.setProduct(product);
				saver.setUploadedAttachments(copyUploadedFiles());
				
				listOfIds.add(saver.create().getId());
				
				saver.clear();
			
				// make sure all persistence fields have been wiped
				cleaner.clean(product);
				
				i++;
			}
			
			addFlashMessage(getText("message.productscreated", new String[] {String.valueOf(identifiers.size())}));
			
		} catch (Exception e) {
			logger.error("Failed to create product.", e);
			addActionErrorText("error.productsave");
			return ERROR;
		}
	
		if(saveAndInspect!=null){
			return "saveinspect";
		}
		
		logger.info("Product Multi-Add Complete");
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

	public List<ProductStatusBean> getProductStatuses() {
		if (productStatuses == null) {
			productStatuses = getLoaderFactory().createProductStatusListLoader().load();
		}
		return productStatuses;
	}

	public List<Listable<Long>> getCommentTemplates() {
		if (commentTemplates == null) {
			commentTemplates = getLoaderFactory().createCommentTemplateListableLoader().load();
		}
		return commentTemplates;
	}

	public List<ProductSerialExtensionBean> getExtentions() {
		if (extentions == null) {
			extentions = getLoaderFactory().createProductSerialExtensionListLoader().load();
		}
		return extentions;
	}
	
	public ProductTypeLister getProductTypes() {
		if (productTypeLister == null) {
			productTypeLister = new ProductTypeLister(persistenceManager, getSecurityFilter());
		}

		return productTypeLister;
	}
	
	public AutoAttributeCriteria getAutoAttributeCriteria() {
		if (autoAttributeCriteria == null && productView.getProductTypeId() != null) {
			AutoAttributeCriteriaByProductTypeIdLoader loader = getLoaderFactory().createAutoAttributeCriteriaByProductTypeIdLoader();
			
			loader.setProductTypeId(productView.getProductTypeId());
			
			autoAttributeCriteria = loader.load();
		}
		return autoAttributeCriteria;
	}
	
	public String getIdentified() {
		return convertDate(new Date());
	}
	
	public Integer getMaxProducts() {
		if (maxProducts == null) {
			if (getLimits().isAssetsMaxed()) {
				maxProducts = 0;
			} else {
				Integer configMax = getConfigContext().getInteger(ConfigEntry.MAX_MULTI_ADD_SIZE, getTenantId());
				Integer limitMax = getLimits().getAssetsMax().intValue() - getLimits().getAssetsUsed().intValue();

				maxProducts = (getLimits().isAssetsUnlimited() || configMax < limitMax) ? configMax : limitMax;
			}
		}

		return maxProducts;
	}
	
	/*************** Form input get/set's go below here **********************/
	

	public void setAssignedUser(Long userId) {
		productView.setAssignedUser(userId);
		
	}
	
	public void setProductStatus(Long statusId) {
		productView.setProductStatus(statusId);
	}
	
	public void setProductTypeId(Long typeId) {
		productView.setProductTypeId(typeId);
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

	public AssetWebModel getAsset() {
		return asset;
	}
	
}
