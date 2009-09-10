package com.n4systems.webservice.dto;

public class ProductTypeScheduleServiceDTO extends AbstractBaseDTOWithOwner {

	private long frequency;
	private long inspectionTypeId;
	private long productTypeId;

	public ProductTypeScheduleServiceDTO() {}
	
	public long getFrequency() {
		return frequency;
	}
	
	public void setFrequency(long frequency) {
		this.frequency = frequency;
	}
	
	public long getInspectionTypeId() {
		return inspectionTypeId;
	}

	public void setInspectionTypeId(long inspectionTypeId) {
		this.inspectionTypeId = inspectionTypeId;
	}

	public long getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(long productTypeId) {
		this.productTypeId = productTypeId;
	}

	
}
