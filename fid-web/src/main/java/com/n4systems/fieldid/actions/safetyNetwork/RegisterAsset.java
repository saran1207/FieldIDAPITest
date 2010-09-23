package com.n4systems.fieldid.actions.safetyNetwork;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;
import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.actions.helpers.ProductTypeLister;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.model.Order;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.api.Listable;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

public class RegisterAsset extends AbstractCrud{

	private Product linkedProduct;
	private Product product;
	private ProductTypeLister productTypes;
	private List<ProductStatusBean> productStatuses;
	private Product parentProduct;
	private boolean lookedUpParent;
	private ProductManager productManager;
	private List<Listable<Long>> employees;
	private OrderManager orderManager;
	private OwnerPicker ownerPicker;
	private ProductType productType;
	private List<InfoOptionInput> productInfoOptions;
	private List<Listable<Long>> commentTemplates;
	
	public RegisterAsset(PersistenceManager persistenceManager, ProductManager productManager, OrderManager orderManager) {
		super(persistenceManager);
		this.productManager = productManager;
		this.orderManager = orderManager;
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
	
	@SkipValidation
	public String doAdd(){
		return SUCCESS;
	}

	public String doSave(){
		return SUCCESS;
	}
	
	public Product getLinkedProduct() {
		return linkedProduct;
	}

	public Product getProduct() {
		return product;
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
	
	@RequiredFieldValidator(type = ValidatorType.FIELD, message = "", key = "error.producttyperequired")
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
}
