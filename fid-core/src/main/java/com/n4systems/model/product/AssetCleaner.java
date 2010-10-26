package com.n4systems.model.product;

import com.n4systems.model.Asset;
import rfid.ejb.entity.AssetSerialExtensionValue;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.model.AbstractEntityCleaner;
import com.n4systems.model.api.Cleaner;
import com.n4systems.model.infooption.InfoOptionCleaner;

public class AssetCleaner extends AbstractEntityCleaner<Asset> {
	
	private final Cleaner<InfoOptionBean> infoOptionCleaner;
	private final Cleaner<AssetSerialExtensionValue> productSerialExtensionValueCleaner;
	
	public AssetCleaner() {
		this(new InfoOptionCleaner(), new AssetSerialExtensionValueCleaner());
	}
	
	public AssetCleaner(Cleaner<InfoOptionBean> infoOptionCleaner, Cleaner<AssetSerialExtensionValue> productSerialExtensionValueCleaner) {
		this.infoOptionCleaner = infoOptionCleaner;
		this.productSerialExtensionValueCleaner = productSerialExtensionValueCleaner;
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
			productSerialExtensionValueCleaner.clean(extension);
		}
	}

}
