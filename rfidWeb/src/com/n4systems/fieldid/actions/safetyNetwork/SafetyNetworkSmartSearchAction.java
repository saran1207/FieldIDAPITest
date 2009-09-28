package com.n4systems.fieldid.actions.safetyNetwork;

import java.util.List;

import org.jboss.logging.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.Product;
import com.n4systems.model.safetynetwork.SafetyNetworkSmartSearchLoader;

public class SafetyNetworkSmartSearchAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(SafetyNetworkSmartSearchAction.class);
	
	private Long vendorId;
	private String searchText;
	private Product product;
	
	public SafetyNetworkSmartSearchAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	public String doFind() {
		SafetyNetworkSmartSearchLoader smartSearchLoader = getLoaderFactory().createSafetyNetworkSmartSearchLoader();
		smartSearchLoader.setVendorOrgId(vendorId);
		smartSearchLoader.setSearchText(searchText);
		
		try {
			List<Product> products = smartSearchLoader.load();
			
			// TODO: need to support multiple products
			if (!products.isEmpty()) {
				product = products.get(0);
				return "foundone";
			}
		} catch(RuntimeException e) {
			logger.error("Failed loading linked product", e);
		}
		
		return "notfound";
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
}
