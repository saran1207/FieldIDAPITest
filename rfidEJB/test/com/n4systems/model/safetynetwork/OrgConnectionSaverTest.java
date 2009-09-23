package com.n4systems.model.safetynetwork;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;

import org.junit.Test;

import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.testutils.DummyEntityManager;

public class OrgConnectionSaverTest {
	
	@Test
	public void test_create_customer_org_from_connection() { 
		class TestOrgConnectionSaver extends OrgConnectionSaver {
			public CustomerOrg createCustomerOrgFromConnection(OrgConnection conn) {
				return super.createCustomerOrgFromConnection(conn);
			}
		};
		
		TestOrgConnectionSaver saver = new TestOrgConnectionSaver();
		
		OrgConnection conn = new OrgConnection();
		conn.setVendor((InternalOrg)OrgBuilder.aPrimaryOrg().build());
		conn.setCustomer((InternalOrg)OrgBuilder.aSecondaryOrg().build());
		
		CustomerOrg org = saver.createCustomerOrgFromConnection(conn);
		
		assertEquals(conn.getVendor().getTenant(), org.getTenant());
		assertEquals(conn.getVendor(), org.getParent());
		assertEquals(conn.getCustomer(), org.getLinkedOrg());
	}
	
	@Test
	public void save_creates_and_saves_customer() {
		class TestOrgSaver extends OrgSaver {
			public boolean saveCalled = false;
			
			public void save(EntityManager em, BaseOrg entity) {
				saveCalled = true;
			}
		};
		
		class TestOrgConnectionSaver extends OrgConnectionSaver {
			public boolean createCustomerCalled = false;
			
			public TestOrgConnectionSaver(OrgSaver saver) {
				super(saver);
			}
			
			public CustomerOrg createCustomerOrgFromConnection(OrgConnection conn) {
				createCustomerCalled = true;
				return null;
			}
		};
		
		TestOrgSaver orgSaver = new TestOrgSaver();
		TestOrgConnectionSaver connSaver = new TestOrgConnectionSaver(orgSaver);
		
		connSaver.save(new DummyEntityManager(), new OrgConnection());
		
		assertTrue(connSaver.createCustomerCalled);
		assertTrue(orgSaver.saveCalled);
		
	}
}
