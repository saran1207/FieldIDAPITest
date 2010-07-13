package com.n4systems.webservice.dto.limitedproductupdate;

import static com.n4systems.webservice.dto.MobileDTOHelper.*;

import com.n4systems.webservice.dto.LocationServiceDTO;
import com.n4systems.webservice.dto.RequestInformation;

public class LimitedProductUpdateRequest extends RequestInformation implements LocationServiceDTO {

	private ProductLookupInformation productLookupInformation = new ProductLookupInformation();
	private String location;
	private Long predefinedLocationId;
	private long modifiedById;

	public ProductLookupInformation getProductLookupInformation() {
		return productLookupInformation;
	}

	public void setProductLookupInformation(ProductLookupInformation productLookupInformation) {
		this.productLookupInformation = productLookupInformation;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getPredefinedLocationId() {
		return predefinedLocationId;
	}

	public void setPredefinedLocationId(Long predefinedLocationId) {
		this.predefinedLocationId = predefinedLocationId;
	}

	public long getModifiedById() {
		return modifiedById;
	}

	public void setModifiedById(long modifiedById) {
		this.modifiedById = modifiedById;
	}

	public boolean modifiedByIdExists() {
		return isValidServerId(modifiedById);
	}

}
