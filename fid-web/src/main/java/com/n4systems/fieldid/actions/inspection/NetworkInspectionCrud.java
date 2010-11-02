package com.n4systems.fieldid.actions.inspection;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.ejb.legacy.UserManager;

public class NetworkInspectionCrud extends InspectionCrud {

    public NetworkInspectionCrud(PersistenceManager persistenceManager, EventManager eventManager, UserManager userManager, LegacyAsset legacyProductManager, AssetManager assetManager, InspectionScheduleManager inspectionScheduleManager) {
        super(persistenceManager, eventManager, userManager, legacyProductManager, assetManager, inspectionScheduleManager);
    }

    @Override
    protected void loadMemberFields(Long uniqueId) {
        event = getLoaderFactory().createSafetyNetworkInspectionLoader(true).setId(uniqueId).load();
    }

}
