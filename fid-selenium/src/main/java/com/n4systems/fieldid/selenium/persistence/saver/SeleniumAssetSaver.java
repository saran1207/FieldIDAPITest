package com.n4systems.fieldid.selenium.persistence.saver;

import com.n4systems.model.Asset;
import com.n4systems.persistence.savers.Saver;

import javax.persistence.EntityManager;

public class SeleniumAssetSaver extends Saver<Asset> {

    @Override
	public void save(EntityManager em, Asset asset) {
        // Not sure why this is required. The normal AssetSaver somehow fails to
        // leave the asset in a state where the rest of the scenario can see it.
        // This has to do with the magical double saving and the use of the old asset manager
        // stuff I think. This is a replacement for the AssetSaver until I can fix the way
        // the asset saving/persistence in general works.

        super.save(em, asset);
        if (asset.getNetworkId() == null) {
            asset.setNetworkId(asset.getId());
            super.update(em, asset);
        }
    }

}
