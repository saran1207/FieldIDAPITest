package com.n4systems.model.safetynetwork;

import javax.persistence.EntityManager;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.safetynetwork.TypedOrgConnection.ConnectionType;
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
		
		TypedOrgConnection customerOrgConnection = new TypedOrgConnection(conn.getVendor().getTenant(), conn.getVendor());
		customerOrgConnection.setConnectionType(ConnectionType.CUSTOMER);
		customerOrgConnection.setConnectedOrg(conn.getCustomer());
		customerOrgConnection.setOrgConnection(conn);
		
		em.persist(conn);
		
		TypedOrgConnection vendorOrgConnection = new TypedOrgConnection(conn.getCustomer().getTenant(), conn.getCustomer());
		vendorOrgConnection.setConnectionType(ConnectionType.VENDOR);
		vendorOrgConnection.setConnectedOrg(conn.getVendor());
		vendorOrgConnection.setOrgConnection(conn);
		
		em.persist(vendorOrgConnection);
		
		// on save, we also need to create our linked customer
		orgSaver.save(em, createCustomerOrgFromConnection(conn));
		
		
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
