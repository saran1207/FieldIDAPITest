package com.n4systems.model.safetynetwork;

import java.util.List;

import com.n4systems.model.Asset;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.Loader;

import javax.persistence.EntityManager;

public class SafetyNetworkBackgroundSearchLoader extends Loader<Asset> {
	
	private final SafetyNetworkSmartSearchLoader networkSmartSearchLoader;
	private String serialNumber;
	private String rfidNumber;
	private String refNumber;
	
	public SafetyNetworkBackgroundSearchLoader(SecurityFilter filter) {
		networkSmartSearchLoader = new SafetyNetworkSmartSearchLoader(filter);
	}

    @Override
    protected Asset load(EntityManager em) {
        return load();
    }

    public Asset load() {
		Asset asset = searchByRfidNumber();
		
		if (asset == null) {
			asset = searchBySerialNumber();
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
	
	private Asset searchBySerialNumber() {
		Asset asset = null;
		if (serialNumber != null && serialNumber.length() > 0) {
			List<Asset> assets = networkSmartSearchLoader.useOnlySerialNumber().setSearchText(serialNumber).load();
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

	public SafetyNetworkBackgroundSearchLoader setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
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
