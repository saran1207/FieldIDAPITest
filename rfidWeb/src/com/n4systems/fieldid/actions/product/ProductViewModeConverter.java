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
import com.n4systems.model.Customer;
import com.n4systems.model.Division;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.JobSite;
import com.n4systems.model.LineItem;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.TenantOrganization;
import com.n4systems.model.productstatus.ProductStatusFilteredLoader;
import com.n4systems.model.user.UserFilteredLoader;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.persistence.loaders.LoaderFactory;

public class ProductViewModeConverter {
	private final LoaderFactory loaderFactory;
	private final OrderManager orderManager;
	private final UserBean identifier;
	
	public ProductViewModeConverter(LoaderFactory loaderFactory, OrderManager orderManager, UserBean identifier) {
		this.loaderFactory = loaderFactory;
		this.orderManager = orderManager;
		this.identifier = identifier;
	}

	public Product viewToModel(ProductView view) {
		Product model = new Product();

		model.setTenant(identifier.getTenant());
		model.setOrganization(identifier.getOrganization());
		model.setIdentifiedBy(identifier);	
		model.setOwner(resolveCustomer(view.getOwner()));
		model.setDivision(resolveDivision(view.getDivision()));
		model.setJobSite(resolveJobSite(view.getJobSite()));
		model.setType(resolveProductType(view.getProductTypeId()));
		model.setAssignedUser(resolveUser(view.getAssignedUser()));
		model.setProductStatus(resolveProductStatus(view.getProductStatus()));
		model.setShopOrder(createNonIntegrationOrder(view.getNonIntegrationOrderNumber(), identifier.getTenant()));
		model.setIdentified(view.getIdentified());
		model.setLocation(view.getLocation());
		model.setPurchaseOrder(view.getPurchaseOrder());
		model.setComments(view.getComments());
		
		List<InfoOptionBean> infoOptions = InfoOptionInput.convertInputInfoOptionsToInfoOptions(view.getProductInfoOptions(), model.getType().getInfoFields());
		model.setInfoOptions(new TreeSet<InfoOptionBean>(infoOptions));
		
		resolveExtensionValues(view.getProductExtentionValues(), model);
		
		return model;
	}
	
	private LineItem createNonIntegrationOrder(String orderNumber, TenantOrganization tenant) {
		LineItem line = null;
		// only do this if the order nuberm is not null and the tenant does not have Integration
		if (orderNumber != null && !tenant.hasExtendedFeature(ExtendedFeature.Integration)) {
			line = orderManager.createNonIntegrationShopOrder(orderNumber, tenant.getId());
		}
		return line;
	}
	
	private Customer resolveCustomer(Long customerId) {
		Customer customer = null;
		if (customerId != null) {
			FilteredIdLoader<Customer> loader = loaderFactory.createFilteredIdLoader(Customer.class).setId(customerId);
			customer = loader.load();
		}
		return customer;
	}
	
	private Division resolveDivision(Long divisionId) {
		Division division = null;
		if (divisionId != null) {
			FilteredIdLoader<Division> loader = loaderFactory.createFilteredIdLoader(Division.class).setId(divisionId);
			division = loader.load();
		}
		return division;
	}
	
	private JobSite resolveJobSite(Long jobSiteId) {
		JobSite jobSite = null;
		if (jobSiteId != null) {
			FilteredIdLoader<JobSite> loader = loaderFactory.createFilteredIdLoader(JobSite.class).setId(jobSiteId);
			jobSite = loader.load();
		}
		return jobSite;
	}
	
	private ProductType resolveProductType(Long productTypeId) {
		ProductType productType = null;
		if (productTypeId != null) {
			FilteredIdLoader<ProductType> loader = loaderFactory.createFilteredIdLoader(ProductType.class).setId(productTypeId);
			productType = loader.load();
		}
		return productType;
	}
	
	private UserBean resolveUser(Long userId) {
		UserBean user = null;
		if (userId != null) {
			UserFilteredLoader loader = loaderFactory.createUserFilteredLoader().setId(userId);
			user = loader.load();
		}
		return user;
	}
	
	private ProductStatusBean resolveProductStatus(Long statusId) {
		ProductStatusBean status = null;
		if (statusId != null) {
			ProductStatusFilteredLoader loader = loaderFactory.createProductStatusFilteredLoader().setId(statusId);
			status = loader.load();
		}
		return status;
	}
	
	private void resolveExtensionValues(List<ProductExtensionValueInput> productExtentionValues, Product product) {		
		if (productExtentionValues != null) {
			List<ProductSerialExtensionBean> extensions = loaderFactory.createProductSerialExtensionListLoader().load();
			
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
