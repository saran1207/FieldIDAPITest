package com.n4systems.fieldid.selenium.datatypes;

import java.util.Map;
import java.util.TreeMap;

public class AssetCodeMapping {
	String assetCode;
	String referenceNumber;
	String assetType;
	Map<Attribute,String> assetAttributes = new TreeMap<Attribute,String>();
	
	public AssetCodeMapping(String assetCode, String referenceNumber, String assetType, Map<Attribute,String> assetAttributes) {
		this(assetCode, assetType, assetAttributes);
		this.referenceNumber = referenceNumber;
	}

	public AssetCodeMapping(String assetCode, String assetType, Map<Attribute,String> assetAttributes) {
		this.assetCode = assetCode;
		this.assetType = assetType;
		this.assetAttributes = assetAttributes;
	}

	public AssetCodeMapping() {
	}
	
	public String getAssetCode() {
		return assetCode;
	}
	
	public String getReferenceNumber() {
		return referenceNumber;
	}
	
	public String getAssetType() {
		return assetType;
	}
	
	public Map<Attribute,String> getAssetAttributes() {
		return assetAttributes;
	}
	
	public void setAssetCode(String s) {
		this.assetCode = s;
	}
	
	public void setReferenceNumber(String s) {
		this.referenceNumber = s;
	}
	
	public void setAssetType(String s) {
		this.assetType = s;
	}
	
	public void setAssetAttributes(Map<Attribute,String> assetAttributes) {
		this.assetAttributes = assetAttributes;
	}
	
	public void addAssetAttribute(Attribute key, String value) {
		assetAttributes.put(key, value);
	}
	
	public String getAssetAttribute(String key) {
		return assetAttributes.get(key);
	}
}
