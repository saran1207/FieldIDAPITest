package com.n4systems.fieldid.selenium.persistence.builder;

import com.n4systems.model.builders.BaseBuilder;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.model.safetynetwork.TypedOrgConnection;

public class SafetyNetworkConnectionBuilder extends BaseBuilder<OrgConnection> {

    private final PrimaryOrg fromOrg;
    private final PrimaryOrg toOrg;
    private final TypedOrgConnection.ConnectionType connectionType;

    public static SafetyNetworkConnectionBuilder aSafetyNetworkConnection() {
        return new SafetyNetworkConnectionBuilder(null, null, null);
    }

    private SafetyNetworkConnectionBuilder(PrimaryOrg fromOrg, PrimaryOrg toOrg, TypedOrgConnection.ConnectionType connectionType) {
        this.fromOrg = fromOrg;
        this.toOrg = toOrg;
        this.connectionType = connectionType;
    }

    public SafetyNetworkConnectionBuilder from(PrimaryOrg fromOrg) {
        return makeBuilder(new SafetyNetworkConnectionBuilder(fromOrg, toOrg, connectionType));
    }

    public SafetyNetworkConnectionBuilder to(PrimaryOrg toOrg) {
        return makeBuilder(new SafetyNetworkConnectionBuilder(fromOrg, toOrg, connectionType));
    }

    public SafetyNetworkConnectionBuilder type(TypedOrgConnection.ConnectionType connectionType) {
        return makeBuilder(new SafetyNetworkConnectionBuilder(fromOrg, toOrg, connectionType));
    }

    public OrgConnection createObject() {
        OrgConnection orgConnection = null;
        if (connectionType == TypedOrgConnection.ConnectionType.CUSTOMER) {
            orgConnection = new OrgConnection(fromOrg, toOrg);
        } else if (connectionType == TypedOrgConnection.ConnectionType.VENDOR) {
            orgConnection = new OrgConnection(toOrg, fromOrg);
        }
        return orgConnection;
    }

}
