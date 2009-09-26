package com.n4systems.fieldid.actions.safetyNetwork;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.Product;

public class SafetyNetworkSmartSearchAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private Long vendorId;
	private String searchText;
	private Product product;
	
	public SafetyNetworkSmartSearchAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	public String doFind() {
		
		
		product = new Product();
		product.setId(12345L);
		product.setSerialNumber("00239284FF");
		product.setRfidNumber("A9FF2GA9FF2G3BC8D23BC8D2");
//		product.setOwner(); 
		
		
		return SUCCESS;
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
