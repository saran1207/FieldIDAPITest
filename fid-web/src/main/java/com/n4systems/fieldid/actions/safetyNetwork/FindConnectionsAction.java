package com.n4systems.fieldid.actions.safetyNetwork;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.tools.Pager;

import java.util.List;

public class FindConnectionsAction extends SafetyNetwork {

    public FindConnectionsAction(PersistenceManager persistenceManager) {
        super(persistenceManager);
    }

    private List<OrgConnection> connectionsForWhichWeAreCustomer;
    private List<OrgConnection> connectionsForWhichWeAreVendor;
    private String searchText;
    
    public String doSearch() {
        connectionsForWhichWeAreCustomer = getLoaderFactory().createVendorOrgConnectionsListLoader().load();
        connectionsForWhichWeAreVendor = getLoaderFactory().createCustomerOrgConnectionsListLoader().load();

        return SUCCESS;
    }

    public boolean isSearch() {
        return searchText != null;
    }

    public Pager<PrimaryOrg> getPage() {
        return getLoaderFactory().createPrimaryOrgsWithNameLikeLoader()
                .setName(searchText)
                .setPage(getCurrentPage())
                .setPageSize(10)
                .load();
    }

    public boolean isConnectedCustomer(PrimaryOrg org) {
        for (OrgConnection connection : connectionsForWhichWeAreCustomer) {
            if (connection.getVendor().getID().equals(org.getId())) {
                return true;
            }
        }
        return false;
    }

    public boolean isConnectedVendor(PrimaryOrg org) {
        for (OrgConnection connection : connectionsForWhichWeAreVendor) {
            if (connection.getCustomer().getID().equals(org.getId())) {
                return true;
            }
        }
        return false;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

}
