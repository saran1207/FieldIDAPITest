package com.n4systems.model.safetynetwork;

import javax.persistence.EntityManager;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.safetynetwork.TypedOrgConnection.ConnectionType;
import com.n4systems.model.security.SafetyNetworkSecurityCache;
import com.n4systems.persistence.savers.Saver;

public class OrgConnectionSaver extends Saver<OrgConnection> {
	private final Long houseAccountId;
	private final OrgSaver orgSaver;
	
	public OrgConnectionSaver(Long houseAccountId) {
		this(new OrgSaver(), houseAccountId);
	}
	
	public OrgConnectionSaver(OrgSaver saver, Long houseAccountId) {
		this.orgSaver = saver;
		this.houseAccountId = houseAccountId;
	}
	
	@Override
	protected void save(EntityManager em, OrgConnection conn) {
		if (isUsingHouseAccount(conn)) {
			// you can never link to a house account we'll fail silently is this conn has one in it
			return;
		}
		
		super.save(em, conn);

		createTypedConnections(em, conn);
		
		// on save, we also need to create our linked customer
		orgSaver.save(em, createCustomerOrgFromConnection(conn));
		
		// now update the connection cache
		SafetyNetworkSecurityCache.getInstance().connect(conn);
	}

	private void createTypedConnections(EntityManager em, OrgConnection conn) {
		TypedOrgConnection customerOrgConnection = createTypedConnection(conn, conn.getVendor(), conn.getCustomer());
		em.persist(customerOrgConnection);
		
		
		TypedOrgConnection vendorOrgConnection = createTypedConnection(conn, conn.getCustomer(), conn.getVendor());
		em.persist(vendorOrgConnection);
	}

	private TypedOrgConnection createTypedConnection(OrgConnection conn, InternalOrg owner, InternalOrg connectedOrg) {
		TypedOrgConnection customerOrgConnection = new TypedOrgConnection(owner.getTenant(), owner);
		customerOrgConnection.setConnectionType(ConnectionType.CUSTOMER);
		customerOrgConnection.setConnectedOrg(connectedOrg);
		customerOrgConnection.setOrgConnection(conn);
		return customerOrgConnection;
	}
	
	protected CustomerOrg createCustomerOrgFromConnection(OrgConnection conn) {
		CustomerOrg customerOrg = new CustomerOrg();
		customerOrg.setTenant(conn.getVendor().getTenant());
		customerOrg.setParent(conn.getVendor());
		customerOrg.setLinkedOrg(conn.getCustomer());
		
		return customerOrg;
	}

	private boolean isUsingHouseAccount(OrgConnection conn) {
		return (isOrgHouseAccount(conn.getVendor()) || isOrgHouseAccount(conn.getCustomer()));
	}
	
	private boolean isOrgHouseAccount(BaseOrg org) {
		return org.getId().equals(houseAccountId);
	}
	
}
