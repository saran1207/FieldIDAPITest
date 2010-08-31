package com.n4systems.services.product;
import static com.n4systems.model.builders.ProductBuilder.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;


import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.exceptions.TenantNotValidForActionException;
import com.n4systems.exceptions.UsedOnMasterInspectionException;
import com.n4systems.exceptions.product.DuplicateProductException;
import com.n4systems.exceptions.product.ProductTypeMissMatchException;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.Product;
import com.n4systems.model.SubInspection;
import com.n4systems.model.builders.InspectionBuilder;
import com.n4systems.model.builders.InspectionScheduleBuilder;
import com.n4systems.model.builders.ProductTypeBuilder;
import com.n4systems.model.builders.SubInspectionBuilder;
import com.n4systems.model.builders.TenantBuilder;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.user.User;
import com.n4systems.services.InspectionScheduleService;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.util.persistence.QueryBuilder;


public class ProductMergerTest {

	private User user = UserBuilder.aUser().build();
	private Product winningProduct;
	private Product losingProduct;
	private PersistenceManager mockPersistenceManager;
	private InspectionManager mockInspectionManager;
	private ProductManager mockProductManager; 
	private InspectionScheduleService mockInspectionScheduleService;
	
	
	@Before
	public void setUp() {
		// default products to merge same tenant and same type, no inspections.
		winningProduct = aProduct().forTenant(TenantBuilder.n4()).build();
		losingProduct = aProduct().forTenant(TenantBuilder.n4()).ofType(winningProduct.getType()).build();
		
		
		// managers mocks  each test is required to put them into reply mode.
		mockPersistenceManager = createMock(PersistenceManager.class);
		mockProductManager = createMock(ProductManager.class);
		mockInspectionManager = createMock(InspectionManager.class);
		mockInspectionScheduleService = createMock(InspectionScheduleService.class);
	}
	
	@Test 
	public void should_merge_products_together_with_no_inspections() {
		
		mockEmptInspectionList();		
		mockArchiveOfLosingProduct();
		replayMocks();
		
		
		ProductMerger sut = createSystemUnderTest();
		Product mergedProduct = sut.merge(winningProduct, losingProduct);
		
		assertEquals(winningProduct, mergedProduct);
		verifyMocks();
	}

	
	
	
	@Test(expected=ProductTypeMissMatchException.class) 
	public void should_fail_to_merge_products_of_different_types() {
		Product losingProduct = aProduct().ofType(ProductTypeBuilder.aProductType().named("type 2").build()).build();
		replayMocks();
		
		ProductMerger sut = createSystemUnderTest();
		sut.merge(winningProduct, losingProduct);
		
		verifyMocks();
	}
	
	
	@Test(expected=TenantNotValidForActionException.class)
	public void should_fail_to_merge_products_for_different_tenants() {
		losingProduct.setTenant(TenantBuilder.aTenant().build());
		
		replayMocks();
		
		ProductMerger sut = createSystemUnderTest();
		sut.merge(winningProduct, losingProduct);
		
		verifyMocks();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_merge_products_together_with_inspections() {
		List<Inspection> inspectionsOnLosingProduct = new ArrayList<Inspection>(); 
		inspectionsOnLosingProduct.add(InspectionBuilder.anInspection().on(losingProduct).build());
		
		mockInspectionLists(inspectionsOnLosingProduct, new ArrayList<Inspection>());
		
				
		try {
			expect(mockInspectionManager.updateInspection((Inspection)eq(inspectionsOnLosingProduct.get(0)), eq(user.getId()), (FileDataContainer)isNull(), (List<FileAttachment>)isNull())).andReturn(inspectionsOnLosingProduct.get(0));
		} catch (Exception e1) {
			fail("should not throw exception");
		} 
		
		mockArchiveOfLosingProduct();
		
		replayMocks();
		
		ProductMerger sut = createSystemUnderTest();
		Product mergedProduct = sut.merge(winningProduct, losingProduct);
		
		assertEquals(winningProduct, mergedProduct);
		assertEquals(winningProduct, inspectionsOnLosingProduct.get(0).getProduct());
		verifyMocks();
		
	}

	
	@SuppressWarnings("unchecked")
	@Test
	public void should_merge_products_together_with_inspections_that_have_a_schedule_on_it() {
		List<Inspection> inspectionsOnLosingProduct = new ArrayList<Inspection>(); 
		inspectionsOnLosingProduct.add(InspectionBuilder.anInspection().on(losingProduct).build());
		
		// puts the schedule onto the inspection.
		InspectionScheduleBuilder.aCompletedInspectionSchedule().completedDoing(inspectionsOnLosingProduct.get(0)).product(inspectionsOnLosingProduct.get(0).getProduct()).build();
		
		mockInspectionLists(inspectionsOnLosingProduct, new ArrayList<Inspection>());
				
		try {
			expect(mockInspectionManager.updateInspection((Inspection)eq(inspectionsOnLosingProduct.get(0)), eq(user.getId()), (FileDataContainer)isNull(), (List<FileAttachment>)isNull())).andReturn(inspectionsOnLosingProduct.get(0));
		} catch (Exception e1) {
			fail("should not throw exception");
		} 
		
		mockArchiveOfLosingProduct();
		
		replayMocks();
		
		ProductMerger sut = createSystemUnderTest();
		Product mergedProduct = sut.merge(winningProduct, losingProduct);
		
		assertEquals(winningProduct, mergedProduct);
		assertEquals(winningProduct, inspectionsOnLosingProduct.get(0).getProduct());
		verifyMocks();
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Test 
	public void should_merge_subproduct_together() {
		SubInspection sub = SubInspectionBuilder.aSubInspection("tom").withProduct(losingProduct).build();
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
		
		ProductMerger sut = createSystemUnderTest();
		Product mergedProduct = sut.merge(winningProduct, losingProduct);
		
		assertEquals(winningProduct, mergedProduct);
		assertEquals(winningProduct, sub.getProduct());
		verifyMocks();
	}

	
	
	@Test(expected=DuplicateProductException.class)
	public void should_not_merge_products_when_they_are_the_same_product() {
		replayMocks();
		
		ProductMerger sut = createSystemUnderTest();
		sut.merge(winningProduct, winningProduct);
		
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
			expect(mockProductManager.archive((Product)eq(losingProduct), (User)anyObject())).andReturn(losingProduct);
		} catch (UsedOnMasterInspectionException e) {
			fail("mock should not throw exception");
		}
	}

	private void verifyMocks() {
		verify(mockPersistenceManager);
		verify(mockProductManager);
		verify(mockInspectionManager);
		verify(mockInspectionScheduleService);
	}
	
	private void replayMocks() {
		replay(mockProductManager);
		replay(mockPersistenceManager);
		replay(mockInspectionManager);
		replay(mockInspectionScheduleService);
	}
	
	private ProductMerger createSystemUnderTest() {
		return new ProductMerger(mockPersistenceManager, mockProductManager, mockInspectionManager, mockInspectionScheduleService, user);
	}
}
