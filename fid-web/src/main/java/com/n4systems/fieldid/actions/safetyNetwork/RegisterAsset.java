package com.n4systems.fieldid.actions.safetyNetwork;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.actions.helpers.ProductExtensionValueInput;
import com.n4systems.fieldid.actions.helpers.ProductTypeLister;
import com.n4systems.fieldid.actions.product.AssetWebModel;
import com.n4systems.fieldid.actions.product.ProductIdentifierView;
import com.n4systems.fieldid.actions.product.ProductView;
import com.n4systems.fieldid.actions.product.ProductViewModeConverter;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.viewhelpers.ProductCrudHelper;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.Product;
import com.n4systems.model.api.Listable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.producttype.AutoAttributeCriteriaByProductTypeIdLoader;
import com.n4systems.services.product.ProductSaveService;
import com.n4systems.util.DateHelper;

public class RegisterAsset extends AbstractCrud{
	
	Logger logger = Logger.getLogger(RegisterAsset.class);

	private Product linkedProduct;
	private Long linkedProductId;
	
	//Drop down lists
	private ProductTypeLister productTypeLister;
	private List<Listable<Long>> employees;
	private List<ProductStatusBean> productStatuses;
	private List<Listable<Long>> commentTemplates;
	private OwnerPicker ownerPicker;
	private AutoAttributeCriteria autoAttributeCriteria;

	private Product parentProduct;

	private ProductManager productManager;
	
    //Form Inputs
	private ProductIdentifierView identifiers;
	private ProductView productView;
	private AssetWebModel asset = new AssetWebModel(this);

	private OrderManager orderManager;

	private LegacyProductSerial legacyProductManager;
	
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
		linkedProduct = lookUpLinkedProduct(linkedProductId);
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		productView = new ProductView();
		identifiers = new ProductIdentifierView();
		linkedProduct = lookUpLinkedProduct(uniqueId);
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), productView);
		if(linkedProduct != null) {
			identifiers.setSerialNumber(linkedProduct.getSerialNumber());
			identifiers.setRfidNumber(linkedProduct.getRfidNumber());
			// set the default product id.
			getProductTypes();
			Long productId = productTypeLister.getProductTypes().iterator().next().getId();
			setProductTypeId(productId);
			setOwnerId(getSessionUser().getOwner().getId());
		}
	}
	
	private Product lookUpLinkedProduct(Long uniqueId) {
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
		
		Product product = converter.viewToModel(productView);
		product.setSerialNumber(identifiers.getSerialNumber());
		product.setCustomerRefNumber(identifiers.getReferenceNumber());
		product.setRfidNumber(identifiers.getRfidNumber());
		product.setLinkedProduct(linkedProduct);
		asset.fillInAsset(product);
		
		ProductSaveService saver = new ProductSaveService(legacyProductManager, fetchCurrentUser());
		saver.setProduct(product);
		product = saver.create();
		
		logger.info("Registered : " + product);
		return SUCCESS;
	}

	public Product getLinkedProduct() {
		return linkedProduct;
	}
		
	public ProductTypeLister getProductTypes() {
		if (productTypeLister == null) {
			productTypeLister = new ProductTypeLister(persistenceManager, getSecurityFilter());
		}

		return productTypeLister;
	}
	
	public Collection<ProductStatusBean> getProductStatuses() {
		if (productStatuses == null) {
			productStatuses = getLoaderFactory().createProductStatusListLoader().load();
		}
		return productStatuses;
	}

	public Product getParentProduct() {
		if (parentProduct == null) {
			parentProduct = productManager.parentProduct(linkedProduct);
		}
		return parentProduct;
	}
	
	public List<Listable<Long>> getEmployees() {
		if (employees == null) {
			employees = getLoaderFactory().createCurrentEmployeesListableLoader().load();
			
		}
		return employees;
	}
			
	public AutoAttributeCriteria getAutoAttributeCriteria() {
		if (autoAttributeCriteria == null && productView.getProductTypeId() != null) {
			AutoAttributeCriteriaByProductTypeIdLoader loader = getLoaderFactory().createAutoAttributeCriteriaByProductTypeIdLoader();
			
			loader.setProductTypeId(productView.getProductTypeId());
			
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
	
	public void setProductStatus(Long statusId) {
		productView.setProductStatus(statusId);
	}
	
	public void setProductTypeId(Long typeId) {
		productView.setProductTypeId(typeId);
	}
	
	public Long getProductTypeId() {
		return productView.getProductTypeId();
	}
	
	public void setPurchaseOrder(String purchaseOrder) {
		productView.setPurchaseOrder(purchaseOrder);
	}
	
	public void setIdentified(String identified) {
		productView.setIdentified(convertDate(identified));
	}
	
	public String getIdentified() {
		if (productView.getIdentified() == null) {
			return convertDate(DateHelper.getToday());
		}
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
	
	public List<InfoOptionInput> getProductInfoOptions() {
		return productView.getProductInfoOptions();
	}
	
	public void setProductInfoOptions(List<InfoOptionInput> infoOptions) {
		productView.setProductInfoOptions(infoOptions);
	}
	
	public ProductIdentifierView getIdentifiers() {
		return identifiers;
	}

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
	
	public AssetWebModel getAsset() {
		return asset;
	}	
	
	public void setLinkedProductId(Long linkedProductId) {
		this.linkedProductId = linkedProductId;
	}
}
