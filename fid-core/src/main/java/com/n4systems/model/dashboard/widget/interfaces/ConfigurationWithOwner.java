package com.n4systems.model.dashboard.widget.interfaces;

import com.n4systems.model.orgs.BaseOrg;

public interface ConfigurationWithOwner {

    BaseOrg getOrg();
    void setOrg(BaseOrg org);
}
