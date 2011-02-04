package com.n4systems.webservice.dto;

public class SubProductMapServiceDTO extends AbstractBaseServiceDTO {

	private String name;
	private long subProductId;
	private long productId;
	private ProductServiceDTO newProduct;

	private String subAssetGuid;
	private String masterAssetGuid;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getSubProductId() {
		return subProductId;
	}

	public void setSubProductId(long subProductId) {
		this.subProductId = subProductId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public ProductServiceDTO getNewProduct() {
		return newProduct;
	}

	public void setNewProduct(ProductServiceDTO newProduct) {
		this.newProduct = newProduct;
	}

	public String getSubAssetGuid() {
		return subAssetGuid;
	}

	public void setSubAssetGuid(String subAssetGuid) {
		this.subAssetGuid = subAssetGuid;
	}

	public String getMasterAssetGuid() {
		return masterAssetGuid;
	}

	public void setMasterAssetGuid(String masterAssetGuid) {
		this.masterAssetGuid = masterAssetGuid;
	}
}
