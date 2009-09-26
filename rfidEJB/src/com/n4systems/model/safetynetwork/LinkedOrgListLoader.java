package com.n4systems.model.safetynetwork;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;

public class LinkedOrgListLoader extends ListLoader<InternalOrg> {

	private final OrgConnectionListLoader connectionLoader;
	
	public LinkedOrgListLoader(SecurityFilter filter, OrgConnectionListLoader connectionLoader) {
		super(filter);
		this.connectionLoader = connectionLoader;
	}
	
	public LinkedOrgListLoader(SecurityFilter filter, OrgConnectionType connectionType) {
		this(filter, new OrgConnectionListLoader(filter, connectionType));
	}

	@Override
	protected List<InternalOrg> load(EntityManager em, SecurityFilter filter) {
		OrgConnectionType type = connectionLoader.getConnectionListType();
		
		List<OrgConnection> connections = connectionLoader.load(em, filter);
		
		List<InternalOrg> orgs = new ArrayList<InternalOrg>();
		for (OrgConnection conn: connections) {
			orgs.add(conn.getByConnectionType(type));
		}
		return orgs;
	}

}
