package com.n4systems.fieldid.actions.helpers;

import com.n4systems.model.Product;

import rfid.ejb.entity.ProductSerialExtensionBean;
import rfid.ejb.entity.ProductSerialExtensionValueBean;

public class ProductExtensionValueInput {
	
	private Long uniqueID;
	
	
	private Long extensionId;
	
	private String value;
	
	public ProductExtensionValueInput() {
		super();
	}
	
	public ProductExtensionValueInput( ProductSerialExtensionValueBean value ) {
		super();
	
		this.extensionId = value.getProductSerialExtension().getUniqueID();
		this.value = value.getExtensionValue();
		this.uniqueID = value.getUniqueID();
	}
	
	
	public Long getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(Long uniqueID) {
		this.uniqueID = uniqueID;
	}

	public Long getExtensionId() {
		return extensionId;
	}

	public void setExtensionId(Long extentionId) {
		this.extensionId = extentionId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public boolean isBlank() {
		return ( ( value == null || value.length() == 0 ) && uniqueID == null );
	}
	
	public ProductSerialExtensionValueBean convertToExtensionValueBean( ProductSerialExtensionBean extention, Product product ) {
		if( isBlank() ) {
			return null;
		}
		
		ProductSerialExtensionValueBean newExtensionValue = new ProductSerialExtensionValueBean();
		newExtensionValue.setUniqueID( uniqueID );
		newExtensionValue.setExtensionValue( value );
		newExtensionValue.setProductSerialExtension( extention );
		newExtensionValue.setProductSerial( product );
		return newExtensionValue;
		
	}
	
}
