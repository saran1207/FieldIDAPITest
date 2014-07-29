package com.n4systems.model.dashboard.widget;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.AllowSafetyNetworkAccess;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "widget_configurations_procedures_without_audits")
@PrimaryKeyJoinColumn(name="id")
public class ProceduresWithoutAuditsWidgetConfiguration extends WidgetConfiguration {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "org_id")
    private BaseOrg org;

    @AllowSafetyNetworkAccess
    public BaseOrg getOrg() {
        return org;
    }

    public void setOrg(BaseOrg org) {
        this.org = org;
    }

}
