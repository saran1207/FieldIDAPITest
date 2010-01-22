package com.n4systems.fieldid.selenium.datatypes;

import java.util.Map;
import java.util.TreeMap;

public class ProductCodeMapping {
	String productCode;
	String referenceNumber;
	String productType;
	Map<Attribute,String> productAttributes = new TreeMap<Attribute,String>();
	
	public ProductCodeMapping(String productCode, String referenceNumber, String productType, Map<Attribute,String> productAttributes) {
		this(productCode, productType, productAttributes);
		this.referenceNumber = referenceNumber;
	}

	public ProductCodeMapping(String productCode, String productType, Map<Attribute,String> productAttributes) {
		this.productCode = productCode;
		this.productType = productType;
		this.productAttributes = productAttributes;
	}

	public ProductCodeMapping() {
	}
	
	public String getProductCode() {
		return productCode;
	}
	
	public String getReferenceNumber() {
		return referenceNumber;
	}
	
	public String getProductType() {
		return productType;
	}
	
	public Map<Attribute,String> getProductAttributes() {
		return productAttributes;
	}
	
	public void setProductCode(String s) {
		this.productCode = s;
	}
	
	public void setReferenceNumber(String s) {
		this.referenceNumber = s;
	}
	
	public void setProductType(String s) {
		this.productType = s;
	}
	
	public void setProductAttributes(Map<Attribute,String> productAttributes) {
		this.productAttributes = productAttributes;
	}
	
	public void addProductAttribute(Attribute key, String value) {
		productAttributes.put(key, value);
	}
	
	public String getProductAttribute(String key) {
		return productAttributes.get(key);
	}
}
