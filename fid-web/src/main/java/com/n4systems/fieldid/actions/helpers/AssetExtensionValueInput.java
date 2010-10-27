package com.n4systems.fieldid.actions.helpers;

import com.n4systems.model.Asset;

import rfid.ejb.entity.AssetExtensionValue;
import rfid.ejb.entity.AssetExtension;

public class AssetExtensionValueInput {
	
	private Long uniqueID;
	private Long extensionId;
	private String value;
	
	public AssetExtensionValueInput() {
	}
	
	public AssetExtensionValueInput( AssetExtensionValue value ) {
		this.extensionId = value.getAssetExtension().getUniqueID();
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
	
	public AssetExtensionValue convertToExtensionValueBean( AssetExtension extention, Asset asset) {
		if( isBlank() ) {
			return null;
		}
		
		AssetExtensionValue newExtensionValue = new AssetExtensionValue();
		newExtensionValue.setUniqueID( uniqueID );
		newExtensionValue.setExtensionValue( value );
		newExtensionValue.setAssetExtension( extention );
		newExtensionValue.setAsset(asset);
		return newExtensionValue;
		
	}
	
}
