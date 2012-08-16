package com.n4systems.model.dashboard.widget.interfaces;

import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;

public interface ConfigurationForAgenda {

    AssetType getAssetType();
    EventType getEventType();
    User getUser();
    BaseOrg getOrg();
}
