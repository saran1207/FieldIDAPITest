package com.n4systems.services.safetyNetwork;

import static com.n4systems.model.builders.ProductTypeBuilder.*;
import static com.n4systems.model.builders.TenantBuilder.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;


import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.easymock.Capture;
import org.junit.Test;

import rfid.ejb.session.LegacyProductType;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.ProductType;
import com.n4systems.model.Tenant;
import com.n4systems.test.helpers.BasicAnswer;

public class ImportCatalogServiceTest {

	@SuppressWarnings("unchecked")
	@Test
	public void test_import_one_product_type() {
		Tenant n4 = aTenant().named("n4").build();
		Tenant linkedTenant = aTenant().named("nischain").build();
		
		ProductType productType = aProductType().named("chain").build();
		Capture<ProductType> captureType = new Capture<ProductType>();
		
		PersistenceManager mockPersistenceManager = createMock(PersistenceManager.class);
		expect(mockPersistenceManager.find(ProductType.class, productType.getId(), linkedTenant.getId(), "infoFields")).andReturn(productType);
		expectLastCall().atLeastOnce();
		
		replay(mockPersistenceManager);
		
		CatalogService mockCatalogService = createMock(CatalogService.class);
		replay(mockCatalogService);
		
		LegacyProductType mockProductTypeManager = createMock(LegacyProductType.class);
		try {
			expect(mockProductTypeManager.updateProductType(capture(captureType), (List<FileAttachment>)anyObject(), (File)anyObject())).andAnswer(new BasicAnswer<ProductType>(captureType));
		} catch (Exception e) {
			fail("creating mock there should be no exception thrown.");
		}
		replay(mockProductTypeManager);
		
		ImportCatalogService sut = new ImportCatalogService(mockPersistenceManager, n4, mockCatalogService, mockProductTypeManager);
		Set<Long> expectedImports = new HashSet<Long>();
		expectedImports.add(productType.getId());
		
		sut.setImportProductTypeIds(expectedImports);
		
		
		/*verify(mockPersistenceManager);
		verify(mockCatalogService);
		assertNotNull(captureType.getValue());
		ProductType actualProductType = captureType.getValue();
		assertTrue(actualProductType.isNew());
		assertEquals(n4, actualProductType.getTenant());*/
		
	}

}
