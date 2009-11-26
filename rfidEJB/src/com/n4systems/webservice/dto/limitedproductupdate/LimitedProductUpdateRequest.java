package com.n4systems.webservice.dto.limitedproductupdate;

import com.n4systems.webservice.dto.RequestInformation;

public class LimitedProductUpdateRequest extends RequestInformation {
	
	private ProductLookupInformation productLookupInformation = new ProductLookupInformation();
	private String location;

	public ProductLookupInformation getProductLookupInformation() {
		return productLookupInformation;
	}
	public void setProductLookupInformation(
			ProductLookupInformation productLookupInformation) {
		this.productLookupInformation = productLookupInformation;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
}
