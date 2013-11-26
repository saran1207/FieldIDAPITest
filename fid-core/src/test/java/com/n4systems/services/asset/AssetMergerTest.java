package com.n4systems.services.asset;
import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.TenantNotValidForActionException;
import com.n4systems.exceptions.UsedOnMasterEventException;
import com.n4systems.exceptions.asset.AssetTypeMissMatchException;
import com.n4systems.exceptions.asset.DuplicateAssetException;
import com.n4systems.model.*;
import com.n4systems.model.builders.*;
import com.n4systems.model.user.User;
import com.n4systems.services.EventScheduleService;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.util.persistence.QueryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.n4systems.model.builders.AssetBuilder.anAsset;
import static com.n4systems.model.builders.SubAssetBuilder.aSubAsset;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class AssetMergerTest {

	private User user = UserBuilder.aUser().build();
	private Asset winningAsset;
	private Asset losingAsset;
	private SubAsset losingSubAsset;
	private PersistenceManager mockPersistenceManager;
	private EventManager mockEventManager;
	private AssetManager mockAssetManager;
	private EventScheduleService mockEventScheduleService;
	
	
	@Before
	public void setUp() {
		// default assets to merge same tenant and same type, no events.
		winningAsset = anAsset().forTenant(TenantBuilder.n4()).build();
		losingAsset = anAsset().forTenant(TenantBuilder.n4()).ofType(winningAsset.getType()).build();
		
		losingSubAsset = aSubAsset().withMasterAsset(losingAsset).build();
		
		
		// managers mocks  each test is required to put them into reply mode.
		mockPersistenceManager = createMock(PersistenceManager.class);
		mockAssetManager = createMock(AssetManager.class);
		mockEventManager = createMock(EventManager.class);
		mockEventScheduleService = createMock(EventScheduleService.class);
	}
	
	@Test 
	public void should_merge_products_together_with_no_events() {
		
		mockEmptyEventList();
		mockArchiveOfLosingAsset();
		replayMocks();
		
		
		AssetMerger sut = createSystemUnderTest();
		Asset mergedAsset = sut.merge(winningAsset, losingAsset);
		
		assertEquals(winningAsset, mergedAsset);
		verifyMocks();
	}
	
	@Test(expected= AssetTypeMissMatchException.class)
	public void should_fail_to_merge_products_of_different_types() {
		Asset losingAsset = anAsset().ofType(AssetTypeBuilder.anAssetType().named("type 2").build()).build();
		replayMocks();
		
		AssetMerger sut = createSystemUnderTest();
		sut.merge(winningAsset, losingAsset);
		
		verifyMocks();
	}
	
	@Test(expected=TenantNotValidForActionException.class)
	public void should_fail_to_merge_products_for_different_tenants() {
		losingAsset.setTenant(TenantBuilder.aTenant().build());
		
		replayMocks();
		
		AssetMerger sut = createSystemUnderTest();
		sut.merge(winningAsset, losingAsset);
		
		verifyMocks();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_merge_products_together_with_events() {
		List<ThingEvent> eventsOnLosingProduct = new ArrayList<ThingEvent>();
		eventsOnLosingProduct.add(EventBuilder.anEvent().on(losingAsset).withTenant(TenantBuilder.aTenant().build()).build());
		
		mockEventLists(eventsOnLosingProduct, new ArrayList<ThingEvent>());
		
				
        expect(mockEventManager.updateEvent((ThingEvent)eq(eventsOnLosingProduct.get(0)), eq(0L), eq(user.getId()), (FileDataContainer)isNull(), (List<FileAttachment>)isNull())).andReturn(eventsOnLosingProduct.get(0));

		mockArchiveOfLosingAsset();
		
		replayMocks();
		
		AssetMerger sut = createSystemUnderTest();
		Asset mergedAsset = sut.merge(winningAsset, losingAsset);
		
		assertEquals(winningAsset, mergedAsset);
		assertEquals(winningAsset, eventsOnLosingProduct.get(0).getAsset());
		verifyMocks();
	}

	@SuppressWarnings("unchecked")
	@Test 
	public void should_merge_subasset_together() {
		SubEvent sub = SubEventBuilder.aSubEvent("tom").withAsset(losingAsset).build();
		List<SubEvent> subEvents = new ArrayList<SubEvent>();
		subEvents.add(sub);
		
		List<ThingEvent> masterEvents = new ArrayList<ThingEvent>();
		masterEvents.add(EventBuilder.anEvent().withSubEvents(subEvents).build());
	
		mockEventLists(new ArrayList<ThingEvent>(), masterEvents);
		mockArchiveOfLosingAsset();
		
		
		try {
			expect(mockEventManager.updateEvent(eq(masterEvents.get(0)), eq(0L), eq(user.getId()), (FileDataContainer)isNull(), (List<FileAttachment>)isNull())).andReturn(masterEvents.get(0));
		} catch (Exception e) {
			fail("should not throw exception " + e.getMessage());
		}
		
		replayMocks();
		
		AssetMerger sut = createSystemUnderTest();
		Asset mergedAsset = sut.merge(winningAsset, losingAsset);
		
		assertEquals(winningAsset, mergedAsset);
		assertEquals(winningAsset, sub.getAsset());
		verifyMocks();
	}
	
	@Test(expected= DuplicateAssetException.class)
	public void should_not_merge_products_when_they_are_the_same_product() {
		replayMocks();
		
		AssetMerger sut = createSystemUnderTest();
		sut.merge(winningAsset, winningAsset);
		
		verifyMocks();
	}
	
	@Test
	public void should_merge_master_asset_with_sub_assets() throws Exception {
		
		losingAsset.getSubAssets().add(losingSubAsset);
		
		mockEmptyEventList();
		mockArchiveOfLosingAssetWithSubAsset();
		replayMocks();
		
		
		AssetMerger assetMerger = createSystemUnderTest();
		Asset mergedAsset = assetMerger.merge(winningAsset, losingAsset);
		
		assertEquals(winningAsset, mergedAsset);
		verifyMocks();
		
	}
	
	@SuppressWarnings("unchecked")
	private void mockEventLists(List<ThingEvent> events, List<ThingEvent> masterEvents) {
		expect(mockPersistenceManager.findAll((QueryBuilder<ThingEvent>)anyObject())).andReturn(events);
		expect(mockPersistenceManager.passThroughFindAll(contains("SELECT"), (Map<String,Object>)anyObject())).andReturn(new ArrayList<Object>(masterEvents));
	}
	
	private void mockEmptyEventList() {
		mockEventLists(new ArrayList<ThingEvent>(), new ArrayList<ThingEvent>());
	}

	
	private void mockArchiveOfLosingAsset() {
		try {
			expect(mockAssetManager.findSubAssetsForAsset(losingAsset)).andReturn(new ArrayList<SubAsset>());
			expect(mockAssetManager.archive((Asset)eq(losingAsset), (User)anyObject())).andReturn(losingAsset);
		} catch (UsedOnMasterEventException e) {
			fail("mock should not throw exception");
		}
	}
	
	private void mockArchiveOfLosingAssetWithSubAsset() {
		try {
			expect(mockAssetManager.findSubAssetsForAsset(losingAsset)).andReturn(Collections.singletonList(losingSubAsset));
			expect(mockAssetManager.archive((Asset)eq(losingAsset), (User)anyObject())).andReturn(losingAsset);
			expect(mockAssetManager.archive((Asset)eq(losingSubAsset.getAsset()), (User)anyObject())).andReturn(losingSubAsset.getAsset());
		} catch (UsedOnMasterEventException e) {
			fail("mock should not throw exception");
		}
	}

	private void verifyMocks() {
		verify(mockPersistenceManager);
		verify(mockAssetManager);
		verify(mockEventManager);
		verify(mockEventScheduleService);
	}
	
	private void replayMocks() {
		replay(mockAssetManager);
		replay(mockPersistenceManager);
		replay(mockEventManager);
		replay(mockEventScheduleService);
	}
	
	private AssetMerger createSystemUnderTest() {
		return new AssetMerger(mockPersistenceManager, mockAssetManager, mockEventManager, mockEventScheduleService, user);
	}
}
