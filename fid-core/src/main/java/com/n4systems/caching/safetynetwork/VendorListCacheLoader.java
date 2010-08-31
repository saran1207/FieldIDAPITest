package com.n4systems.caching.safetynetwork;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.caching.CacheLoader;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.model.safetynetwork.VendorOrgConnectionsListLoader;
import com.n4systems.model.security.OrgOnlySecurityFilter;

public class VendorListCacheLoader implements CacheLoader<VendorListCacheKey, List<InternalOrg>> {

	public VendorListCacheLoader() {}
	
	@Override
	public List<InternalOrg> load(VendorListCacheKey key) {
		List<OrgConnection> connections = createConnectionLoader(key).load();
		
		List<InternalOrg> orgs = new ArrayList<InternalOrg>(connections.size());
		for (OrgConnection conn: connections) {
			orgs.add(conn.getVendor());
		}
		
		return orgs;
	}

	protected VendorOrgConnectionsListLoader createConnectionLoader(VendorListCacheKey key) {
		return new VendorOrgConnectionsListLoader(new OrgOnlySecurityFilter(key.getInternalOrg()));
	}
}
