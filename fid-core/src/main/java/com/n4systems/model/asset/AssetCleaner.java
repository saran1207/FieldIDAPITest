package com.n4systems.model.asset;

import com.n4systems.model.Asset;
import rfid.ejb.entity.AssetExtensionValue;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.model.AbstractEntityCleaner;
import com.n4systems.model.api.Cleaner;
import com.n4systems.model.infooption.InfoOptionCleaner;

public class AssetCleaner extends AbstractEntityCleaner<Asset> {
	
	private final Cleaner<InfoOptionBean> infoOptionCleaner;
	private final Cleaner<AssetExtensionValue> assetExtensionValueCleaner;
	
	public AssetCleaner() {
		this(new InfoOptionCleaner(), new AssetExtensionValueCleaner());
	}
	
	public AssetCleaner(Cleaner<InfoOptionBean> infoOptionCleaner, Cleaner<AssetExtensionValue> assetExtensionValueCleaner) {
		this.infoOptionCleaner = infoOptionCleaner;
		this.assetExtensionValueCleaner = assetExtensionValueCleaner;
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
		
		for (AssetExtensionValue extension: asset.getAssetExtensionValues()) {
			assetExtensionValueCleaner.clean(extension);
		}
	}

}
