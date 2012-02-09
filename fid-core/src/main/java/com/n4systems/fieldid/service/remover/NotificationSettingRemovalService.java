package com.n4systems.fieldid.service.remover;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.handlers.remover.summary.NotificationSettingDeleteSummary;
import com.n4systems.model.EventType;
import com.n4systems.model.notificationsettings.NotificationSetting;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

public class NotificationSettingRemovalService extends FieldIdPersistenceService {

    @Transactional
	public void cleanUp(EventType eventType) {
		List<NotificationSetting> notificationsToDelete = getNotificationsWithEventType(eventType);

        for (NotificationSetting notificationSetting : notificationsToDelete) {
            persistenceService.remove(notificationSetting);
        }
	}

    @Transactional
	public NotificationSettingDeleteSummary summary(EventType eventType) {
		NotificationSettingDeleteSummary summary = new NotificationSettingDeleteSummary();

		summary.setNotificationsToDelete(getNotificationsWithEventType(eventType).size());

		return summary;
	}

	@SuppressWarnings("unchecked")
	private List<NotificationSetting> getNotificationsWithEventType(EventType eventType) {
        HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("eventTypeId", eventType.getId());
        Query query = persistenceService.createQuery("FROM " + NotificationSetting.class.getName() + " ns, IN (ns.eventTypes) eventTypeId WHERE eventTypeId = :eventTypeId", params);

        return (List<NotificationSetting>)query.getResultList();
	}

}
