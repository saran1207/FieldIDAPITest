package com.n4systems.fieldid.selenium.persistence.saver;

import com.n4systems.model.AssetType;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.persistence.savers.Saver;

import java.util.Set;

import javax.persistence.EntityManager;

public class SeleniumAssetTypeSaver extends Saver<AssetType> {

    @Override
	public void save(EntityManager em, AssetType assetType) {
        // This is required since associated event types must also be saved or hibernate
        // will be unhappy. We could explore cascade options but this an easy solution for now.
        super.save(em, assetType);
        Set<AssociatedEventType> associatedEventTypes = assetType.getAssociatedEventTypes();
        for (AssociatedEventType type : associatedEventTypes) {
            em.persist(type);
        }
    }

}
