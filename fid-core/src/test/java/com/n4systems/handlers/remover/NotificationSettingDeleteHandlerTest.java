package com.n4systems.handlers.remover;

import static com.n4systems.model.builders.EventTypeBuilder.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.n4systems.model.EventType;
import org.junit.Test;

import com.n4systems.handlers.remover.summary.NotificationSettingDeleteSummary;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.persistence.FieldIdTransaction;
import com.n4systems.persistence.Transaction;
import com.n4systems.test.helpers.FluentArrayList;


public class NotificationSettingDeleteHandlerTest {

	

	@Test
	public void should_find_1_notification_to_delete() {
		EventType eventTypeToDelete = anEventType().build();
		
		
		Query mockQuery = createMock(Query.class);
		expect(mockQuery.setParameter((String)anyObject(), anyLong())).andReturn(mockQuery);
		expect(mockQuery.getResultList()).andReturn(new FluentArrayList<Long>().stickOn(1L));
		replay(mockQuery);
		
		EntityManager mockEntityManager = createMock(EntityManager.class);
		expect(mockEntityManager.createQuery(find("^SELECT \\S*id FROM " + NotificationSetting.class.getName()))).andReturn(mockQuery);
		replay(mockEntityManager);
		
		Transaction mockTransaction = createMock(FieldIdTransaction.class);
		expect(mockTransaction.getEntityManager()).andReturn(mockEntityManager);
		replay(mockTransaction);
		
		NotificationSettingDeleteHandler sut = new NotificationSettingDeleteHandlerImpl();
		
		NotificationSettingDeleteSummary summary = sut.forEventType(eventTypeToDelete).summary(mockTransaction);
		
		assertEquals(new Long(1), summary.getNotificationsToDelete());
		verify(mockTransaction);
		verify(mockEntityManager);
		verify(mockQuery);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_delete_1_notification() {
		EventType eventTypeToDelete = anEventType().build();
		
		List<Long> inspectionToRemove = new FluentArrayList<Long>().stickOn(1L);
		
		Query mockQuery = createMock(Query.class);
		expect(mockQuery.setParameter((String)anyObject(), anyLong())).andReturn(mockQuery);
		expect(mockQuery.getResultList()).andReturn(inspectionToRemove);
		replay(mockQuery);
		
		Query mockDeleteQuery = createMock(Query.class);
		expect(mockDeleteQuery.setParameter((String)anyObject(), (List<Long>)anyObject())).andReturn(mockQuery);
		expect(mockDeleteQuery.executeUpdate()).andReturn(1);
		replay(mockDeleteQuery);
		
		EntityManager mockEntityManager = createMock(EntityManager.class);
		expect(mockEntityManager.createQuery(find("^SELECT \\S*id FROM " + NotificationSetting.class.getName()))).andReturn(mockQuery);
		expect(mockEntityManager.createQuery(find("^DELETE FROM " + NotificationSetting.class.getName()))).andReturn(mockDeleteQuery);
		replay(mockEntityManager);
		
		Transaction mockTransaction = createMock(FieldIdTransaction.class);
		expect(mockTransaction.getEntityManager()).andReturn(mockEntityManager);
		expectLastCall().atLeastOnce();
		replay(mockTransaction);
		
		NotificationSettingDeleteHandler sut = new NotificationSettingDeleteHandlerImpl();
		
		sut.forEventType(eventTypeToDelete).remove(mockTransaction);
		
		
		verify(mockTransaction);
		verify(mockEntityManager);
		verify(mockQuery);
	}
	
	
	
}
