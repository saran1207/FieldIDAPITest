package com.n4systems.services.asset;
import static com.n4systems.model.builders.AssetBuilder.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.n4systems.ejb.AssetManager;
import com.n4systems.exceptions.asset.AssetTypeMissMatchException;
import com.n4systems.exceptions.asset.DuplicateAssetException;
import com.n4systems.model.Asset;
import com.n4systems.model.builders.AssetTypeBuilder;
import org.junit.Before;
import org.junit.Test;


import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.TenantNotValidForActionException;
import com.n4systems.exceptions.UsedOnMasterInspectionException;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.SubInspection;
import com.n4systems.model.builders.InspectionBuilder;
import com.n4systems.model.builders.InspectionScheduleBuilder;
import com.n4systems.model.builders.SubInspectionBuilder;
import com.n4systems.model.builders.TenantBuilder;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.user.User;
import com.n4systems.services.InspectionScheduleService;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.util.persistence.QueryBuilder;


public class AssetMergerTest {

	private User user = UserBuilder.aUser().build();
	private Asset winningAsset;
	private Asset losingAsset;
	private PersistenceManager mockPersistenceManager;
	private InspectionManager mockInspectionManager;
	private AssetManager mockAssetManager;
	private InspectionScheduleService mockInspectionScheduleService;
	
	
	@Before
	public void setUp() {
		// default assets to merge same tenant and same type, no inspections.
		winningAsset = anAsset().forTenant(TenantBuilder.n4()).build();
		losingAsset = anAsset().forTenant(TenantBuilder.n4()).ofType(winningAsset.getType()).build();
		
		
		// managers mocks  each test is required to put them into reply mode.
		mockPersistenceManager = createMock(PersistenceManager.class);
		mockAssetManager = createMock(AssetManager.class);
		mockInspectionManager = createMock(InspectionManager.class);
		mockInspectionScheduleService = createMock(InspectionScheduleService.class);
	}
	
	@Test 
	public void should_merge_products_together_with_no_inspections() {
		
		mockEmptInspectionList();		
		mockArchiveOfLosingProduct();
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
	public void should_merge_products_together_with_inspections() {
		List<Inspection> inspectionsOnLosingProduct = new ArrayList<Inspection>(); 
		inspectionsOnLosingProduct.add(InspectionBuilder.anInspection().on(losingAsset).build());
		
		mockInspectionLists(inspectionsOnLosingProduct, new ArrayList<Inspection>());
		
				
		try {
			expect(mockInspectionManager.updateInspection((Inspection)eq(inspectionsOnLosingProduct.get(0)), eq(user.getId()), (FileDataContainer)isNull(), (List<FileAttachment>)isNull())).andReturn(inspectionsOnLosingProduct.get(0));
		} catch (Exception e1) {
			fail("should not throw exception");
		} 
		
		mockArchiveOfLosingProduct();
		
		replayMocks();
		
		AssetMerger sut = createSystemUnderTest();
		Asset mergedAsset = sut.merge(winningAsset, losingAsset);
		
		assertEquals(winningAsset, mergedAsset);
		assertEquals(winningAsset, inspectionsOnLosingProduct.get(0).getAsset());
		verifyMocks();
		
	}

	
	@SuppressWarnings("unchecked")
	@Test
	public void should_merge_products_together_with_inspections_that_have_a_schedule_on_it() {
		List<Inspection> inspectionsOnLosingProduct = new ArrayList<Inspection>(); 
		inspectionsOnLosingProduct.add(InspectionBuilder.anInspection().on(losingAsset).build());
		
		// puts the schedule onto the inspection.
		InspectionScheduleBuilder.aCompletedInspectionSchedule().completedDoing(inspectionsOnLosingProduct.get(0)).asset(inspectionsOnLosingProduct.get(0).getAsset()).build();
		
		mockInspectionLists(inspectionsOnLosingProduct, new ArrayList<Inspection>());
				
		try {
			expect(mockInspectionManager.updateInspection((Inspection)eq(inspectionsOnLosingProduct.get(0)), eq(user.getId()), (FileDataContainer)isNull(), (List<FileAttachment>)isNull())).andReturn(inspectionsOnLosingProduct.get(0));
		} catch (Exception e1) {
			fail("should not throw exception");
		} 
		
		mockArchiveOfLosingProduct();
		
		replayMocks();
		
		AssetMerger sut = createSystemUnderTest();
		Asset mergedAsset = sut.merge(winningAsset, losingAsset);
		
		assertEquals(winningAsset, mergedAsset);
		assertEquals(winningAsset, inspectionsOnLosingProduct.get(0).getAsset());
		verifyMocks();
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Test 
	public void should_merge_subasset_together() {
		SubInspection sub = SubInspectionBuilder.aSubInspection("tom").withAsset(losingAsset).build();
		List<SubInspection> subInspections = new ArrayList<SubInspection>();
		subInspections.add(sub);
		
		List<Inspection> masterInspections = new ArrayList<Inspection>();
		masterInspections.add(InspectionBuilder.anInspection().withSubInspections(subInspections).build());
	
		mockInspectionLists(new ArrayList<Inspection>(), masterInspections);
		mockArchiveOfLosingProduct();
		
		
		try {
			expect(mockInspectionManager.updateInspection(eq(masterInspections.get(0)), eq(user.getId()), (FileDataContainer)isNull(), (List<FileAttachment>)isNull())).andReturn(masterInspections.get(0));
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
	
	@SuppressWarnings("unchecked")
	private void mockInspectionLists(List<Inspection> inspections, List<Inspection> masterInspections) {
		expect(mockPersistenceManager.findAll((QueryBuilder<Inspection>)anyObject())).andReturn(inspections);
		for (Inspection inspection : inspections) {
			if (inspection.getSchedule() != null) {
				expect(mockInspectionScheduleService.updateSchedule((InspectionSchedule)eq(inspection.getSchedule()))).andReturn(inspection.getSchedule());
			}
		}
		expect(mockPersistenceManager.passThroughFindAll(contains("SELECT"), (Map<String,Object>)anyObject())).andReturn(new ArrayList<Object>(masterInspections));
		
		for (Inspection inspection : masterInspections) {
			if (inspection.getSchedule() != null) {
				expect(mockInspectionScheduleService.updateSchedule((InspectionSchedule)eq(inspection.getSchedule()))).andReturn(inspection.getSchedule());
			}
		}
	}
	
	private void mockEmptInspectionList() {
		mockInspectionLists(new ArrayList<Inspection>(), new ArrayList<Inspection>());
	}

	
	private void mockArchiveOfLosingProduct() {
		try {
			expect(mockAssetManager.archive((Asset)eq(losingAsset), (User)anyObject())).andReturn(losingAsset);
		} catch (UsedOnMasterInspectionException e) {
			fail("mock should not throw exception");
		}
	}

	private void verifyMocks() {
		verify(mockPersistenceManager);
		verify(mockAssetManager);
		verify(mockInspectionManager);
		verify(mockInspectionScheduleService);
	}
	
	private void replayMocks() {
		replay(mockAssetManager);
		replay(mockPersistenceManager);
		replay(mockInspectionManager);
		replay(mockInspectionScheduleService);
	}
	
	private AssetMerger createSystemUnderTest() {
		return new AssetMerger(mockPersistenceManager, mockAssetManager, mockInspectionManager, mockInspectionScheduleService, user);
	}
}
