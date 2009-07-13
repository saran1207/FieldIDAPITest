package com.n4systems.fieldid.datatypes;

public class ProductAttribute {

	String name = null;
	boolean required = false;
	ProductAttributeType p = new ProductAttributeType();
	
	public ProductAttribute() {
		super();
	}

	public ProductAttribute(String name) {
		this.name = name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setType(String type) {
		p.setType(type);
	}
	
	public String getType() {
		return p.getType();
	}
	
	public void setRequired(boolean required) {
		this.required = required;
	}

	public boolean getRequired() {
		return required;
	}
	
	public void setProductAttributeType(ProductAttributeType p) {
		this.p = p;
	}
	
	public ProductAttributeType getProductAttributeType() {
		return p;
	}
}
