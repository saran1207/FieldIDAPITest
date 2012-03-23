package com.n4systems.fieldid.actions.event;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.fieldid.service.event.EventCreationService;

public class NetworkEventCrud extends EventCrud {

    public NetworkEventCrud(PersistenceManager persistenceManager, EventManager eventManager, UserManager userManager, LegacyAsset legacyAssetManager, AssetManager assetManager, EventScheduleManager eventScheduleManager, EventCreationService eventCreationService) {
        super(persistenceManager, eventManager, userManager, legacyAssetManager, assetManager, eventScheduleManager, eventCreationService);
    }

    @Override
    protected void loadMemberFields(Long uniqueId) {
        event = getLoaderFactory().createSafetyNetworkEventLoader(true).setId(uniqueId).load();
    }

}
