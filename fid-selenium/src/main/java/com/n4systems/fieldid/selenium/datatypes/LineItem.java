package com.n4systems.fieldid.selenium.datatypes;

public class LineItem {
	String productCode;
	String description;
	String quantity;
	String identifiedProducts;
	
	public LineItem(String productCode, String description, String quantity, String identifiedProducts) {
		this.productCode = productCode;
		this.description = description;
		this.quantity = quantity;
		this.identifiedProducts = identifiedProducts;
	}

	public String getProductCode() {
		return productCode;
	}
	
	public void setProductCode(String productCode) {
		this.productCode = productCode;
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
	public String getIdentifiedProducts() {
		return identifiedProducts;
	}
	public void setIdentifiedProducts(String identifiedProducts) {
		this.identifiedProducts = identifiedProducts;
	}
}
