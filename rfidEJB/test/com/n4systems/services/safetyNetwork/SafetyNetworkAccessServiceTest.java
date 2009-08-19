package com.n4systems.services.safetyNetwork;

import static org.junit.Assert.*;

import org.junit.Test;



public class SafetyNetworkAccessServiceTest {


	@Test
	public void should_allow_access_to_catalog() {
		fail("Fix me");
		
//		ManufacturerOrganization linkedTenant = aManufacturer("nis");
//		Tenant n4 = aTenant().named("n4").linkedTo(linkedTenant).build();
//		
//		
//		PersistenceManager mockPersistenceManager = createMock(PersistenceManager.class);
//		expect(mockPersistenceManager.find(Tenant.class, n4.getId(), "linkedTenants")).andReturn(n4);
//		expectLastCall().once();
//		replay(mockPersistenceManager);
//		
//		SafetyNetworkAccessService sut = new SafetyNetworkAccessService(mockPersistenceManager, n4);
//		try {
//			CatalogService catalogAccess = sut.getCatalogAccess(linkedTenant);
//			assertNotNull(catalogAccess);
//			assertEquals(linkedTenant, catalogAccess.getTenant());
//		} catch (NoAccessToTenantException e) {
//			fail("should not throw no access to tenant");
//		}
//		
//		verify(mockPersistenceManager);
	}
	
	@Test
	public void should_deny_access_to_catalog_of_non_linked_tenant() {
		fail("Fix me");
//		ManufacturerOrganization linkedTenant = aManufacturer("nis");
//		Tenant n4 = aTenant().named("n4").build();
//		
//		
//		PersistenceManager mockPersistenceManager = createMock(PersistenceManager.class);
//		expect(mockPersistenceManager.find(Tenant.class, n4.getId(), "linkedTenants")).andReturn(n4);
//		expectLastCall().once();
//		replay(mockPersistenceManager);
//		
//		SafetyNetworkAccessService sut = new SafetyNetworkAccessService(mockPersistenceManager, n4);
//		try {
//			sut.getCatalogAccess(linkedTenant);
//			fail("should have thrown no access to tenant exception");
//		} catch (NoAccessToTenantException e) {
//			assertTrue(true);
//		}
//		
//				
//		verify(mockPersistenceManager);
	}

	
}
