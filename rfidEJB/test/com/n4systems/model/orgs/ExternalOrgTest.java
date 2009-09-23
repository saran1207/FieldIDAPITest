package com.n4systems.model.orgs;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.AddressInfo;

public class ExternalOrgTest {
	@SuppressWarnings("serial")
	class TestExternalOrg extends ExternalOrg {
		public boolean updateFieldsFromOrgCalled = false;
		public boolean updateAddressInfoCalled = false;
		@Override
		public CustomerOrg getCustomerOrg() { return null; }
		@Override
		public DivisionOrg getDivisionOrg() { return null; }
		@Override
		public String getFilterPath() { return null; }
		@Override
		public InternalOrg getInternalOrg() { return null; }
		@Override
		public BaseOrg getParent() { return null; }
		@Override
		public PrimaryOrg getPrimaryOrg() { return null; }
		@Override
		public SecondaryOrg getSecondaryOrg() { return null; }
		@Override
		public void onCreate() { super.onCreate(); }
		@Override
		public void onUpdate() { super.onUpdate(); }
		@Override
		public void updateAddressInfo(AddressInfo newAddressInfo) {
			super.updateAddressInfo(newAddressInfo);
			updateAddressInfoCalled = true;
		}
		@Override
		public void updateFieldsFromOrg(InternalOrg org) {
			assertSame(getLinkedOrg(), org);
			super.updateFieldsFromOrg(org);
			updateFieldsFromOrgCalled = true;
		}
	}
	
	@Test
	public void update_address_info_handles_null() {
		TestExternalOrg org = new TestExternalOrg();
		org.updateAddressInfo(null);
	}
	
	@Test
	public void oncreate_handles_null_linkedorg() {
		TestExternalOrg orgs = new TestExternalOrg();
		orgs.onCreate();
		
		assertTrue(orgs.updateFieldsFromOrgCalled);
	}
	
	@Test
	public void onupdate_handles_null_linkedorg() {
		TestExternalOrg orgs = new TestExternalOrg();
		orgs.onUpdate();
		
		assertTrue(orgs.updateFieldsFromOrgCalled);
	}
	
	@Test
	public void oncreate_calls_update_fields_from_org_with_linked_org() {
		TestExternalOrg orgs = new TestExternalOrg();
		orgs.setLinkedOrg(new PrimaryOrg());
		orgs.onCreate();
		
		assertTrue(orgs.updateFieldsFromOrgCalled);
	}
	
	@Test
	public void onupdate_calls_update_fields_from_org_with_linked_org() {
		TestExternalOrg orgs = new TestExternalOrg();
		orgs.setLinkedOrg(new PrimaryOrg());
		orgs.onUpdate();
		
		assertTrue(orgs.updateFieldsFromOrgCalled);
	}
	
	@Test
	public void update_fields_from_org_updates_name() {
		TestExternalOrg org = new TestExternalOrg();
		org.setName("my old name");
		
		PrimaryOrg nameOrg = new PrimaryOrg();
		nameOrg.setName("my new name");
		
		org.setLinkedOrg(nameOrg); // <-- this is just so the assertSame passes on TestExternalOrg
		
		org.updateFieldsFromOrg(nameOrg);
		
		assertEquals(nameOrg.getName(), org.getName());
	}
	
	@Test
	public void update_fields_calls_update_address_info() {
		TestExternalOrg org = new TestExternalOrg();
		
		PrimaryOrg otherOrg = new PrimaryOrg();
		
		org.setLinkedOrg(otherOrg);
		org.updateFieldsFromOrg(otherOrg);
		
		assertTrue(org.updateAddressInfoCalled);
	}
	
	@Test
	public void update_address_info_clears_id_if_old_addressinfo_was_null() {
		TestExternalOrg org = new TestExternalOrg();
		
		AddressInfo address = new AddressInfo();
		address.setId(-1L);
		address.setCity("Mark's town");
		
		org.updateAddressInfo(address);
		
		assertNull(org.getAddressInfo().getId());
		assertEquals(org.getAddressInfo().getCity(), address.getCity());
	}
	
	@Test
	public void update_address_info_preserves_old_id() {
		TestExternalOrg org = new TestExternalOrg();
		long oldId = -1L;
			
		AddressInfo oldAddress = new AddressInfo();
		oldAddress.setId(oldId);
		oldAddress.setCity("Mark's town");
		org.setAddressInfo(oldAddress);
		
		AddressInfo newAddress = new AddressInfo();
		newAddress.setId(10L);
		newAddress.setCity("Funkytown");
		
		org.updateAddressInfo(newAddress);
		
		assertEquals(new Long(oldId), org.getAddressInfo().getId());
		assertEquals(org.getAddressInfo().getCity(), newAddress.getCity());
	}
}
