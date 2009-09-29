package com.n4systems.model.safetynetwork;

import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.testutils.DummyEntityManager;

public class OrgConnectionSaverTest {
	
	private OrgConnection conn;
	
	@Before
	public void setup() {
		conn = new OrgConnection(
				(PrimaryOrg)OrgBuilder.aPrimaryOrg().build(), 
				(PrimaryOrg)OrgBuilder.aPrimaryOrg().build()
		);
	}
	
	@Test
	public void test_create_customer_org_from_connection() { 
		class TestOrgConnectionSaver extends OrgConnectionSaver {
			public TestOrgConnectionSaver() { super(-1L); }
			
			public CustomerOrg createCustomerOrgFromConnection(OrgConnection conn) {
				return super.createCustomerOrgFromConnection(conn);
			}
		};
		
		TestOrgConnectionSaver saver = new TestOrgConnectionSaver();
		
		CustomerOrg org = saver.createCustomerOrgFromConnection(conn);
		
		assertEquals(conn.getVendor().getTenant(), org.getTenant());
		assertEquals(conn.getVendor(), org.getParent());
		assertEquals(conn.getCustomer(), org.getLinkedOrg());
	}
	
	@Test
	public void save_creates_and_saves_customer() {		
		DummyEntityManager em = new DummyEntityManager();
		
		final CustomerOrg customer = (CustomerOrg)OrgBuilder.aCustomerOrg().build();
		
		OrgSaver orgSaver = createMock(OrgSaver.class);
		orgSaver.save(em, customer);
		replay(orgSaver);
		
		
		class TestOrgConnectionSaver extends OrgConnectionSaver {
			public TestOrgConnectionSaver(OrgSaver saver) {
				super(saver, -1L);
			}
			
			public CustomerOrg createCustomerOrgFromConnection(OrgConnection conn) {
				return customer;
			}
		};
		
		TestOrgConnectionSaver connSaver = new TestOrgConnectionSaver(orgSaver);
		
		connSaver.save(em, conn);
	}
	
	@Test
	public void save_silently_ignores_house_account_on_vendor() {
		DummyEntityManager em = new DummyEntityManager();
		
		OrgSaver orgSaver = createMock(OrgSaver.class);
		// we don't expect to see the save call here
		replay(orgSaver);
		
		OrgConnectionSaver orgConnSaver = new OrgConnectionSaver(orgSaver, conn.getVendor().getId());
		
		orgConnSaver.save(em, conn);
	}
	
	@Test
	public void save_silently_ignores_house_account_on_customer() {
		DummyEntityManager em = new DummyEntityManager();
		
		OrgSaver orgSaver = createMock(OrgSaver.class);
		// we don't expect to see the save call here
		replay(orgSaver);
		
		OrgConnectionSaver orgConnSaver = new OrgConnectionSaver(orgSaver, conn.getCustomer().getId());
		
		orgConnSaver.save(em, conn);
	}
}
