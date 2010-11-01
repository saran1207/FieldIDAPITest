package com.n4systems.model.asset;

import com.n4systems.model.Asset;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.model.AbstractEntityCleaner;
import com.n4systems.model.api.Cleaner;
import com.n4systems.model.infooption.InfoOptionCleaner;

public class AssetCleaner extends AbstractEntityCleaner<Asset> {
	
	private final Cleaner<InfoOptionBean> infoOptionCleaner;
	
	public AssetCleaner() {
		this(new InfoOptionCleaner());
	}
	
	public AssetCleaner(Cleaner<InfoOptionBean> infoOptionCleaner) {
		this.infoOptionCleaner = infoOptionCleaner;
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
	}
}
