package com.n4systems.webservice.dto.limitedproductupdate;

import com.n4systems.webservice.dto.LocationServiceDTO;
import com.n4systems.webservice.dto.RequestInformation;

import static com.n4systems.webservice.dto.MobileDTOHelper.isValidServerId;

public class UpdateProductByCustomerRequest extends RequestInformation implements LocationServiceDTO {
	
	private ProductLookupInformation productLookupInformation = new ProductLookupInformation();
	private long ownerId;
	private String customerRefNumber;
	private String purchaseOrder;
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

	public String getCustomerRefNumber() {
		return customerRefNumber;
	}

	public void setCustomerRefNumber(String customerRefNumber) {
		this.customerRefNumber = customerRefNumber;
	}

	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
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
