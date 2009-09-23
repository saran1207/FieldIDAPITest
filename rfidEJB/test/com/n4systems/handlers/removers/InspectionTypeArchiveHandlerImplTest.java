package com.n4systems.handlers.removers;

import static com.n4systems.model.builders.InspectionTypeBuilder.*;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.handlers.TestUsesTransactionBase;
import com.n4systems.handlers.remover.AssociatedInspectionTypeListDeleteHandler;
import com.n4systems.handlers.remover.CatalogElementRemovalHandler;
import com.n4systems.handlers.remover.InspectionListArchiveHandlerImp;
import com.n4systems.handlers.remover.InspectionTypeArchiveHandler;
import com.n4systems.handlers.remover.InspectionTypeArchiveHandlerImpl;
import com.n4systems.handlers.remover.NotificationSettingDeleteHandler;
import com.n4systems.handlers.remover.summary.AssociatedInspectionTypeDeleteSummary;
import com.n4systems.handlers.remover.summary.InspectionArchiveSummary;
import com.n4systems.handlers.remover.summary.InspectionTypeArchiveSummary;
import com.n4systems.handlers.remover.summary.NotificationSettingDeleteSummary;
import com.n4systems.model.InspectionType;
import com.n4systems.model.inspectiontype.InspectionTypeSaver;


public class InspectionTypeArchiveHandlerImplTest extends TestUsesTransactionBase {


	@Before
	public void setup() {
		mockTransaction();
	}

	
	@Test
	public void should_be_able_to_archive_inspection_type_not_used_by_anything() {
		InspectionType inspectionTypeToDelete = anInspectionType().build(); 
		
		InspectionListArchiveHandlerImp mockListDeleter = createNiceMock(InspectionListArchiveHandlerImp.class);
		expect(mockListDeleter.setInspectionType(inspectionTypeToDelete)).andReturn(mockListDeleter);
		expect(mockListDeleter.summary(mockTransaction)).andReturn(new InspectionArchiveSummary());
		replay(mockListDeleter);
		
		AssociatedInspectionTypeListDeleteHandler mockAssociatedInspectionTypeListDeleteHandler = createMock(AssociatedInspectionTypeListDeleteHandler.class);
		
		expect(mockAssociatedInspectionTypeListDeleteHandler.setInspectionType(inspectionTypeToDelete)).andReturn(mockAssociatedInspectionTypeListDeleteHandler);
		expect(mockAssociatedInspectionTypeListDeleteHandler.summary(mockTransaction)).andReturn(new AssociatedInspectionTypeDeleteSummary());
		replay(mockAssociatedInspectionTypeListDeleteHandler);
		
		CatalogElementRemovalHandler mockCatalogElementRemovalHandler = createMock(CatalogElementRemovalHandler.class);
		replay(mockCatalogElementRemovalHandler);
		
		NotificationSettingDeleteHandler mockNotificationSettingDeleteHandler = createMock(NotificationSettingDeleteHandler.class);
		expect(mockNotificationSettingDeleteHandler.forInspectionType(inspectionTypeToDelete)).andReturn(mockNotificationSettingDeleteHandler);
		expect(mockNotificationSettingDeleteHandler.summary(mockTransaction)).andReturn(new NotificationSettingDeleteSummary());
		replay(mockNotificationSettingDeleteHandler);
		
		InspectionTypeArchiveHandler sut = new InspectionTypeArchiveHandlerImpl(null, mockListDeleter,  mockAssociatedInspectionTypeListDeleteHandler, mockCatalogElementRemovalHandler, mockNotificationSettingDeleteHandler);
		
		
		InspectionTypeArchiveSummary summary = sut.forInspectionType(inspectionTypeToDelete).summary(mockTransaction);
		
		assertTrue(summary.canBeRemoved());
		assertEquals(new Long(0), summary.getRemoveFromProductTypes());
		verify(mockListDeleter);
		verify(mockAssociatedInspectionTypeListDeleteHandler);
		verify(mockCatalogElementRemovalHandler);
		verify(mockNotificationSettingDeleteHandler);
	}
	
	@Test
	public void should_archive_inspection_type_not_used_by_anything() {
		InspectionType inspectionTypeToDelete = anInspectionType().build();
		
		InspectionTypeSaver mockInspectionTypeSaver = createMock(InspectionTypeSaver.class);
		expect(mockInspectionTypeSaver.update(mockTransaction, inspectionTypeToDelete)).andReturn(inspectionTypeToDelete);
		replay(mockInspectionTypeSaver);
		
		
		InspectionListArchiveHandlerImp mockListDeleter = createMock(InspectionListArchiveHandlerImp.class);
		expect(mockListDeleter.setInspectionType(inspectionTypeToDelete)).andReturn(mockListDeleter);
		mockListDeleter.remove(mockTransaction);
		replay(mockListDeleter);
		
		AssociatedInspectionTypeListDeleteHandler mockAssociatedInspectionTypeListDeleteHandler = createMock(AssociatedInspectionTypeListDeleteHandler.class);
		expect(mockAssociatedInspectionTypeListDeleteHandler.setInspectionType(inspectionTypeToDelete)).andReturn(mockAssociatedInspectionTypeListDeleteHandler);
		mockAssociatedInspectionTypeListDeleteHandler.remove(mockTransaction);
		replay(mockAssociatedInspectionTypeListDeleteHandler);
		
		CatalogElementRemovalHandler mockCatalogElementRemovalHandler = createMock(CatalogElementRemovalHandler.class);
		expect(mockCatalogElementRemovalHandler.setInspectionType(inspectionTypeToDelete)).andReturn(mockCatalogElementRemovalHandler);
		mockCatalogElementRemovalHandler.cleanUp(mockTransaction);
		replay(mockCatalogElementRemovalHandler);
		
		NotificationSettingDeleteHandler mockNotificationSettingDeleteHandler = createMock(NotificationSettingDeleteHandler.class);
		expect(mockNotificationSettingDeleteHandler.forInspectionType(inspectionTypeToDelete)).andReturn(mockNotificationSettingDeleteHandler);
		mockNotificationSettingDeleteHandler.remove(mockTransaction);
		replay(mockNotificationSettingDeleteHandler);
		
		InspectionTypeArchiveHandler sut = new InspectionTypeArchiveHandlerImpl(mockInspectionTypeSaver, mockListDeleter, mockAssociatedInspectionTypeListDeleteHandler, mockCatalogElementRemovalHandler, mockNotificationSettingDeleteHandler);
		
		
		sut.forInspectionType(inspectionTypeToDelete).remove(mockTransaction);
		
		verify(mockInspectionTypeSaver);
		verify(mockAssociatedInspectionTypeListDeleteHandler);
		verify(mockListDeleter);
		verify(mockCatalogElementRemovalHandler);
		verify(mockNotificationSettingDeleteHandler);
	}

	
	@Test
	public void should_be_able_to_archive_inspection_type_tied_to_1_product_type() {
		InspectionType inspectionTypeToDelete = anInspectionType().build(); 
		
		InspectionListArchiveHandlerImp mockInspectionListDeleter = createMock(InspectionListArchiveHandlerImp.class);
		expect(mockInspectionListDeleter.setInspectionType(inspectionTypeToDelete)).andReturn(mockInspectionListDeleter);
		expect(mockInspectionListDeleter.summary(mockTransaction)).andReturn(new InspectionArchiveSummary());
		replay(mockInspectionListDeleter);
		
		AssociatedInspectionTypeListDeleteHandler mockAssociatedInspectionTypeListDeleteHandler = createMock(AssociatedInspectionTypeListDeleteHandler.class);
		expect(mockAssociatedInspectionTypeListDeleteHandler.setInspectionType(inspectionTypeToDelete)).andReturn(mockAssociatedInspectionTypeListDeleteHandler);
		expect(mockAssociatedInspectionTypeListDeleteHandler.summary(mockTransaction)).andReturn(new AssociatedInspectionTypeDeleteSummary(1L, 0L, 0L));
		replay(mockAssociatedInspectionTypeListDeleteHandler);
		
		CatalogElementRemovalHandler mockCatalogElementRemovalHandler = createMock(CatalogElementRemovalHandler.class);
		replay(mockCatalogElementRemovalHandler);
		
		NotificationSettingDeleteHandler mockNotificationSettingDeleteHandler = createMock(NotificationSettingDeleteHandler.class);
		expect(mockNotificationSettingDeleteHandler.forInspectionType(inspectionTypeToDelete)).andReturn(mockNotificationSettingDeleteHandler);
		expect(mockNotificationSettingDeleteHandler.summary(mockTransaction)).andReturn(new NotificationSettingDeleteSummary());
		replay(mockNotificationSettingDeleteHandler);
		
		InspectionTypeArchiveHandler sut = new InspectionTypeArchiveHandlerImpl(null, mockInspectionListDeleter, mockAssociatedInspectionTypeListDeleteHandler, mockCatalogElementRemovalHandler, mockNotificationSettingDeleteHandler);
		
		InspectionTypeArchiveSummary summary = sut.forInspectionType(inspectionTypeToDelete).summary(mockTransaction);
		
		assertTrue(summary.canBeRemoved());
		assertEquals(new Long(1), summary.getRemoveFromProductTypes());
		assertEquals(new Long(0), summary.getInspectionArchiveSummary().getDeleteInspections());
		verify(mockInspectionListDeleter);
		verify(mockAssociatedInspectionTypeListDeleteHandler);
		verify(mockCatalogElementRemovalHandler);
		verify(mockNotificationSettingDeleteHandler);
	}
	
	@Test
	public void should_be_to_archive_inspection_type_tied_to_1_product_type() {
		InspectionType inspectionTypeToDelete = anInspectionType().build(); 
		
		
		InspectionTypeSaver mockInspectionTypeSaver = createMock(InspectionTypeSaver.class);
		expect(mockInspectionTypeSaver.update(mockTransaction, inspectionTypeToDelete)).andReturn(inspectionTypeToDelete);
		replay(mockInspectionTypeSaver);
		
		InspectionListArchiveHandlerImp mockListDeleter = createMock(InspectionListArchiveHandlerImp.class);
		expect(mockListDeleter.setInspectionType(inspectionTypeToDelete)).andReturn(mockListDeleter);
		mockListDeleter.remove(mockTransaction);
		replay(mockListDeleter);
		
		AssociatedInspectionTypeListDeleteHandler mockAssociatedInspectionListTypeDeleteHandler = createMock(AssociatedInspectionTypeListDeleteHandler.class);
		expect(mockAssociatedInspectionListTypeDeleteHandler.setInspectionType(inspectionTypeToDelete)).andReturn(mockAssociatedInspectionListTypeDeleteHandler);
		mockAssociatedInspectionListTypeDeleteHandler.remove(mockTransaction);
		replay(mockAssociatedInspectionListTypeDeleteHandler);
		
		CatalogElementRemovalHandler mockCatalogElementRemovalHandler = createMock(CatalogElementRemovalHandler.class);
		expect(mockCatalogElementRemovalHandler.setInspectionType(inspectionTypeToDelete)).andReturn(mockCatalogElementRemovalHandler);
		mockCatalogElementRemovalHandler.cleanUp(mockTransaction);
		replay(mockCatalogElementRemovalHandler);
		
		NotificationSettingDeleteHandler mockNotificationSettingDeleteHandler = createMock(NotificationSettingDeleteHandler.class);
		expect(mockNotificationSettingDeleteHandler.forInspectionType(inspectionTypeToDelete)).andReturn(mockNotificationSettingDeleteHandler);
		mockNotificationSettingDeleteHandler.remove(mockTransaction);
		replay(mockNotificationSettingDeleteHandler);
		
		InspectionTypeArchiveHandler sut = new InspectionTypeArchiveHandlerImpl(mockInspectionTypeSaver, mockListDeleter, mockAssociatedInspectionListTypeDeleteHandler, mockCatalogElementRemovalHandler, mockNotificationSettingDeleteHandler);
		
		sut.forInspectionType(inspectionTypeToDelete).remove(mockTransaction);
		
		verify(mockInspectionTypeSaver);
		verify(mockListDeleter);
		verify(mockAssociatedInspectionListTypeDeleteHandler);
		verify(mockCatalogElementRemovalHandler);
		verify(mockNotificationSettingDeleteHandler);
	}
	
	@Test
	public void should_be_able_to_archive_inspection_type_tied_used_in_a_master_inspection() {
		InspectionType inspectionTypeToDelete = anInspectionType().build(); 
		
	
		InspectionListArchiveHandlerImp mockListDeleter = createMock(InspectionListArchiveHandlerImp.class);
		expect(mockListDeleter.setInspectionType(inspectionTypeToDelete)).andReturn(mockListDeleter);
		expect(mockListDeleter.summary(mockTransaction)).andReturn(new InspectionArchiveSummary().setInspectionsPartOfMaster(5L));
		replay(mockListDeleter);
		
		AssociatedInspectionTypeListDeleteHandler mockAssociatedInspectionListTypeDeleteHandler = createMock(AssociatedInspectionTypeListDeleteHandler.class);
		expect(mockAssociatedInspectionListTypeDeleteHandler.setInspectionType(inspectionTypeToDelete)).andReturn(mockAssociatedInspectionListTypeDeleteHandler);
		expect(mockAssociatedInspectionListTypeDeleteHandler.summary(mockTransaction)).andReturn(new AssociatedInspectionTypeDeleteSummary(1L, 0L, 0L));
		replay(mockAssociatedInspectionListTypeDeleteHandler);
		
		NotificationSettingDeleteHandler mockNotificationSettingDeleteHandler = createMock(NotificationSettingDeleteHandler.class);
		expect(mockNotificationSettingDeleteHandler.forInspectionType(inspectionTypeToDelete)).andReturn(mockNotificationSettingDeleteHandler);
		expect(mockNotificationSettingDeleteHandler.summary(mockTransaction)).andReturn(new NotificationSettingDeleteSummary());
		replay(mockNotificationSettingDeleteHandler);
		
		InspectionTypeArchiveHandler sut = new InspectionTypeArchiveHandlerImpl(null, mockListDeleter, mockAssociatedInspectionListTypeDeleteHandler, null, mockNotificationSettingDeleteHandler);
		
		InspectionTypeArchiveSummary summary = sut.forInspectionType(inspectionTypeToDelete).summary(mockTransaction);
		
		assertFalse(summary.canBeRemoved());
		assertEquals(new Long(5), summary.getInspectionArchiveSummary().getInspectionsPartOfMaster());
		verify(mockListDeleter);
		verify(mockAssociatedInspectionListTypeDeleteHandler);
		verify(mockNotificationSettingDeleteHandler);
	}
	
}
