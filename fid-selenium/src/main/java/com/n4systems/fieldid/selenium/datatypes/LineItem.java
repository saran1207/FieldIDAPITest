package com.n4systems.fieldid.selenium.datatypes;

public class LineItem {
	String assetCode;
	String description;
	String quantity;
	String identifiedAssets;
	
	public LineItem(String assetCode, String description, String quantity, String identifiedAssets) {
		this.assetCode = assetCode;
		this.description = description;
		this.quantity = quantity;
		this.identifiedAssets = identifiedAssets;
	}

	public String getAssetCode() {
		return assetCode;
	}
	
	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getIdentifiedAssets() {
		return identifiedAssets;
	}
	public void setIdentifiedAssets(String identifiedAssets) {
		this.identifiedAssets = identifiedAssets;
	}
}
