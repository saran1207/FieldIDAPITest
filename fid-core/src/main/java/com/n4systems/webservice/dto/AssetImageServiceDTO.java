package com.n4systems.webservice.dto;

public class AssetImageServiceDTO {
	private String assetMobileGuid;
	private long modifiedById;
	private ImageServiceDTO image;
	
	public void setAssetMobileGuid(String assetMobileGuid) {
		this.assetMobileGuid = assetMobileGuid;
	}
	
	public String getAssetMobileGuid() {
		return assetMobileGuid;
	}
	
	public void setModifiedById(long modifiedById) {
		this.modifiedById = modifiedById;
	}

	public long getModifiedById() {
		return modifiedById;
	}
	
	public void setImage(ImageServiceDTO image) {
		this.image = image;
	}
	
	public ImageServiceDTO getImage() {
		return image;
	}	
}
