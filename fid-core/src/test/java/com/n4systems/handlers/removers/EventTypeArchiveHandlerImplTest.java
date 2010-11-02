package com.n4systems.handlers.removers;

import static com.n4systems.model.builders.EventTypeBuilder.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import com.n4systems.handlers.remover.AssociatedEventTypeListDeleteHandler;
import com.n4systems.handlers.remover.EventListArchiveHandlerImp;
import com.n4systems.handlers.remover.EventTypeArchiveHandler;
import com.n4systems.handlers.remover.EventTypeArchiveHandlerImpl;
import com.n4systems.handlers.remover.summary.EventArchiveSummary;
import com.n4systems.handlers.remover.summary.EventTypeArchiveSummary;
import com.n4systems.model.EventType;
import com.n4systems.model.inspectiontype.EventTypeSaver;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.handlers.TestUsesTransactionBase;
import com.n4systems.handlers.remover.CatalogElementRemovalHandler;
import com.n4systems.handlers.remover.NotificationSettingDeleteHandler;
import com.n4systems.handlers.remover.summary.AssociatedEventTypeDeleteSummary;
import com.n4systems.handlers.remover.summary.NotificationSettingDeleteSummary;


public class EventTypeArchiveHandlerImplTest extends TestUsesTransactionBase {


	@Before
	public void setup() {
		mockTransaction();
	}

	
	@Test
	public void should_be_able_to_archive_inspection_type_not_used_by_anything() {
		EventType eventTypeToDelete = anEventType().build();
		
		EventListArchiveHandlerImp mockListDeleter = createNiceMock(EventListArchiveHandlerImp.class);
		expect(mockListDeleter.setEventType(eventTypeToDelete)).andReturn(mockListDeleter);
		expect(mockListDeleter.summary(mockTransaction)).andReturn(new EventArchiveSummary());
		replay(mockListDeleter);
		
		AssociatedEventTypeListDeleteHandler mockAssociatedEventTypeListDeleteHandler = createMock(AssociatedEventTypeListDeleteHandler.class);
		
		expect(mockAssociatedEventTypeListDeleteHandler.setEventType(eventTypeToDelete)).andReturn(mockAssociatedEventTypeListDeleteHandler);
		expect(mockAssociatedEventTypeListDeleteHandler.summary(mockTransaction)).andReturn(new AssociatedEventTypeDeleteSummary());
		replay(mockAssociatedEventTypeListDeleteHandler);
		
		CatalogElementRemovalHandler mockCatalogElementRemovalHandler = createMock(CatalogElementRemovalHandler.class);
		replay(mockCatalogElementRemovalHandler);
		
		NotificationSettingDeleteHandler mockNotificationSettingDeleteHandler = createMock(NotificationSettingDeleteHandler.class);
		expect(mockNotificationSettingDeleteHandler.forEventType(eventTypeToDelete)).andReturn(mockNotificationSettingDeleteHandler);
		expect(mockNotificationSettingDeleteHandler.summary(mockTransaction)).andReturn(new NotificationSettingDeleteSummary());
		replay(mockNotificationSettingDeleteHandler);
		
		EventTypeArchiveHandler sut = new EventTypeArchiveHandlerImpl(null, mockListDeleter, mockAssociatedEventTypeListDeleteHandler, mockCatalogElementRemovalHandler, mockNotificationSettingDeleteHandler);
		
		
		EventTypeArchiveSummary summary = sut.forEventType(eventTypeToDelete).summary(mockTransaction);
		
		assertTrue(summary.canBeRemoved());
		assertEquals(new Long(0), summary.getRemoveFromAssetTypes());
		verify(mockListDeleter);
		verify(mockAssociatedEventTypeListDeleteHandler);
		verify(mockCatalogElementRemovalHandler);
		verify(mockNotificationSettingDeleteHandler);
	}
	
	@Test
	public void should_archive_inspection_type_not_used_by_anything() {
		EventType eventTypeToDelete = anEventType().build();
		
		EventTypeSaver mockEventTypeSaver = createMock(EventTypeSaver.class);
		expect(mockEventTypeSaver.update(mockTransaction, eventTypeToDelete)).andReturn(eventTypeToDelete);
		replay(mockEventTypeSaver);
		
		
		EventListArchiveHandlerImp mockListDeleter = createMock(EventListArchiveHandlerImp.class);
		expect(mockListDeleter.setEventType(eventTypeToDelete)).andReturn(mockListDeleter);
		mockListDeleter.remove(mockTransaction);
		replay(mockListDeleter);
		
		AssociatedEventTypeListDeleteHandler mockAssociatedEventTypeListDeleteHandler = createMock(AssociatedEventTypeListDeleteHandler.class);
		expect(mockAssociatedEventTypeListDeleteHandler.setEventType(eventTypeToDelete)).andReturn(mockAssociatedEventTypeListDeleteHandler);
		mockAssociatedEventTypeListDeleteHandler.remove(mockTransaction);
		replay(mockAssociatedEventTypeListDeleteHandler);
		
		CatalogElementRemovalHandler mockCatalogElementRemovalHandler = createMock(CatalogElementRemovalHandler.class);
		expect(mockCatalogElementRemovalHandler.setEventType(eventTypeToDelete)).andReturn(mockCatalogElementRemovalHandler);
		mockCatalogElementRemovalHandler.cleanUp(mockTransaction);
		replay(mockCatalogElementRemovalHandler);
		
		NotificationSettingDeleteHandler mockNotificationSettingDeleteHandler = createMock(NotificationSettingDeleteHandler.class);
		expect(mockNotificationSettingDeleteHandler.forEventType(eventTypeToDelete)).andReturn(mockNotificationSettingDeleteHandler);
		mockNotificationSettingDeleteHandler.remove(mockTransaction);
		replay(mockNotificationSettingDeleteHandler);
		
		EventTypeArchiveHandler sut = new EventTypeArchiveHandlerImpl(mockEventTypeSaver, mockListDeleter, mockAssociatedEventTypeListDeleteHandler, mockCatalogElementRemovalHandler, mockNotificationSettingDeleteHandler);
		
		
		sut.forEventType(eventTypeToDelete).remove(mockTransaction);
		
		verify(mockEventTypeSaver);
		verify(mockAssociatedEventTypeListDeleteHandler);
		verify(mockListDeleter);
		verify(mockCatalogElementRemovalHandler);
		verify(mockNotificationSettingDeleteHandler);
	}

	
	@Test
	public void should_be_able_to_archive_inspection_type_tied_to_1_asset_type() {
		EventType eventTypeToDelete = anEventType().build();
		
		EventListArchiveHandlerImp mockEventListDeleter = createMock(EventListArchiveHandlerImp.class);
		expect(mockEventListDeleter.setEventType(eventTypeToDelete)).andReturn(mockEventListDeleter);
		expect(mockEventListDeleter.summary(mockTransaction)).andReturn(new EventArchiveSummary());
		replay(mockEventListDeleter);
		
		AssociatedEventTypeListDeleteHandler mockAssociatedEventTypeListDeleteHandler = createMock(AssociatedEventTypeListDeleteHandler.class);
		expect(mockAssociatedEventTypeListDeleteHandler.setEventType(eventTypeToDelete)).andReturn(mockAssociatedEventTypeListDeleteHandler);
		expect(mockAssociatedEventTypeListDeleteHandler.summary(mockTransaction)).andReturn(new AssociatedEventTypeDeleteSummary(1L, 0L, 0L));
		replay(mockAssociatedEventTypeListDeleteHandler);
		
		CatalogElementRemovalHandler mockCatalogElementRemovalHandler = createMock(CatalogElementRemovalHandler.class);
		replay(mockCatalogElementRemovalHandler);
		
		NotificationSettingDeleteHandler mockNotificationSettingDeleteHandler = createMock(NotificationSettingDeleteHandler.class);
		expect(mockNotificationSettingDeleteHandler.forEventType(eventTypeToDelete)).andReturn(mockNotificationSettingDeleteHandler);
		expect(mockNotificationSettingDeleteHandler.summary(mockTransaction)).andReturn(new NotificationSettingDeleteSummary());
		replay(mockNotificationSettingDeleteHandler);
		
		EventTypeArchiveHandler sut = new EventTypeArchiveHandlerImpl(null, mockEventListDeleter, mockAssociatedEventTypeListDeleteHandler, mockCatalogElementRemovalHandler, mockNotificationSettingDeleteHandler);
		
		EventTypeArchiveSummary summary = sut.forEventType(eventTypeToDelete).summary(mockTransaction);
		
		assertTrue(summary.canBeRemoved());
		assertEquals(new Long(1), summary.getRemoveFromAssetTypes());
		assertEquals(new Long(0), summary.getEventArchiveSummary().getDeleteEvents());
		verify(mockEventListDeleter);
		verify(mockAssociatedEventTypeListDeleteHandler);
		verify(mockCatalogElementRemovalHandler);
		verify(mockNotificationSettingDeleteHandler);
	}
	
	@Test
	public void should_be_to_archive_inspection_type_tied_to_1_asset_type() {
		EventType eventTypeToDelete = anEventType().build();
		
		
		EventTypeSaver mockEventTypeSaver = createMock(EventTypeSaver.class);
		expect(mockEventTypeSaver.update(mockTransaction, eventTypeToDelete)).andReturn(eventTypeToDelete);
		replay(mockEventTypeSaver);
		
		EventListArchiveHandlerImp mockListDeleter = createMock(EventListArchiveHandlerImp.class);
		expect(mockListDeleter.setEventType(eventTypeToDelete)).andReturn(mockListDeleter);
		mockListDeleter.remove(mockTransaction);
		replay(mockListDeleter);
		
		AssociatedEventTypeListDeleteHandler mockAssociatedEventListTypeDeleteHandler = createMock(AssociatedEventTypeListDeleteHandler.class);
		expect(mockAssociatedEventListTypeDeleteHandler.setEventType(eventTypeToDelete)).andReturn(mockAssociatedEventListTypeDeleteHandler);
		mockAssociatedEventListTypeDeleteHandler.remove(mockTransaction);
		replay(mockAssociatedEventListTypeDeleteHandler);
		
		CatalogElementRemovalHandler mockCatalogElementRemovalHandler = createMock(CatalogElementRemovalHandler.class);
		expect(mockCatalogElementRemovalHandler.setEventType(eventTypeToDelete)).andReturn(mockCatalogElementRemovalHandler);
		mockCatalogElementRemovalHandler.cleanUp(mockTransaction);
		replay(mockCatalogElementRemovalHandler);
		
		NotificationSettingDeleteHandler mockNotificationSettingDeleteHandler = createMock(NotificationSettingDeleteHandler.class);
		expect(mockNotificationSettingDeleteHandler.forEventType(eventTypeToDelete)).andReturn(mockNotificationSettingDeleteHandler);
		mockNotificationSettingDeleteHandler.remove(mockTransaction);
		replay(mockNotificationSettingDeleteHandler);
		
		EventTypeArchiveHandler sut = new EventTypeArchiveHandlerImpl(mockEventTypeSaver, mockListDeleter, mockAssociatedEventListTypeDeleteHandler, mockCatalogElementRemovalHandler, mockNotificationSettingDeleteHandler);
		
		sut.forEventType(eventTypeToDelete).remove(mockTransaction);
		
		verify(mockEventTypeSaver);
		verify(mockListDeleter);
		verify(mockAssociatedEventListTypeDeleteHandler);
		verify(mockCatalogElementRemovalHandler);
		verify(mockNotificationSettingDeleteHandler);
	}
	
	@Test
	public void should_be_able_to_archive_inspection_type_tied_used_in_a_master_inspection() {
		EventType eventTypeToDelete = anEventType().build();
		
	
		EventListArchiveHandlerImp mockListDeleter = createMock(EventListArchiveHandlerImp.class);
		expect(mockListDeleter.setEventType(eventTypeToDelete)).andReturn(mockListDeleter);
		expect(mockListDeleter.summary(mockTransaction)).andReturn(new EventArchiveSummary().setEventsPartOfMaster(5L));
		replay(mockListDeleter);
		
		AssociatedEventTypeListDeleteHandler mockAssociatedEventListTypeDeleteHandler = createMock(AssociatedEventTypeListDeleteHandler.class);
		expect(mockAssociatedEventListTypeDeleteHandler.setEventType(eventTypeToDelete)).andReturn(mockAssociatedEventListTypeDeleteHandler);
		expect(mockAssociatedEventListTypeDeleteHandler.summary(mockTransaction)).andReturn(new AssociatedEventTypeDeleteSummary(1L, 0L, 0L));
		replay(mockAssociatedEventListTypeDeleteHandler);
		
		NotificationSettingDeleteHandler mockNotificationSettingDeleteHandler = createMock(NotificationSettingDeleteHandler.class);
		expect(mockNotificationSettingDeleteHandler.forEventType(eventTypeToDelete)).andReturn(mockNotificationSettingDeleteHandler);
		expect(mockNotificationSettingDeleteHandler.summary(mockTransaction)).andReturn(new NotificationSettingDeleteSummary());
		replay(mockNotificationSettingDeleteHandler);
		
		EventTypeArchiveHandler sut = new EventTypeArchiveHandlerImpl(null, mockListDeleter, mockAssociatedEventListTypeDeleteHandler, null, mockNotificationSettingDeleteHandler);
		
		EventTypeArchiveSummary summary = sut.forEventType(eventTypeToDelete).summary(mockTransaction);
		
		assertFalse(summary.canBeRemoved());
		assertEquals(new Long(5), summary.getEventArchiveSummary().getEventsPartOfMaster());
		verify(mockListDeleter);
		verify(mockAssociatedEventListTypeDeleteHandler);
		verify(mockNotificationSettingDeleteHandler);
	}
	
}
