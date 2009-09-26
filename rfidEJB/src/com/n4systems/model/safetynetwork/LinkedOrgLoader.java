package com.n4systems.model.safetynetwork;

import javax.persistence.EntityManager;

import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;

public class LinkedOrgLoader extends SecurityFilteredLoader<InternalOrg> {
	private final OrgConnectionLoader connectionLoader;
	
	public LinkedOrgLoader(SecurityFilter filter, OrgConnectionLoader connectionLoader) {
		super(filter);
		this.connectionLoader = connectionLoader;
	}
	
	public LinkedOrgLoader(SecurityFilter filter, OrgConnectionType connectionType) {
		this(filter, new OrgConnectionLoader(filter, connectionType));
	}

	@Override
	protected InternalOrg load(EntityManager em, SecurityFilter filter) {
		OrgConnection connection = connectionLoader.load();
		
		if (connection == null) {
			throw new SecurityException(String.format("%s Connection does not exist from [%d] to [%d]", connectionLoader.getConnectionType(), filter.getOwner().getId(), connectionLoader.getLinkedOrgId()));
		}
		
		return (connectionLoader.getConnectionType() == OrgConnectionType.CUSTOMER) ? connection.getCustomer() : connection.getVendor();
	}

	public LinkedOrgLoader setLinkedOrgId(Long linkedOrgId) {
		connectionLoader.setLinkedOrgId(linkedOrgId);
		return this;
	}
	
}
