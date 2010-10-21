package com.n4systems.fieldid.actions.helpers;

import com.n4systems.model.Asset;

import rfid.ejb.entity.AssetSerialExtension;
import rfid.ejb.entity.AssetSerialExtensionValue;

public class ProductExtensionValueInput {
	
	private Long uniqueID;
	
	
	private Long extensionId;
	
	private String value;
	
	public ProductExtensionValueInput() {
		super();
	}
	
	public ProductExtensionValueInput( AssetSerialExtensionValue value ) {
		super();
	
		this.extensionId = value.getAssetSerialExtension().getUniqueID();
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
	
	public AssetSerialExtensionValue convertToExtensionValueBean( AssetSerialExtension extention, Asset asset) {
		if( isBlank() ) {
			return null;
		}
		
		AssetSerialExtensionValue newExtensionValue = new AssetSerialExtensionValue();
		newExtensionValue.setUniqueID( uniqueID );
		newExtensionValue.setExtensionValue( value );
		newExtensionValue.setAssetSerialExtension( extention );
		newExtensionValue.setProductSerial(asset);
		return newExtensionValue;
		
	}
	
}
