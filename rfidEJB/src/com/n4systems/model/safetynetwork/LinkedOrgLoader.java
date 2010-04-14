package com.n4systems.model.safetynetwork;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;

public class LinkedOrgLoader extends SecurityFilteredLoader<InternalOrg> {
	private final OrgConnectionByLinkedOrgLoader connectionLoader;
	
	public LinkedOrgLoader(SecurityFilter filter, OrgConnectionByLinkedOrgLoader connectionLoader) {
		super(filter);
		this.connectionLoader = connectionLoader;
	}
	
	public LinkedOrgLoader(SecurityFilter filter, OrgConnectionType connectionType) {
		this(filter, new OrgConnectionByLinkedOrgLoader(filter, connectionType));
	}

	@Override
	protected InternalOrg load(EntityManager em, SecurityFilter filter) {
		List<OrgConnection> connections = connectionLoader.load(em, filter);
		
		if (connections == null || connections.isEmpty()) {
			throw new SecurityException(String.format("%s Connection does not exist from [%d] to [%d]", connectionLoader.getConnectionType(), filter.getOwner().getId(), connectionLoader.getLinkedOrgId()));
		}
		
		// These connections will all be for the same linked org and same connection type, so we can use the first
		InternalOrg org = connections.get(0).getByConnectionType(connectionLoader.getConnectionType());
		return org;
	}

	public LinkedOrgLoader setLinkedOrgId(Long linkedOrgId) {
		connectionLoader.setLinkedOrgId(linkedOrgId);
		return this;
	}
	
	
	public LinkedOrgLoader setLinkedOrg(PrimaryOrg primaryOrg) {
		return setLinkedOrgId(primaryOrg.getId());
	}
}
