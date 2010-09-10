package com.n4systems.fieldid.actions.safetyNetwork;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.safetynetwork.TypedOrgConnection;
import com.n4systems.security.Permissions;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.CatalogServiceImpl;
import com.n4systems.util.ConfigEntry;

import java.util.ArrayList;
import java.util.List;

@UserPermissionFilter(userRequiresOneOf = { Permissions.ManageSafetyNetwork })
public class SafetyNetwork extends AbstractCrud {
	private static final long serialVersionUID = 1L;

	public SafetyNetwork(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

    private List<TypedOrgConnection> customerConnections;
    private List<TypedOrgConnection> vendorConnections;
    private List<TypedOrgConnection> catalogOnlyConnections;

	@Override
	protected void loadMemberFields(Long uniqueId) {
	}

	@Override
	protected void initMemberFields() {
	}

    @Override
    protected void postInit() {
		customerConnections = new ArrayList<TypedOrgConnection>();
        vendorConnections = new ArrayList<TypedOrgConnection>();
        catalogOnlyConnections = new ArrayList<TypedOrgConnection>();
        for (TypedOrgConnection connection : getConnections()) {
            if (connection.isCustomerConnection()) {
                customerConnections.add(connection);
            } else if (connection.isVendorConnection()) {
                vendorConnections.add(connection);
            } else if (connection.isCatalogOnlyConnection()) {
                catalogOnlyConnections.add(connection);
            }
        }
    }

    public String doShow() {
		return SUCCESS;
	}

	public String getHelpUrl() {
		return getConfigContext().getString(ConfigEntry.SAFETY_NETWORK_HELP_URL);
	}

	public String getVideoUrl() {
		return getConfigContext().getString(ConfigEntry.SAFETY_NETWORK_VIDEO_URL);
	}

	public Long getUnreadMessageCount() {
		return getLoaderFactory().createUnreadMessageCountLoader().load();
	}

	public List<TypedOrgConnection> getConnections() {
		return getLoaderFactory().createdTypedOrgConnectionListLoader().load();
	}

	public boolean hasAPublishedCatalog(BaseOrg org) {
		try {
			CatalogService catalogService = new CatalogServiceImpl(persistenceManager, org.getTenant());
			return catalogService.hasCatalog();
		} catch (Exception e) {
			return false;
		}
	}

	public List<TypedOrgConnection> getCustomerConnections() {
		return customerConnections;
	}

	public List<TypedOrgConnection> getVendorConnections() {
		return vendorConnections;
	}

	public List<TypedOrgConnection> getCatalogOnlyConnections() {
		return catalogOnlyConnections;
	}

}
