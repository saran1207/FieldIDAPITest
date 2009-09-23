package com.n4systems.model.safetynetwork;

import javax.persistence.EntityManager;

import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.persistence.savers.Saver;

public class OrgConnectionSaver extends Saver<OrgConnection> {

	private final OrgSaver orgSaver;
	
	public OrgConnectionSaver() {
		this(new OrgSaver());
	}
	
	public OrgConnectionSaver(OrgSaver saver) {
		this.orgSaver = saver;
	}
	
	@Override
	protected void save(EntityManager em, OrgConnection conn) {
		super.save(em, conn);
		
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

}
