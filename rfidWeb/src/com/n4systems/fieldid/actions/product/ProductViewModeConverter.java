package com.n4systems.fieldid.actions.product;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import rfid.ejb.entity.InfoOptionBean;
import rfid.ejb.entity.ProductSerialExtensionBean;
import rfid.ejb.entity.ProductSerialExtensionValueBean;
import rfid.ejb.entity.ProductStatusBean;
import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.OrderManager;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.actions.helpers.ProductExtensionValueInput;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.LineItem;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.productstatus.ProductStatusFilteredLoader;
import com.n4systems.model.user.UserFilteredLoader;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.services.TenantCache;

public class ProductViewModeConverter {
	private final LoaderFactory loaderFactory;
	private final OrderManager orderManager;
	private final UserBean identifier;
	private Transaction transaction;
	
	public ProductViewModeConverter(LoaderFactory loaderFactory, OrderManager orderManager, UserBean identifier) {
		this.loaderFactory = loaderFactory;
		this.orderManager = orderManager;
		this.identifier = identifier;
	}

	public Product viewToModel(ProductView view) {
		Product model = new Product();

		PrimaryOrg primaryOrg = TenantCache.getInstance().findPrimaryOrg(identifier.getTenant().getId());
		
		transaction = PersistenceManager.startTransaction();
		
		try {
			model.setTenant(identifier.getTenant());
			model.setIdentifiedBy(identifier);	
			model.setOwner(view.getOwner());
			model.setType(resolveProductType(view.getProductTypeId()));
			model.setAssignedUser(resolveUser(view.getAssignedUser()));
			model.setProductStatus(resolveProductStatus(view.getProductStatus()));
			model.setShopOrder(createNonIntegrationOrder(view.getNonIntegrationOrderNumber(), primaryOrg));
			model.setIdentified(view.getIdentified());
			model.setLocation(view.getLocation());
			model.setPurchaseOrder(view.getPurchaseOrder());
			model.setComments(view.getComments());
			
			List<InfoOptionBean> infoOptions = InfoOptionInput.convertInputInfoOptionsToInfoOptions(view.getProductInfoOptions(), model.getType().getInfoFields());
			model.setInfoOptions(new TreeSet<InfoOptionBean>(infoOptions));
			
			resolveExtensionValues(view.getProductExtentionValues(), model);
		} finally {
			transaction.commit();
		}
		
		return model;
	}
	
	private LineItem createNonIntegrationOrder(String orderNumber, PrimaryOrg primaryOrg) {
		LineItem line = null;
		// only do this if the order nuberm is not null and the tenant does not have Integration
		if (orderNumber != null && !primaryOrg.hasExtendedFeature(ExtendedFeature.Integration)) {
			line = orderManager.createNonIntegrationShopOrder(orderNumber, primaryOrg.getTenant().getId());
		}
		return line;
	}
	
		
	private ProductType resolveProductType(Long productTypeId) {
		ProductType productType = null;
		if (productTypeId != null) {
			FilteredIdLoader<ProductType> loader = loaderFactory.createFilteredIdLoader(ProductType.class).setId(productTypeId);
			productType = loader.load(transaction);
		}
		return productType;
	}
	
	private UserBean resolveUser(Long userId) {
		UserBean user = null;
		if (userId != null) {
			UserFilteredLoader loader = loaderFactory.createUserFilteredLoader().setId(userId);
			user = loader.load(transaction);
		}
		return user;
	}
	
	private ProductStatusBean resolveProductStatus(Long statusId) {
		ProductStatusBean status = null;
		if (statusId != null) {
			ProductStatusFilteredLoader loader = loaderFactory.createProductStatusFilteredLoader().setId(statusId);
			status = loader.load(transaction);
		}
		return status;
	}
	
	private void resolveExtensionValues(List<ProductExtensionValueInput> productExtentionValues, Product product) {		
		if (productExtentionValues != null) {
			List<ProductSerialExtensionBean> extensions = loaderFactory.createProductSerialExtensionListLoader().load(transaction);
			
			Set<ProductSerialExtensionValueBean> newExtensionValues = new TreeSet<ProductSerialExtensionValueBean>();
			for (ProductExtensionValueInput input : productExtentionValues) {
				if (input != null) { // some of the inputs can be null due to the retired info fields.
					for (ProductSerialExtensionBean extension : extensions) {
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
}
