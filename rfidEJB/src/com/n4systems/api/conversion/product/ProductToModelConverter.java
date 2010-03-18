package com.n4systems.api.conversion.product;

import java.util.Date;
import java.util.TreeSet;

import rfid.ejb.entity.InfoOptionBean;
import rfid.ejb.entity.ProductStatusBean;
import rfid.ejb.entity.UserBean;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.ViewToModelConverter;
import com.n4systems.api.model.ProductView;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.LineItem;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.Tenant;
import com.n4systems.model.infooption.InfoOptionConversionException;
import com.n4systems.model.infooption.InfoOptionMapConverter;
import com.n4systems.model.orders.NonIntegrationOrderManager;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.OrgByNameLoader;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.productstatus.ProductStatusByNameLoader;
import com.n4systems.persistence.Transaction;

public class ProductToModelConverter implements ViewToModelConverter<Product, ProductView> {
	private final OrgByNameLoader orgLoader;
	private final NonIntegrationOrderManager nonIntegrationOrderManager;
	private final ProductStatusByNameLoader productStatusLoader;
	private final InfoOptionMapConverter optionConverter;
	
	private ProductType type;
	private UserBean identifiedBy;
	
	public ProductToModelConverter(OrgByNameLoader orgLoader, NonIntegrationOrderManager nonIntegrationOrderManager, ProductStatusByNameLoader productStatusLoader, InfoOptionMapConverter optionConverter) {
		this.orgLoader = orgLoader;
		this.nonIntegrationOrderManager = nonIntegrationOrderManager;
		this.productStatusLoader = productStatusLoader;
		this.optionConverter = optionConverter;
	}
	
	@Override
	public Product toModel(ProductView view, Transaction transaction) throws ConversionException {
		Product model = new Product();
		
		PrimaryOrg primaryOrg = identifiedBy.getOwner().getPrimaryOrg();
		
		if (view.getIdentified() != null) {
			model.setIdentified(view.getIdentified());
		} else {
			model.setIdentified(new Date());
		}
		
		model.setOwner(resolveOwner(view, transaction));
		model.setTenant(model.getOwner().getTenant());
		model.setIdentifiedBy(identifiedBy);
		model.setType(type);		
		model.setSerialNumber(view.getSerialNumber());
		model.setRfidNumber(view.getRfidNumber());
		model.setCustomerRefNumber(view.getCustomerRefNumber());
		model.setLocation(view.getLocation());
		model.setPurchaseOrder(view.getPurchaseOrder());
		model.setComments(view.getComments());		
		model.setProductStatus(resolveProductStatus(view.getStatus(), transaction));
		model.setInfoOptions(new TreeSet<InfoOptionBean>());
		model.setPublished(primaryOrg.isAutoPublish());
		
		// the order number field is ignored for integration customers
		if (!primaryOrg.hasExtendedFeature(ExtendedFeature.Integration)) {
			model.setShopOrder(createShopOrder(view.getShopOrder(), model.getOwner().getTenant(), transaction));
		}
		
		try {
			// this could throw an exception if a select box info option could not be resolved.  It
			// will not throw on missing but required info fields.  We let the validators take care of that part.
			model.getInfoOptions().addAll(optionConverter.convertProductAttributes(view.getAttributes(), type)); 
		} catch (InfoOptionConversionException e) {
			throw new ConversionException(e);
		}
		
		return model;
	}

	private LineItem createShopOrder(String orderNumber, Tenant tenant, Transaction transaction) {
		/*
		 * TODO: refactor to use the existing transaction once the legacy product manager gets off ejb
		 * 
		 * The problem here is that the ProductSaveService is running inside it's own transaction so if we use the 
		 * existing transaction here, the non integration order will not have been committed by time the product
		 * save service tries to look it up by id.
		 */
		return (orderNumber != null) ? nonIntegrationOrderManager.createAndSave(orderNumber, tenant) : null;
	}
	
	private ProductStatusBean resolveProductStatus(String productStatus, Transaction transaction) {
		return (productStatus != null) ? productStatusLoader.setName(productStatus).load(transaction) : null;
	}

	private BaseOrg resolveOwner(ProductView view, Transaction transaction) {
		orgLoader.setOrganizationName(view.getOrganization());
		orgLoader.setCustomerName(view.getCustomer());
		orgLoader.setDivision(view.getDivision());
		
		return orgLoader.load(transaction);
	}
	
	public void setType(ProductType type) {
		this.type = type;
	}

	public ProductType getType() {
		return type;
	}
	
	public void setIdentifiedBy(UserBean identifiedBy) {
		this.identifiedBy = identifiedBy;
	}
	
}
