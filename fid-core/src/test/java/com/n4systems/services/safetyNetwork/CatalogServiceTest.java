package com.n4systems.services.safetyNetwork;


import static com.n4systems.model.builders.CatalogBuilder.*;
import static com.n4systems.model.builders.AssetTypeBuilder.*;
import static com.n4systems.model.builders.TenantBuilder.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.n4systems.model.AssetType;
import org.easymock.Capture;
import org.junit.Test;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Tenant;
import com.n4systems.model.catalog.Catalog;
import com.n4systems.test.helpers.BasicAnswer;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;

public class CatalogServiceTest {

	private Tenant n4;

	public CatalogServiceTest() {
		n4 = aTenant().named("n4").build();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void should_find_catalog_asset_type_ids() {
		AssetType assetType = anAssetType().build();
		Catalog catalog = aCatalog().belongingTo(n4).with(assetType).build();
		
		PersistenceManager mockPersistenceManager = createMock(PersistenceManager.class);
		expect(mockPersistenceManager.find((QueryBuilder<Catalog>)anyObject())).andReturn(catalog);
		expectLastCall().once();
		replay(mockPersistenceManager);
		
		Set<Long> expectedIds = new HashSet<Long>();
		expectedIds.add(assetType.getId());
		
		CatalogService sut = new CatalogServiceImpl(mockPersistenceManager, n4);
		Set<Long> publishedIds = sut.getAssetTypeIdsPublished();
		
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
		Set<Long> publishedIds = sut.getAssetTypeIdsPublished();
		
		assertEquals(expectedIds, publishedIds);
		verify(mockPersistenceManager);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_add_create_catalog_and_publish_asset_types() {
		Capture<Catalog> captureCatalog = new Capture<Catalog>();
		Capture<AssetType> captureAssetType = new Capture<AssetType>();
		
		PersistenceManager mockPersistenceManager = createMock(PersistenceManager.class);
		expect(mockPersistenceManager.find((QueryBuilder<Catalog>)anyObject())).andReturn(null);
		expectLastCall().once();
		expect(mockPersistenceManager.update(capture(captureCatalog))).andAnswer(new BasicAnswer<Catalog>(captureCatalog));
		expect(mockPersistenceManager.reattchAndFetch(capture(captureAssetType), (String)anyObject())).andAnswer(new BasicAnswer<AssetType>(captureAssetType));
		expectLastCall().once();
		replay(mockPersistenceManager);
		
		Set<AssetType> assetTypes = new HashSet<AssetType>();
		assetTypes.add(anAssetType().build());
		
		CatalogService sut = new CatalogServiceImpl(mockPersistenceManager, n4);
		Catalog createdCatalog = sut.publishAssetTypes(assetTypes);
		
		assertEquals(assetTypes, createdCatalog.getPublishedAssetTypes());
		verify(mockPersistenceManager);
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_add_create_catalog_and_publish_asset_types_with_master_asset() {
		Capture<Catalog> captureCatalog = new Capture<Catalog>();
		Capture<AssetType> captureAssetType = new Capture<AssetType>();
		
		PersistenceManager mockPersistenceManager = createMock(PersistenceManager.class);
		expect(mockPersistenceManager.find((QueryBuilder<Catalog>)anyObject())).andReturn(null);
		expectLastCall().once();
		expect(mockPersistenceManager.update(capture(captureCatalog))).andAnswer(new BasicAnswer<Catalog>(captureCatalog));
		expect(mockPersistenceManager.reattchAndFetch(capture(captureAssetType), (String)anyObject())).andAnswer(new BasicAnswer<AssetType>(captureAssetType));
		expectLastCall().once();
		replay(mockPersistenceManager);
		
		AssetType subType = anAssetType().named("subType").build();
		
		Set<AssetType> assetTypes = new HashSet<AssetType>();
		assetTypes.add(anAssetType().withSubTypes(subType).build());
		
		Set<AssetType> expectedAssetTypes = new HashSet<AssetType>(assetTypes);
		expectedAssetTypes.add(subType);
		
		CatalogService sut = new CatalogServiceImpl(mockPersistenceManager, n4);
		Catalog createdCatalog = sut.publishAssetTypes(assetTypes);
		
		assertEquals(expectedAssetTypes, createdCatalog.getPublishedAssetTypes());
		verify(mockPersistenceManager);
	}
	
	@SuppressWarnings(value="unchecked")
	@Test
	public void should_return_listing_pair_of_published_types() {
		Capture<QueryBuilder<ListingPair>> capturedQuery = new Capture<QueryBuilder<ListingPair>>();
		AssetType assetType = anAssetType().named("Chain").build();
		Catalog catalog = aCatalog().belongingTo(n4).with(assetType).build();
		
		List<ListingPair> expectedTypes = new ArrayList<ListingPair>();
		expectedTypes.add(new ListingPair(assetType.getId(), assetType.getName()));
		
		PersistenceManager mockPersistenceManager = createMock(PersistenceManager.class);
		expect(mockPersistenceManager.find((QueryBuilder<Catalog>)anyObject())).andReturn(catalog);
		expectLastCall().atLeastOnce();
		expect(mockPersistenceManager.findAll(capture(capturedQuery))).andReturn(expectedTypes);
		expectLastCall().once();
		replay(mockPersistenceManager);
		
		CatalogService sut = new CatalogServiceImpl(mockPersistenceManager, n4);
		
		assertEquals(expectedTypes, sut.getPublishedAssetTypesLP());
		assertNotNull(capturedQuery.getValue());
		assertEquals(sut.getAssetTypeIdsPublished(), capturedQuery.getValue().getWhereParameter("ids").getValue());
		assertEquals("name", capturedQuery.getValue().getOrderArguments().iterator().next().getParam());
		verify(mockPersistenceManager);
	}
	
}
