package com.n4systems.fieldid.actions.safetyNetwork;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.safetynetwork.TypedOrgConnection;
import com.n4systems.security.Permissions;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.CatalogServiceImpl;
import com.n4systems.tools.Pager;
import com.n4systems.util.ConfigEntry;

@UserPermissionFilter(userRequiresOneOf = { Permissions.ManageSafetyNetwork })
public class SafetyNetwork extends AbstractCrud {
	private static final long serialVersionUID = 1L;

	public SafetyNetwork(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
	}

	@Override
	protected void initMemberFields() {
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

	public Pager<TypedOrgConnection> getConnections() {
		return getLoaderFactory().createPaginatedConnectionListLoader().load();
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
		List<TypedOrgConnection> customerConnections = new ArrayList<TypedOrgConnection>();
		Pager<TypedOrgConnection> pager = getConnections();
		for (int i =0; i <= pager.getTotalPages(); i++) {
			for (TypedOrgConnection connection : pager.getList()) {
				if (connection.isCustomerConnection()) {
					customerConnections.add(connection);
				}
			}
			pager.getNextPage();
		}
		return customerConnections;
	}

	public List<TypedOrgConnection> getVendorConnections() {
		List<TypedOrgConnection> vendorConnections = new ArrayList<TypedOrgConnection>();
		Pager<TypedOrgConnection> pager = getConnections();
		for (int i =0; i <= pager.getTotalPages(); i++) {
			for (TypedOrgConnection connection : getConnections().getList()) {
				if (connection.isVendorConnection()) {
					vendorConnections.add(connection);
				}
			}
			pager.getNextPage();
		}
		return vendorConnections;
	}

	public List<TypedOrgConnection> getCatalogOnlyConnections() {
		List<TypedOrgConnection> catalogOnlyConnections = new ArrayList<TypedOrgConnection>();
		Pager<TypedOrgConnection> pager = getConnections();
		for (int i =0; i <= pager.getTotalPages(); i++) {
			for (TypedOrgConnection connection : getConnections().getList()) {
				if (connection.isCatalogOnlyConnection()) {
					catalogOnlyConnections.add(connection);
				}
			}
			pager.getNextPage();
		}
		return catalogOnlyConnections;
	}

}
