package com.n4systems.handlers.remover;

import java.util.List;

import javax.persistence.Query;

import com.n4systems.handlers.remover.summary.NotificationSettingDeleteSummary;
import com.n4systems.model.EventType;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.utils.LargeInListQueryExecutor;

public class NotificationSettingDeleteHandlerImpl implements NotificationSettingDeleteHandler {

	private EventType eventType;
	
	public void remove(Transaction transaction) {
		List<Long> notificationsToDelete = getNotificationIdsWithEventType(transaction);
		
		String deleteQuery = "DELETE FROM " + NotificationSetting.class.getName() + " WHERE id IN (:ids)";
		new LargeInListQueryExecutor().executeUpdate(transaction.getEntityManager().createQuery(deleteQuery), notificationsToDelete);
	}

	public NotificationSettingDeleteSummary summary(Transaction transaction) {
		NotificationSettingDeleteSummary summary = new NotificationSettingDeleteSummary();
		
		summary.setNotificationsToDelete(getNotificationIdsWithEventType(transaction).size());
		
		return summary;
	}

	@SuppressWarnings("unchecked")
	private List<Long> getNotificationIdsWithEventType(Transaction transaction) {
		Query query = transaction.getEntityManager().createQuery("SELECT ns.id FROM " + NotificationSetting.class.getName() + " ns, IN (ns.eventTypes) eventTypeId WHERE eventTypeId = :eventTypeId" );
		query.setParameter("eventTypeId", eventType.getId());
		
		 
		return (List<Long>)query.getResultList();
	}


	public NotificationSettingDeleteHandlerImpl forEventType(EventType eventType) {
		this.eventType = eventType;
		return this;
	}

}
