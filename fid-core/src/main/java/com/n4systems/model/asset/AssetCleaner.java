package com.n4systems.model.asset;

import com.n4systems.model.Asset;
import rfid.ejb.entity.AssetSerialExtensionValue;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.model.AbstractEntityCleaner;
import com.n4systems.model.api.Cleaner;
import com.n4systems.model.infooption.InfoOptionCleaner;

public class AssetCleaner extends AbstractEntityCleaner<Asset> {
	
	private final Cleaner<InfoOptionBean> infoOptionCleaner;
	private final Cleaner<AssetSerialExtensionValue> assetSerialExtensionValueCleaner;
	
	public AssetCleaner() {
		this(new InfoOptionCleaner(), new AssetSerialExtensionValueCleaner());
	}
	
	public AssetCleaner(Cleaner<InfoOptionBean> infoOptionCleaner, Cleaner<AssetSerialExtensionValue> assetSerialExtensionValueCleaner) {
		this.infoOptionCleaner = infoOptionCleaner;
		this.assetSerialExtensionValueCleaner = assetSerialExtensionValueCleaner;
	}
	
	@Override
	public void clean(Asset asset) {
		super.clean(asset);
		
		for (InfoOptionBean option: asset.getInfoOptions()) {
			// we only want to clean non static options
			if (!option.isStaticData()) {
				infoOptionCleaner.clean(option);
			}
		}
		
		for (AssetSerialExtensionValue extension: asset.getAssetSerialExtensionValues()) {
			assetSerialExtensionValueCleaner.clean(extension);
		}
	}

}
