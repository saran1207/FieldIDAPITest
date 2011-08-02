package com.n4systems.fieldid.actions.asset;

import java.io.Serializable;

public class AssetIdentifierView implements Serializable {
	private static final long serialVersionUID = 1L;

    private Long assetId;
	private String identifier;
	private String rfidNumber;
	private String referenceNumber;
	
	public AssetIdentifierView() {}
	
	public AssetIdentifierView(String identifier) {
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getRfidNumber() {
		return rfidNumber;
	}

	public void setRfidNumber(String rfidNumber) {
		this.rfidNumber = rfidNumber;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }
}
