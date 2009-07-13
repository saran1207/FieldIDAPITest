package com.n4systems.services.safetyNetwork;


import static com.n4systems.model.builders.CatalogBuilder.*;
import static com.n4systems.model.builders.ProductTypeBuilder.*;
import static com.n4systems.model.builders.TenantBuilder.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.easymock.Capture;
import org.junit.Test;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Catalog;
import com.n4systems.model.ProductType;
import com.n4systems.model.TenantOrganization;
import com.n4systems.test.helpers.BasicAnswer;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;

public class CatalogServiceTest {

	private TenantOrganization n4;

	public CatalogServiceTest() {
		n4 = aTenant().named("n4").build();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void should_find_catalog_product_type_ids() {
		ProductType productType = aProductType().build();
		Catalog catalog = aCatalog().belongingTo(n4).with(productType).build();
		
		PersistenceManager mockPersistenceManager = createMock(PersistenceManager.class);
		expect(mockPersistenceManager.find((QueryBuilder<Catalog>)anyObject())).andReturn(catalog);
		expectLastCall().once();
		replay(mockPersistenceManager);
		
		Set<Long> expectedIds = new HashSet<Long>();
		expectedIds.add(productType.getId());
		
		CatalogService sut = new CatalogServiceImpl(mockPersistenceManager, n4);
		Set<Long> publishedIds = sut.getProductTypeIdsPublished();
		
		assertEquals(expectedIds, publishedIds);
		verify(mockPersistenceManager);
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_not_find_catalog() {
		PersistenceManager mockPersistenceManager = createMock(PersistenceManager.class);
		expect(mockPersistenceManager.find((QueryBuilder<Catalog>)anyObject())).andReturn(null);
		expectLastCall().once();
		replay(mockPersistenceManager);
		
		Set<Long> expectedIds = new HashSet<Long>();
		
		CatalogService sut = new CatalogServiceImpl(mockPersistenceManager, n4);
		Set<Long> publishedIds = sut.getProductTypeIdsPublished();
		
		assertEquals(expectedIds, publishedIds);
		verify(mockPersistenceManager);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_add_create_catalog_and_publish_product_types() {
		Capture<Catalog> captureCatalog = new Capture<Catalog>();
		Capture<ProductType> captureProductType = new Capture<ProductType>();
		
		PersistenceManager mockPersistenceManager = createMock(PersistenceManager.class);
		expect(mockPersistenceManager.find((QueryBuilder<Catalog>)anyObject())).andReturn(null);
		expectLastCall().once();
		expect(mockPersistenceManager.update(capture(captureCatalog))).andAnswer(new BasicAnswer<Catalog>(captureCatalog));
		expect(mockPersistenceManager.reattchAndFetch(capture(captureProductType), (String)anyObject())).andAnswer(new BasicAnswer<ProductType>(captureProductType));
		expectLastCall().once();
		replay(mockPersistenceManager);
		
		Set<ProductType> productTypes = new HashSet<ProductType>();
		productTypes.add(aProductType().build());
		
		CatalogService sut = new CatalogServiceImpl(mockPersistenceManager, n4);
		Catalog createdCatalog = sut.publishProductTypes(productTypes);
		
		assertEquals(productTypes, createdCatalog.getPublishedProductTypes());
		verify(mockPersistenceManager);
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_add_create_catalog_and_publish_product_types_with_master_product() {
		Capture<Catalog> captureCatalog = new Capture<Catalog>();
		Capture<ProductType> captureProductType = new Capture<ProductType>();
		
		PersistenceManager mockPersistenceManager = createMock(PersistenceManager.class);
		expect(mockPersistenceManager.find((QueryBuilder<Catalog>)anyObject())).andReturn(null);
		expectLastCall().once();
		expect(mockPersistenceManager.update(capture(captureCatalog))).andAnswer(new BasicAnswer<Catalog>(captureCatalog));
		expect(mockPersistenceManager.reattchAndFetch(capture(captureProductType), (String)anyObject())).andAnswer(new BasicAnswer<ProductType>(captureProductType));
		expectLastCall().once();
		replay(mockPersistenceManager);
		
		ProductType subType = aProductType().named("subType").build();
		
		Set<ProductType> productTypes = new HashSet<ProductType>();
		productTypes.add(aProductType().withSubTypes(subType).build());
		
		Set<ProductType> expectedProductTypes = new HashSet<ProductType>(productTypes);
		expectedProductTypes.add(subType);
		
		CatalogService sut = new CatalogServiceImpl(mockPersistenceManager, n4);
		Catalog createdCatalog = sut.publishProductTypes(productTypes);
		
		assertEquals(expectedProductTypes, createdCatalog.getPublishedProductTypes());
		verify(mockPersistenceManager);
	}
	
	@SuppressWarnings(value="unchecked")
	@Test
	public void should_return_listing_pair_of_published_types() {
		Capture<QueryBuilder<ListingPair>> capturedQuery = new Capture<QueryBuilder<ListingPair>>();
		ProductType productType = aProductType().named("Chain").build();
		Catalog catalog = aCatalog().belongingTo(n4).with(productType).build();
		
		List<ListingPair> expectedTypes = new ArrayList<ListingPair>();
		expectedTypes.add(new ListingPair(productType.getId(), productType.getName()));
		
		PersistenceManager mockPersistenceManager = createMock(PersistenceManager.class);
		expect(mockPersistenceManager.find((QueryBuilder<Catalog>)anyObject())).andReturn(catalog);
		expectLastCall().atLeastOnce();
		expect(mockPersistenceManager.findAll(capture(capturedQuery))).andReturn(expectedTypes);
		expectLastCall().once();
		replay(mockPersistenceManager);
		
		CatalogService sut = new CatalogServiceImpl(mockPersistenceManager, n4);
		
		assertEquals(expectedTypes, sut.getPublishedProductTypesLP());
		assertNotNull(capturedQuery.getValue());
		assertEquals(sut.getProductTypeIdsPublished(), capturedQuery.getValue().getWhereParameter("ids").getValue());
		assertEquals("name", capturedQuery.getValue().getOrderArguments().iterator().next().getParam());
		verify(mockPersistenceManager);
	}
	
}
