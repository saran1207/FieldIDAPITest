package com.n4systems.webservice.dto;

import java.util.List;

public abstract class AbstractInspectionServiceDTO extends AbstractBaseServiceDTO {
	
	private String comments;
	private long inspectionTypeId;
	private String productMobileGuid;
	private ProductServiceDTO product;
	private long productId;
	private long formVersion;
	private List<CriteriaResultServiceDTO> results;
	private List<InspectionInfoOptionServiceDTO> infoOptions;
	private List<ImageServiceDTO> images;
	
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public long getInspectionTypeId() {
		return inspectionTypeId;
	}
	public void setInspectionTypeId(long inspectionTypeId) {
		this.inspectionTypeId = inspectionTypeId;
	}
	public boolean productMobileGuidExists() {
		return !isNullGUID( productMobileGuid );
	}
	
	public String getProductMobileGuid() {
		return productMobileGuid;
	}
	public void setProductMobileGuid(String productMobileGuid) {
		this.productMobileGuid = productMobileGuid;
	}
	public ProductServiceDTO getProduct() {
		return product;
	}
	public void setProduct(ProductServiceDTO product) {
		this.product = product;
	}
	public List<CriteriaResultServiceDTO> getResults() {
		return results;
	}
	public void setResults(List<CriteriaResultServiceDTO> results) {
		this.results = results;
	}
	public List<InspectionInfoOptionServiceDTO> getInfoOptions() {
		return infoOptions;
	}
	public void setInfoOptions(List<InspectionInfoOptionServiceDTO> infoOptions) {
		this.infoOptions = infoOptions;
	}
	
	public boolean productIdExists() {
		return isValidServerId( productId );
	}
	
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	
	public List<ImageServiceDTO> getImages() {
		return images;
	}
	public void setImages(List<ImageServiceDTO> images) {
		this.images = images;
	}
	public long getFormVersion() {
    	return formVersion;
    }
	public void setFormVersion(long formVersion) {
    	this.formVersion = formVersion;
    }

}
