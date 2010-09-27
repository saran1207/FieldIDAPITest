package com.n4systems.fieldid.actions.safetyNetwork;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;
import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.actions.helpers.ProductTypeLister;
import com.n4systems.fieldid.actions.product.AssetWebModel;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.viewhelpers.ProductCrudHelper;
import com.n4systems.model.Order;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.api.Listable;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.QueryBuilder;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;

public class RegisterAsset extends AbstractCrud{
	
	Logger logger = Logger.getLogger(RegisterAsset.class);

	private Product linkedProduct;
	private Product product;
	private ProductTypeLister productTypes;
	private List<ProductStatusBean> productStatuses;
	private Product parentProduct;
	private boolean lookedUpParent;
	private List<Listable<Long>> employees;
	private OwnerPicker ownerPicker;
	private ProductType productType;
	private List<InfoOptionInput> productInfoOptions;
	private List<Listable<Long>> commentTemplates;

	private ProductManager productManager;
	private LegacyProductSerial legacyProductSerialManager;
	private OrderManager orderManager;
	
	private AssetWebModel asset = new AssetWebModel(this);
	
	public RegisterAsset(PersistenceManager persistenceManager, ProductManager productManager, OrderManager orderManager,
			LegacyProductSerial legacyProductSerialManager) {
		super(persistenceManager);
		this.productManager = productManager;
		this.orderManager = orderManager;
		this.legacyProductSerialManager = legacyProductSerialManager;
	}

	@Override
	protected void initMemberFields() {
		product = new Product();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		product = new Product();
		linkedProduct = getLoaderFactory().createSafetyNetworkProductLoader().withAllFields().setProductId(uniqueId).load();
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), product);
		if(linkedProduct != null) {
			product.setSerialNumber(linkedProduct.getSerialNumber());
			product.setRfidNumber(linkedProduct.getRfidNumber());
			// set the default product id.
			getProductTypes();
			Long productId = productTypes.getProductTypes().iterator().next().getId();
			setProductTypeId(productId);
			setOwnerId(getSessionUser().getOwner().getId());
		}
	}
	
	@Override
	protected void postInit() {
		super.postInit();
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), product);
		overrideHelper(new ProductCrudHelper(getLoaderFactory()));
	}
	
	@SkipValidation
	public String doAdd(){
		return SUCCESS;
	}

	public String doSave(){
		product.setLinkedProduct(linkedProduct);
		logger.info(product);
		return SUCCESS;
	}
	
	public Product getLinkedProduct() {
		return linkedProduct;
	}

	public Product getProduct() {
		return product;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
	
	public Long getOwnerId() {
		return ownerPicker.getOwnerId();
	}

	public void setOwnerId(Long id) {
		ownerPicker.setOwnerId(id);
	}
	
	//@RequiredFieldValidator(message="", key="error.owner_required")
	public BaseOrg getOwner() {
		return ownerPicker.getOwner();
	}
	
	public ProductTypeLister getProductTypes() {
		if (productTypes == null) {
			productTypes = new ProductTypeLister(persistenceManager, getSecurityFilter());
		}

		return productTypes;
	}
	
	public Collection<ProductStatusBean> getProductStatuses() {
		if (productStatuses == null) {
			productStatuses = getLoaderFactory().createProductStatusListLoader().load();
		}
		return productStatuses;
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
	
	//@CustomValidator(type = "requiredInfoFields", message = "", key = "error.attributesrequired")
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
	
	//@RequiredFieldValidator(type = ValidatorType.FIELD, message = "", key = "error.producttyperequired")
	public Long getProductTypeId() {
		return (product.getType() != null) ? product.getType().getId() : null;
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

	public List<Listable<Long>> getCommentTemplates() {
		if (commentTemplates == null) {
			commentTemplates = getLoaderFactory().createCommentTemplateListableLoader().load();
		}
		return commentTemplates;
	}
	
	public Long getCommentTemplate() {
		return 0L;
	}
	
	//@RequiredStringValidator(message = "", key = "error.identifiedrequired")
	@CustomValidator(type = "n4systemsDateValidator", message = "", key = "error.mustbeadate")
	public void setIdentified(String identified) {
		product.setIdentified(convertDate(identified));
	}

	public String getIdentified() {
		return convertDate(product.getIdentified());
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
	
	public String getSerialNumber() {
		return product.getSerialNumber();
	}

	public void setSerialNumber(String serialNumber) {
		product.setSerialNumber(serialNumber);
	}

	public String getRfidNumber() {
		return product.getRfidNumber();
	}

	public void setRfidNumber(String rfidNumber) {
		product.setRfidNumber(rfidNumber);
	}

	public String getCustomerRefNumber() {
		return product.getCustomerRefNumber();
	}

	public void setCustomerRefNumber(String customerRefNumber) {
		product.setCustomerRefNumber(customerRefNumber);
	}
	
	public void setAsset(AssetWebModel asset) {
		this.asset = asset;
	}

	public AssetWebModel getAsset() {
		return asset;
	}
	
	public String getComments() {
		return product.getComments();
	}

	public void setComments(String comments) {
		product.setComments(comments);
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
}
