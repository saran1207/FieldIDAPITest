package com.n4systems.fieldid.actions.inspection;

import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.ejb.legacy.UserManager;

public class NetworkInspectionCrud extends InspectionCrud {

    public NetworkInspectionCrud(PersistenceManager persistenceManager, InspectionManager inspectionManager, UserManager userManager, LegacyProductSerial legacyProductManager, ProductManager productManager, InspectionScheduleManager inspectionScheduleManager) {
        super(persistenceManager, inspectionManager, userManager, legacyProductManager, productManager, inspectionScheduleManager);
    }

    @Override
    protected void loadMemberFields(Long uniqueId) {
        inspection = getLoaderFactory().createSafetyNetworkInspectionLoader(true).setId(uniqueId).load();
    }

}
