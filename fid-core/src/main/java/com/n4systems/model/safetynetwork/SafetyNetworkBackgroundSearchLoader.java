package com.n4systems.model.safetynetwork;

import java.util.List;

import com.n4systems.model.Asset;
import com.n4systems.model.security.SecurityFilter;

public class SafetyNetworkBackgroundSearchLoader {
	
	private final SafetyNetworkSmartSearchLoader networkSmartSearchLoader;
	private String identifier;
	private String rfidNumber;
	private String refNumber;
	
	public SafetyNetworkBackgroundSearchLoader(SecurityFilter filter) {
		networkSmartSearchLoader = new SafetyNetworkSmartSearchLoader(filter);
	}
	
	public Asset load() {
		Asset asset = searchByRfidNumber();
		
		if (asset == null) {
			asset = searchByIdentifier();
		}
		
		if (asset == null) {
			asset = searchByRefNumber();
		}
		
		return asset;
	}
	
	private Asset searchByRfidNumber() {
		Asset asset = null;
		if (rfidNumber != null && rfidNumber.length() > 0) {
			List<Asset> assets = networkSmartSearchLoader.useOnlyRfidNumber().setSearchText(rfidNumber).load();
			asset = pullAssetFromList(assets);
		}
		return asset;
	}
	
	private Asset searchByIdentifier() {
		Asset asset = null;
		if (identifier != null && identifier.length() > 0) {
			List<Asset> assets = networkSmartSearchLoader.useOnlyIdentifier().setSearchText(identifier).load();
			asset = pullAssetFromList(assets);
		}
		return asset;
	}
	
	private Asset searchByRefNumber() {
		Asset asset = null;
		if (refNumber != null && refNumber.length() > 0) {
			List<Asset> assets = networkSmartSearchLoader.useOnlyRefNumber().setSearchText(refNumber).load();
			asset = pullAssetFromList(assets);
		}
		return asset;
	}
	
	private Asset pullAssetFromList(List<Asset> assets) {
		Asset asset = null;
		
		if (assets.size() > 0) {
			asset = assets.get(0);
		}
		
		return asset;
	}
	
	public SafetyNetworkBackgroundSearchLoader setVendorOrgId(Long vendorOrgId) {
		networkSmartSearchLoader.setVendorOrgId(vendorOrgId);
		return this;
	}

	public SafetyNetworkBackgroundSearchLoader setIdentifier(String identifier) {
		this.identifier = identifier;
		return this;
	}

	public SafetyNetworkBackgroundSearchLoader setRfidNumber(String rfidNumber) {
		this.rfidNumber = rfidNumber;
		return this;
	}

	public SafetyNetworkBackgroundSearchLoader setRefNumber(String refNumber) {
		this.refNumber = refNumber;
		return this;
	}
}
