package com.n4systems.fieldid.actions.inspection;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.ejb.legacy.UserManager;

public class NetworkInspectionCrud extends InspectionCrud {

    public NetworkInspectionCrud(PersistenceManager persistenceManager, InspectionManager inspectionManager, UserManager userManager, LegacyAsset legacyProductManager, AssetManager assetManager, InspectionScheduleManager inspectionScheduleManager) {
        super(persistenceManager, inspectionManager, userManager, legacyProductManager, assetManager, inspectionScheduleManager);
    }

    @Override
    protected void loadMemberFields(Long uniqueId) {
        inspection = getLoaderFactory().createSafetyNetworkInspectionLoader(true).setId(uniqueId).load();
    }

}
