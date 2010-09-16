package com.n4systems.fieldid.actions.safetyNetwork;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.safetynetwork.TypedOrgConnection;
import com.n4systems.tools.Pager;

public class FindConnectionsAction extends SafetyNetwork {

    public FindConnectionsAction(PersistenceManager persistenceManager) {
        super(persistenceManager);
    }
    
    public String doSearch() {
        return SUCCESS;
    }

    public Pager<PrimaryOrg> getPage() {
        return getLoaderFactory().createPrimaryOrgsWithNameLikeLoader()
                .setExcludeOrg(getPrimaryOrg())
                .setName(getSearchText())
                .setSearchableOnly(true)
                .setPage(getCurrentPage())
                .setPageSize(10)
                .load();
    }

    public boolean isConnectedCustomer(PrimaryOrg org) {
        for (TypedOrgConnection connection : getCustomerConnections()) {
            if (connection.getConnectedOrg().getId().equals(org.getId())) {
                return true;
            }
        }
        return false;
    }

    public boolean isConnectedVendor(PrimaryOrg org) {
        for (TypedOrgConnection connection : getVendorConnections()) {
            if (connection.getConnectedOrg().getID().equals(org.getId())) {
                return true;
            }
        }
        return false;
    }

}
