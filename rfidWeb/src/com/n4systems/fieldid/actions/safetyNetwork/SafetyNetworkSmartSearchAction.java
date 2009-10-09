package com.n4systems.fieldid.actions.safetyNetwork;

import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.Product;
import com.n4systems.model.safetynetwork.SafetyNetworkSmartSearchLoader;
import com.n4systems.model.safetynetwork.VendorLinkedOrgLoader;

public class SafetyNetworkSmartSearchAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(SafetyNetworkSmartSearchAction.class);
	
	private Long vendorId;
	private String searchText;
	private Product product;
	private List<Product> products;
	
	public SafetyNetworkSmartSearchAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	public String doFind() {
		if (searchText == null || searchText.trim().length() == 0) {
			return "notfound";
		}
		
		SafetyNetworkSmartSearchLoader smartSearchLoader = setupLoader();
		
		try {
			loadProducts(smartSearchLoader);
			if (!products.isEmpty()) {
				if (products.size() == 1) {
					product = products.get(0);
					return "foundone";
				} else {
					return "foundMany";
				}
			}
			return "notfound";
			
		} catch(Exception e) {
			addActionErrorText("error.could_not_access_the_safety_network");
			logger.error("Failed loading linked product", e);
			return ERROR;
		}
	}

	private void loadProducts(SafetyNetworkSmartSearchLoader smartSearchLoader) {
		List<Product> pulblishedProducts = getAllPublishedProducts(smartSearchLoader);
		
		filterProductsToVisableToUser(pulblishedProducts);
		
	}

	private void filterProductsToVisableToUser(List<Product> pulblishedProducts) {
		products = new ArrayList<Product>();
		
		for (Product product : pulblishedProducts) {
			if (isProductVisable(product)) {
				products.add(product);
			}
		}
	}

	private boolean isProductVisable(Product product) {
		VendorLinkedOrgLoader vendorOrgLoader = getLoaderFactory().createVendorLinkedOrgLoader();

		try {
			vendorOrgLoader.setLinkedOrgId(product.getOwner().getId()).load();
			return true;
		} catch (SecurityException e) {
			return false;
		}
	}

	private List<Product> getAllPublishedProducts(SafetyNetworkSmartSearchLoader smartSearchLoader) {
		List<Product> pulblishedProducts = smartSearchLoader.load();
		return pulblishedProducts;
	}

	private SafetyNetworkSmartSearchLoader setupLoader() {
		SafetyNetworkSmartSearchLoader smartSearchLoader = getLoaderFactory().createSafetyNetworkSmartSearchLoader();
		smartSearchLoader.setVendorOrgId(vendorId);
		smartSearchLoader.setSearchText(searchText);
		return smartSearchLoader;
	}
	
	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	
	public Product getProduct() {
		return product;
	}

	public List<Product> getProducts() {
		return products;
	}
}
